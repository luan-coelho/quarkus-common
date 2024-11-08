package com.luan.common.service;

import com.luan.common.model.user.BaseEntity;
import com.luan.common.repository.Repository;
import com.luan.common.util.audit.AuditObjectComparator;
import com.luan.common.util.audit.FieldChange;
import com.luan.common.util.pagination.DataPagination;
import com.luan.common.util.pagination.Pageable;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.glassfish.jaxb.core.v2.model.core.ID;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.exception.AuditException;
import org.hibernate.envers.query.AuditEntity;

import java.util.List;
import java.util.Optional;

@SuppressWarnings({"CdiInjectionPointsInspection"})
public abstract class BaseService<T extends BaseEntity, UUID, R extends Repository<T, UUID>>
        implements Service<T, UUID> {

    @Inject
    public R repository;

    private final Class<T> entityType;

    protected BaseService(Class<T> entityType) {
        this.entityType = entityType;
    }

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

    @Transactional
    @Override
    public T updateById(T entity, UUID uuid) {
        if (!this.existsById(uuid)) {
            throw new NotFoundException("Entity not found");
        }
        this.repository.persist(entity);
        return entity;
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
    public List<T> listRevisions(ID id) {
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

    public T findRevision(ID id, int revision) {
        try {
            AuditReader auditReader = AuditReaderFactory.get(repository.getEntityManager());
            return auditReader.find(entityType, id, revision);
        } catch (AuditException e) {
            throw new RuntimeException("Error fetching specific revision", e);
        }
    }

    public List<FieldChange> compareWithRevision(ID entityId, int revisionNumber) throws IllegalAccessException {
        AuditReader auditReader = AuditReaderFactory.get(repository.getEntityManager());

        T oldVersion = auditReader.find(entityType, entityId, revisionNumber);
        if (oldVersion == null) {
            throw new IllegalArgumentException("Revisão não encontrada para o número fornecido.");
        }

        T currentVersion = repository.getEntityManager().find(entityType, entityId);

        return AuditObjectComparator.compareObjects(oldVersion, currentVersion);
    }

}