package com.luan.common.service;

import com.luan.common.mapper.BaseMapper;
import com.luan.common.model.user.AuditRevisionEntity;
import com.luan.common.model.user.BaseEntity;
import com.luan.common.repository.Repository;
import com.luan.common.util.audit.FieldChange;
import com.luan.common.util.pagination.DataPagination;
import com.luan.common.util.pagination.Pageable;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.exception.AuditException;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;

import java.util.*;

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

    @Override
    @SuppressWarnings("unchecked")
    public List<T> listRevisions(UUID id) {
        try {
            AuditReader auditReader = AuditReaderFactory.get(repository.getEntityManager());
            return auditReader.createQuery()
                    .forRevisionsOfEntity(entityType, false, true)
                    .add(AuditEntity.id().eq(id))
                    .getResultList();
        } catch (AuditException e) {
            throw new RuntimeException("Error fetching revisions", e);
        }
    }

    public T findRevision(UUID id, int revision) {
        try {
            AuditReader auditReader = AuditReaderFactory.get(repository.getEntityManager());
            return auditReader.find(entityType, id, revision);
        } catch (AuditException e) {
            throw new RuntimeException("Error fetching specific revision", e);
        }
    }

    @Override
    public List<FieldChange> compareWithPreviousRevision(UUID entityId, int revisionId) {
        AuditReader auditReader = AuditReaderFactory.get(getRepository().getEntityManager());

        // Retrieve the current revision entity
        T currentEntity = auditReader.find(entityType, entityId, revisionId);
        if (currentEntity == null) {
            throw new IllegalArgumentException("Revision ID not found for entity.");
        }

        // Retrieve the current revision metadata
        AuditQuery currentRevisionQuery = auditReader.createQuery()
                .forRevisionsOfEntity(entityType, false, true)
                .add(AuditEntity.id().eq(entityId))
                .add(AuditEntity.revisionNumber().eq(revisionId));
        Object[] currentRevisionData = (Object[]) currentRevisionQuery.getSingleResult();

        String currentRevisionAuthor = ((AuditRevisionEntity) currentRevisionData[1]).getUsername();
        Date currentRevisionDate = ((AuditRevisionEntity) currentRevisionData[1]).getRevisionDate();

        // Retrieve the previous revision number, if it exists
        Number previousRevisionNumber = (Number) auditReader.createQuery()
                .forRevisionsOfEntity(entityType, false, true)
                .add(AuditEntity.id().eq(entityId))
                .add(AuditEntity.revisionNumber().lt(revisionId))
                .addOrder(AuditEntity.revisionNumber().desc())
                .setMaxResults(1)
                .getSingleResult();

        if (previousRevisionNumber == null) {
            // No previous revision found; likely the creation revision
            return generateInitialChanges(currentEntity, currentRevisionAuthor, currentRevisionDate);
        }

        // Fetch the previous revision entity
        T previousEntity = auditReader.find(entityType, entityId, previousRevisionNumber.intValue());

        return generateFieldChanges(entityType, previousEntity, currentEntity, currentRevisionAuthor, currentRevisionDate);
    }

    private List<FieldChange> generateInitialChanges(T entity, String revisionAuthor, Date revisionDate) {
        List<FieldChange> initialChanges = new ArrayList<>();
        for (var field : entity.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object newValue = field.get(entity);
                initialChanges.add(new FieldChange(
                        field.getName(),
                        null, // No previous value as this is the creation
                        newValue,
                        revisionAuthor,
                        revisionDate
                ));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return initialChanges;
    }

    private List<FieldChange> generateFieldChanges(Class<T> entityClass, T previousEntity, T currentEntity, String revisionAuthor, Date revisionDate) {
        List<FieldChange> changes = new ArrayList<>();

        // Compare fields between previous and current entities
        for (var field : entityClass.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object oldValue = field.get(previousEntity);
                Object newValue = field.get(currentEntity);

                if (!Objects.equals(oldValue, newValue)) {
                    changes.add(new FieldChange(
                            field.getName(),
                            oldValue,
                            newValue,
                            revisionAuthor,
                            revisionDate
                    ));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return changes;
    }

}