package com.luan.common.service;

import com.luan.common.annotation.AuditFieldLabel;
import com.luan.common.mapper.BaseMapper;
import com.luan.common.model.user.AuditRevisionEntity;
import com.luan.common.model.user.BaseEntity;
import com.luan.common.repository.Repository;
import com.luan.common.util.audit.FieldChange;
import com.luan.common.util.audit.Revision;
import com.luan.common.util.audit.RevisionComparator;
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

import java.lang.reflect.Field;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@SuppressWarnings({"CdiInjectionPointsInspection"})
public abstract class BaseService<T extends BaseEntity, DTO, UUID, R extends Repository<T, UUID>,
        M extends BaseMapper<T, DTO>> implements Service<T, DTO, UUID> {

    @Getter
    @Inject
    R repository;

    @Inject
    M mapper;

    private final Class<T> entityType;

    @Transactional
    @Override
    public DTO save(T entity) {
        this.repository.persist(entity);
        return mapper.toDto(entity);
    }

    @Override
    public DTO findById(UUID uuid) {
        T entity = this.repository.findByIdOptional(uuid).orElseThrow(() -> new NotFoundException("Entity not found"));
        return this.mapper.toDto(entity);
    }

    @Override
    public List<DTO> findAll() {
        return this.mapper.toDto(this.repository.listAll());
    }

    @Override
    public DataPagination<DTO> findAll(Pageable pageable) {
        DataPagination<T> dataPagination = this.repository.listAll(pageable);
        return mapper.toDto(dataPagination);
    }

    @Transactional
    @Override
    public DTO updateById(T entity, UUID uuid) {
        T databaseEntity = this.repository
                .findByIdOptional(uuid)
                .orElseThrow(() -> new NotFoundException("Entity not found"));
        this.mapper.copyProperties(entity, databaseEntity);
        repository.getEntityManager().merge(databaseEntity);
        return this.mapper.toDto(databaseEntity);
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
        return this.repository.listAll(pageable);
    }

    @Transactional
    @Override
    public List<Revision<DTO>> findAllRevisions(UUID entityId) {
        AuditReader reader = AuditReaderFactory.get(getRepository().getEntityManager());
        List<Number> revisionsNumbers = reader.getRevisions(entityType, entityId);
        List<Revision<DTO>> revisionList = new ArrayList<>();
        for (Number revisionNumber : revisionsNumbers) {
            T entity = reader.find(entityType, entityId, revisionNumber.intValue());
            DTO entityDto = mapper.toDto(entity);
            Revision<DTO> revisionObj = new Revision<>();
            revisionObj.setRevisionId(revisionNumber);
            revisionObj.setRevisionType(getRevisionType(reader, entityId, revisionNumber));
            revisionObj.setEntity(entityDto);
            revisionList.add(revisionObj);
        }
        return revisionList;
    }

    private RevisionComparator<T> buildComparator(UUID entityId, Integer revisionId) {
        RevisionComparator<T> comparator = new RevisionComparator<>();

        AuditReader auditReader = AuditReaderFactory.get(getRepository().getEntityManager());
        comparator.setAuditReader(auditReader);

        List<Number> revisions = auditReader.getRevisions(entityType, entityId);

        int currentRevisionIndex = revisions.indexOf(revisionId);
        comparator.setCurrentRevisionIndex(currentRevisionIndex);

        if (currentRevisionIndex == -1) {
            throw new IllegalArgumentException("Revisão não encontrada.");
        }

        Number currentRevision = revisions.get(currentRevisionIndex);

        T currentEntity = auditReader.find(entityType, entityId, currentRevision);
        comparator.setCurrentEntity(currentEntity);
        AuditRevisionEntity currentRevisionEntity = auditReader.findRevision(AuditRevisionEntity.class, currentRevision);
        comparator.setCurrentRevisionEntity(currentRevisionEntity);

        T previousEntity;
        if (currentRevisionIndex > 0) {
            Number previousRevision = revisions.get(currentRevisionIndex - 1);
            previousEntity = auditReader.find(entityType, entityId, previousRevision);
            comparator.setPreviousEntity(previousEntity);
        }

        return comparator;
    }

    @Override
    @Transactional
    public RevisionComparison compareWithPreviousRevision(UUID entityId, Integer revisionId) {
        RevisionComparator<T> comparator = buildComparator(entityId, revisionId);

        List<FieldChange> fieldChanges;
        if (comparator.getCurrentRevisionIndex() == 0) {
            fieldChanges = generateFieldChangesForCreation(mapper.toDto(comparator.getCurrentEntity()));
        } else {
            fieldChanges = compareEntities(
                    mapper.toDto(comparator.getPreviousEntity()),
                    mapper.toDto(comparator.getCurrentEntity())
            );
        }

        RevisionComparison comparison = new RevisionComparison();
        AuditRevisionEntity currentRevisionEntity = comparator.getCurrentRevisionEntity();

        comparison.setRevisionAuthor(currentRevisionEntity.getUsername());
        comparison.setRevisionDateTime(new Date(currentRevisionEntity.getTimestamp()));
        comparison.setFieldChanges(fieldChanges);
        comparison.setRevisionType(getRevisionType(comparator.getAuditReader(), entityId, revisionId).name());

        return comparison;
    }

    private List<FieldChange> generateFieldChangesForCreation(DTO currentEntity) {
        List<FieldChange> fieldChanges = new ArrayList<>();
        if (currentEntity == null) {
            return fieldChanges;
        }

        for (Field field : currentEntity.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);

                if (field.getName().startsWith("$$_") ||
                    field.getName().contains("hibernate") ||
                    field.getName().equals("serialVersionUID") ||
                    field.getName().equals("id") ||
                    field.getDeclaringClass().getName().startsWith("java.")) {
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

                int order = field.getAnnotation(AuditFieldLabel.class).order();
                if (order == 0) {
                    change.setOrder(fieldChanges.size() + 1);
                }

                fieldChanges.add(change);

            } catch (IllegalAccessException e) {
                throw new RuntimeException("Erro ao acessar os campos da entidade", e);
            }
        }

        return fieldChanges;
    }

    private List<FieldChange> compareEntities(Object previousEntity, Object currentEntity) {
        List<FieldChange> fieldChanges = new ArrayList<>();
        if (previousEntity == null || currentEntity == null) {
            return fieldChanges;
        }

        for (Field field : currentEntity.getClass().getDeclaredFields()) {
            try {
                // Ignorar campos desnecessários ou problemáticos
                if (field.getName().startsWith("$$_") ||
                    field.getName().contains("hibernate") ||
                    field.getName().equals("serialVersionUID") ||
                    field.getName().equals("id") ||
                    field.getDeclaringClass().getName().startsWith("java.")) {
                    continue;
                }

                field.setAccessible(true);

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

                if (oldValue != null && newValue != null && !isPrimitiveOrWrapperOrString(field.getType())) {
                    List<FieldChange> subFieldChanges = compareEntities(oldValue, newValue);
                    if (!subFieldChanges.isEmpty()) {
                        FieldChange change = new FieldChange();
                        change.setLabel(label);
                        change.setName(field.getName());
                        change.setOldValue(oldValue);
                        change.setNewValue(newValue);
                        change.setSubFieldChanges(subFieldChanges);

                        int order = field.getAnnotation(AuditFieldLabel.class).order();
                        if (order == 0) {
                            change.setOrder(subFieldChanges.size() + 1);
                        }

                        fieldChanges.add(change);
                    }
                } else if (!Objects.equals(oldValue, newValue)) {
                    FieldChange change = new FieldChange();
                    change.setLabel(label);
                    change.setName(field.getName());
                    change.setOldValue(oldValue);
                    change.setNewValue(newValue);
                    int order = field.getAnnotation(AuditFieldLabel.class).order();
                    if (order == 0) {
                        change.setOrder(fieldChanges.size() + 1);
                    }
                    fieldChanges.add(change);
                }
            } catch (IllegalAccessException e) {
                log.error("Erro ao comparar entidades em revisão. {}", e.getMessage());
            }
        }

        return fieldChanges;
    }

    private <C> boolean isPrimitiveOrWrapperOrString(Class<C> type) {
        return type.isPrimitive() ||
               type == String.class ||
               type == Boolean.class ||
               type == Byte.class ||
               type == Character.class ||
               type == Double.class ||
               type == Float.class ||
               type == Integer.class ||
               type == Long.class ||
               type == Short.class ||
               type == Void.class ||
               type == java.util.UUID.class ||
               type == Date.class ||
               type == Temporal.class;
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