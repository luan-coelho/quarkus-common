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
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

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

    @Transactional
    @Override
    public List<Revision<T>> findAllRevisions(UUID entityId) {
        AuditReader reader = AuditReaderFactory.get(getRepository().getEntityManager());
        List<Number> revisionsNumbers = reader.getRevisions(entityType, entityId);
        List<Revision<T>> revisionList = new ArrayList<>();
        for (Number revisionNumber : revisionsNumbers) {
            T t = reader.find(entityType, entityId, revisionNumber.intValue());
            Revision<T> revisionObj = new Revision<>();
            revisionObj.setRevisionId(revisionNumber);
            revisionObj.setEntity(t);
            revisionList.add(revisionObj);
        }
        return revisionList;
    }

    @Override
    @Transactional
    public RevisionComparison compareWithPreviousRevision(UUID entityId, Integer revisionId) {
        AuditReader reader = AuditReaderFactory.get(getRepository().getEntityManager());

        // Obtém a revisão atual e a anterior
        T currentRevision = reader.find(entityType, entityId, revisionId);
        Number previousRevisionId = reader.getRevisionNumberForDate(reader.getRevisionDate(revisionId).toInstant().minusMillis(1));
        if (previousRevisionId == null) {
            return null; // Se não houver revisão anterior, retorna null ou outro tratamento adequado
        }
        T previousRevision = reader.find(entityType, entityId, previousRevisionId);

        // Configuração inicial do objeto de comparação
        AuditRevisionEntity currentRevEntity = reader.findRevision(AuditRevisionEntity.class, revisionId);

        RevisionComparison comparison = new RevisionComparison();
        comparison.setRevisionAuthor(currentRevEntity.getUsername());
        comparison.setRevisionDateTime(currentRevEntity.getRevisionDate());
        comparison.setRevisionType("");

        List<FieldChange> fieldChanges = new ArrayList<>();

        // Comparação dos campos entre as revisões
        for (Field field : entityType.getDeclaredFields()) {
            String label = field.getName().replaceFirst("^[a-z]", (field.getName().charAt(0) + "").toUpperCase());

            if (field.isAnnotationPresent(AuditFieldLabel.class)) {
                label = field.getAnnotation(AuditFieldLabel.class).value();
            }
            field.setAccessible(true);

            try {
                Object oldValue = field.get(previousRevision);
                Object newValue = field.get(currentRevision);

                if (!Objects.equals(oldValue, newValue)) {
                    fieldChanges.add(new FieldChange(field.getName(), label, oldValue, newValue, fieldChanges.size()));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        comparison.setFieldChanges(fieldChanges);
        return comparison;
    }


}