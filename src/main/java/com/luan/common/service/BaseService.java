package com.luan.common.service;

import com.luan.common.annotation.AuditFieldLabel;
import com.luan.common.mapper.BaseMapper;
import com.luan.common.model.user.AuditRevisionEntity;
import com.luan.common.model.user.BaseEntity;
import com.luan.common.repository.Repository;
import com.luan.common.util.audit.FieldChange;
import com.luan.common.util.audit.Revision;
import com.luan.common.util.audit.RevisionComparison;
import com.luan.common.util.pagination.DataPagination;
import com.luan.common.util.pagination.Pageable;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@SuppressWarnings({"CdiInjectionPointsInspection"})
public abstract class BaseService<T extends BaseEntity, UUID, R extends Repository<T, UUID>, M extends BaseMapper<T>>
        implements Service<T, UUID> {

    @Getter
    @Inject
    R repository;

    @Inject
    M mapper;

    private final Class<T> entityType;

    @Transactional
    @Override
    public T save(T entity) {
        this.repository.persist(entity);
        return entity;
    }

    @Override
    public T findById(UUID uuid) {
        Optional<T> entity = this.repository.findByIdOptional(uuid);
        if (entity.isEmpty()) {
            throw new NotFoundException("Entity not found");
        }
        return entity.get();
    }

    @Override
    public List<T> findAll() {
        return this.repository.listAll();
    }

    @Override
    public DataPagination<T> findAll(Pageable pageable) {
        return this.findAllPaginated(pageable);
    }

    @Transactional
    @Override
    public T updateById(T entity, UUID uuid) {
        T databaseEntity = this.findById(uuid);
        this.mapper.copyProperties(entity, databaseEntity);
        repository.getEntityManager().merge(databaseEntity);
        return databaseEntity;
    }

    @Transactional
    @Override
    public void deleteById(UUID uuid) {
        if (!this.existsById(uuid)) {
            throw new NotFoundException("Entity not found");
        }
        this.repository.deleteById(uuid);
    }

    @Override
    public boolean existsById(UUID id) {
        return this.repository.existsById(id);
    }

    public DataPagination<T> findAllPaginated(Pageable pageable) {
        return this.repository.findAll(pageable);
    }

    @SuppressWarnings("unchecked")
    @Transactional
    @Override
    public List<Revision<T>> findAllRevisions(UUID entityId) {
        AuditReader auditReader = AuditReaderFactory.get(getRepository().getEntityManager());
        return auditReader.createQuery()
                .forRevisionsOfEntity(entityType, true, true)
                .add(AuditEntity.id().eq(entityId))
                .getResultList();
    }

    @Override
    @Transactional
    public RevisionComparison compareWithPreviousRevision(UUID entityId, Integer revisionId) {
        AuditReader auditReader = AuditReaderFactory.get(getRepository().getEntityManager());

        List<Number> revisions = auditReader.getRevisions(entityType, entityId);

        int currentRevisionIndex = revisions.indexOf(revisionId);
        if (currentRevisionIndex == -1) {
            throw new IllegalArgumentException("Revisão não encontrada.");
        }

        Number currentRevision = revisions.get(currentRevisionIndex);
        T currentEntity = auditReader.find(entityType, entityId, currentRevision);
        AuditRevisionEntity currentRevisionEntity = auditReader.findRevision(AuditRevisionEntity.class, currentRevision);

        T previousEntity = null;
        if (currentRevisionIndex > 0) {
            Number previousRevision = revisions.get(currentRevisionIndex - 1);
            previousEntity = auditReader.find(entityType, entityId, previousRevision);
        }

        List<FieldChange> fieldChanges;
        if (currentRevisionIndex == 0) {
            fieldChanges = generateFieldChangesForCreation(currentEntity);
        } else {
            fieldChanges = compareEntities(previousEntity, currentEntity);
        }

        RevisionComparison comparison = new RevisionComparison();
        comparison.setRevisionAuthor(currentRevisionEntity.getUsername());
        comparison.setRevisionDateTime(new Date(currentRevisionEntity.getTimestamp()));
        comparison.setFieldChanges(fieldChanges);
        comparison.setRevisionType(getRevisionType(auditReader, entityId, currentRevision).name());

        return comparison;
    }

    private List<FieldChange> generateFieldChangesForCreation(T currentEntity) {
        List<FieldChange> fieldChanges = new ArrayList<>();
        if (currentEntity == null) {
            return fieldChanges;
        }

        for (Field field : currentEntity.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);

                if (field.getName().startsWith("$$_") || field.getName().contains("hibernate")) {
                    continue;
                }

                Object value = field.get(currentEntity);

                String label = field.getName();
                if (field.isAnnotationPresent(AuditFieldLabel.class)) {
                    label = field.getAnnotation(AuditFieldLabel.class).value();
                    boolean ignore = field.getAnnotation(AuditFieldLabel.class).ignore();

                    if (ignore) {
                        continue;
                    }
                }

                FieldChange change = new FieldChange();
                change.setLabel(label);
                change.setName(field.getName());
                change.setOldValue(value);
                change.setNewValue(null);
                fieldChanges.add(change);

            } catch (IllegalAccessException e) {
                throw new RuntimeException("Erro ao acessar os campos da entidade", e);
            }
        }

        return fieldChanges;
    }

    private List<FieldChange> compareEntities(T previousEntity, T currentEntity) {
        List<FieldChange> fieldChanges = new ArrayList<>();
        if (previousEntity == null || currentEntity == null) {
            return fieldChanges;
        }

        for (Field field : currentEntity.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);

                if (field.getName().startsWith("$$_") || field.getName().contains("hibernate")) {
                    continue;
                }

                String label = field.getName();
                if (field.isAnnotationPresent(AuditFieldLabel.class)) {
                    label = field.getAnnotation(AuditFieldLabel.class).value();
                    boolean ignore = field.getAnnotation(AuditFieldLabel.class).ignore();

                    if (ignore) {
                        continue;
                    }
                }

                Object oldValue = field.get(previousEntity);
                Object newValue = field.get(currentEntity);

                if (!Objects.equals(oldValue, newValue)) {
                    FieldChange change = new FieldChange();
                    change.setLabel(label);
                    change.setName(field.getName());
                    change.setOldValue(oldValue);
                    change.setNewValue(newValue);
                    fieldChanges.add(change);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Erro ao acessar os campos da entidade", e);
            }
        }

        return fieldChanges;
    }

    @SuppressWarnings("unchecked")
    private RevisionType getRevisionType(AuditReader auditReader, UUID entityId, Number revisionId) {
        List<Object[]> revisionData = auditReader.createQuery()
                .forRevisionsOfEntity(entityType, false, true)
                .add(AuditEntity.id().eq(entityId))
                .add(AuditEntity.revisionNumber().eq(revisionId))
                .getResultList();

        if (!revisionData.isEmpty()) {
            return (RevisionType) revisionData.getFirst()[2];
        }
        return RevisionType.ADD;
    }

}