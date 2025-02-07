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
import org.hibernate.exception.ConstraintViolationException;

import java.lang.reflect.Field;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@SuppressWarnings({"CdiInjectionPointsInspection"})
public abstract class BaseService<T extends BaseEntity, DTO, UUID, R extends Repository<T, UUID>,
        M extends BaseMapper<T, DTO>> implements Service<T, DTO, UUID> {

    @Getter
    @Inject
    R repository;

    @Getter
    @Inject
    M mapper;

    private final Class<T> entityClass;

    @Transactional
    @Override
    public T save(T entity) {
        this.repository.persist(entity);
        return entity;
    }

    @Override
    public DTO saveAndReturnDto(T entity) {
        return this.mapper.toDto(save(entity));
    }

    @Override
    public T findById(UUID uuid) {
        return this.repository
                .findByIdOptional(uuid)
                .orElseThrow(() -> new NotFoundException("Entidade não encontrada"));
    }

    @Override
    public DTO findByIdAndReturnDto(UUID uuid) {
        return this.mapper.toDto(findById(uuid));
    }

    @Override
    public List<T> findAll() {
        return this.repository.listAll();
    }

    @Override
    public List<DTO> findAllAndReturnDto() {
        return this.mapper.toDto(findAll());
    }

    @Override
    public DataPagination<T> findAll(Pageable pageable) {
        return this.repository.listAll(pageable);
    }

    @Override
    public DataPagination<DTO> findAllAndReturnDto(Pageable pageable) {
        return this.mapper.toDto(findAll(pageable));
    }

    @Transactional
    @Override
    public T updateById(UUID uuid, T entity) {
        T databaseEntity = findById(uuid);
        this.mapper.copyProperties(entity, databaseEntity);
        update(databaseEntity);
        return databaseEntity;
    }

    @Transactional
    @Override
    public DTO updateByIdAndReturnDto(UUID uuid, T entity) {
        return this.mapper.toDto(updateById(uuid, entity));
    }

    @Transactional
    public T update(T entity) {
        return this.repository.getEntityManager().merge(entity);
    }

    @Transactional
    @Override
    public void deleteById(UUID uuid) {
        T entity = this.repository.findByIdOptional(uuid).orElseThrow(() -> new NotFoundException("Entity not found"));
        try {
            repository.getEntityManager().remove(entity);
            repository.getEntityManager().flush();
        } catch (ConstraintViolationException e) {
            throw new IllegalStateException("Não é possível excluir o item, pois ele está sendo referenciado.");
        }
    }

    @Transactional
    @Override
    public T activateById(UUID uuid) {
        boolean exists = existsById(uuid);
        if (!exists) {
            throw new NotFoundException("Entidade não encontrada");
        }
        repository.activeById(uuid);
        return findById(uuid);
    }

    @Transactional
    @Override
    public DTO activateByIdAndReturnDto(UUID uuid) {
        return this.mapper.toDto(activateById(uuid));
    }

    @Transactional
    @Override
    public T disableById(UUID uuid) {
        boolean exists = existsById(uuid);
        if (!exists) {
            throw new NotFoundException("Entidade não encontrada");
        }
        repository.desactiveById(uuid);
        return findById(uuid);
    }

    @Transactional
    @Override
    public DTO disableByIdAndReturnDto(UUID uuid) {
        return this.mapper.toDto(disableById(uuid));
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
    public void deleteAll() {
        this.repository.deleteAll();
    }

    @Override
    public List<Revision<T>> findAllRevisions(UUID entityId) {
        AuditReader reader = AuditReaderFactory.get(repository.getEntityManager());
        List<Number> revisionsNumbers = reader.getRevisions(entityClass, entityId);
        List<Revision<T>> revisionList = new ArrayList<>();
        for (Number revisionNumber : revisionsNumbers) {
            T entity = reader.find(entityClass, entityId, revisionNumber.intValue());
            Revision<T> revisionObj = new Revision<>();
            revisionObj.setRevisionId(revisionNumber);
            revisionObj.setRevisionType(getRevisionType(reader, entityId, revisionNumber));
            revisionObj.setEntity(entity);
            revisionObj.setRevisionDate(reader.getRevisionDate(revisionNumber));
            AuditRevisionEntity revision = reader.findRevision(AuditRevisionEntity.class, revisionNumber);
            revisionObj.setUsername(revision.getUsername());
            revisionObj.setCpf(revision.getCpf());
            revisionList.add(revisionObj);
        }
        return revisionList;
    }

    @Override
    public RevisionComparison<T> compareWithPreviousRevision(UUID entityId, Integer revisionId) {
        AuditReader auditReader = AuditReaderFactory.get(repository.getEntityManager());

        // Obtém todas as revisões para a entidade
        List<Number> revisions = auditReader.getRevisions(entityClass, entityId);
        int currentRevisionIndex = revisions.indexOf(revisionId);

        if (currentRevisionIndex == -1) {
            throw new IllegalArgumentException("Revision not found for the entity");
        }

        // Obtém a revisão anterior, se existir
        Integer previousRevisionId = currentRevisionIndex > 0
                ? revisions.get(currentRevisionIndex - 1).intValue()
                : null;

        // Carrega as entidades das revisões
        T currentEntity = auditReader.find(entityClass, entityId, revisionId);
        Object previousEntity = previousRevisionId != null
                ? auditReader.find(entityClass, entityId, previousRevisionId)
                : null;

        // Compara as entidades para detectar alterações
        List<FieldChange> fieldChanges = compareEntities(previousEntity, currentEntity);

        // Obtém metadados da revisão atual
        AuditRevisionEntity revisionEntity = auditReader.findRevision(AuditRevisionEntity.class, revisionId);
        RevisionType revisionType = getRevisionType(auditReader, entityId, revisionId);

        Revision<T> revision = new Revision<>();
        revision.setRevisionId(revisionId);
        revision.setRevisionType(revisionType);
        revision.setEntity(currentEntity);
        revision.setRevisionDate(revisionEntity.getRevisionDate());
        revision.setUsername(revisionEntity.getUsername());
        revision.setCpf(revisionEntity.getCpf());

        return new RevisionComparison<T>(revision, fieldChanges);
    }

    @Override
    public List<RevisionComparison<T>> findAllRevisionsComparisons(UUID entityId) {
        List<Revision<T>> revisions = findAllRevisions(entityId);
        List<RevisionComparison<T>> comparisons = new ArrayList<>();
        for (Revision<T> revision : revisions) {
            RevisionComparison<T> comparison = compareWithPreviousRevision(entityId, revision.getRevisionId().intValue());
            comparisons.add(comparison);
        }
        return comparisons;
    }

    private List<FieldChange> compareEntities(Object oldEntity, Object newEntity) {
        List<FieldChange> changes = new ArrayList<>();

        if (oldEntity == null) {
            return new ArrayList<>();
        }

        if (newEntity == null) return changes;

        Class<?> clazz = oldEntity.getClass();
        List<Field> fields = getAllFields(clazz);

        for (Field field : fields) {
            field.setAccessible(true);

            if (field.getName().startsWith("$$_") ||
                field.getName().contains("hibernate") ||
                field.getName().equals("serialVersionUID") ||
                field.getName().equals("id") ||
                field.getDeclaringClass().getName().startsWith("java.")) {
                continue;
            }

            AuditFieldLabel annotation = field.getAnnotation(AuditFieldLabel.class);
            String label = field.getName();
            boolean ignore = false;
            int order = 0;

            if (annotation != null) {
                // Se o campo deve ser ignorado, pula para o próximo
                if (annotation.ignore()) continue;

                label = annotation.value();
                order = annotation.order();
            }

            try {
                Object oldValue = oldEntity != null ? field.get(oldEntity) : null;
                Object newValue = newEntity != null ? field.get(newEntity) : null;

                if (!Objects.equals(oldValue, newValue)) {
                    FieldChange change = new FieldChange();
                    change.setName(field.getName());
                    change.setLabel(label);

                    // Define a ordenação
                    if (order == 0) {
                        order = changes.size() + 1; // Ordem baseada na posição no fluxo
                    }
                    change.setOrder(order);

                    change.setOldValue(oldValue);
                    change.setNewValue(newValue);

                    // Comparação recursiva para subobjetos
                    if (isComplex(field.getType())) {
                        List<FieldChange> subChanges = compareEntities(oldValue, newValue);
                        change.setSubFieldChanges(subChanges);
                    }

                    changes.add(change);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error comparing fields", e);
            }
        }

        // Ordena as mudanças com base na ordem definida
        changes.sort(Comparator.comparingInt(FieldChange::getOrder));
        return changes;
    }

    private List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    private boolean isComplex(Class<?> type) {
        return !type.isPrimitive() &&
               !type.isEnum() &&
               !CharSequence.class.isAssignableFrom(type) &&
               !Number.class.isAssignableFrom(type) &&
               !Date.class.isAssignableFrom(type) &&
               !type.equals(java.util.UUID.class);
    }

    @SuppressWarnings("unchecked")
    private RevisionType getRevisionType(AuditReader auditReader, UUID entityId, Number revisionId) {
        List<Object[]> revisionData = auditReader.createQuery()
                .forRevisionsOfEntity(entityClass, false, true)
                .add(AuditEntity.id().eq(entityId))
                .add(AuditEntity.revisionNumber().eq(revisionId))
                .getResultList();

        if (!revisionData.isEmpty()) {
            return (RevisionType) revisionData.getFirst()[2];
        }
        return RevisionType.ADD;
    }

}