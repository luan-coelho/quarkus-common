package com.luan.common.service;

import java.util.List;

import org.hibernate.exception.ConstraintViolationException;

import com.luan.common.mapper.BaseMapper;
import com.luan.common.model.user.BaseEntity;
import com.luan.common.repository.BaseRepository;
import com.luan.common.util.pagination.DataPagination;
import com.luan.common.util.pagination.Pageable;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@SuppressWarnings({ "CdiInjectionPointsInspection" })
public abstract class BaseService<T extends BaseEntity, DTO, ID, R extends BaseRepository<T, ID>, M extends BaseMapper<T, DTO>>
        extends RevisionService<T, ID> implements Service<T, DTO, ID> {

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
    public T findById(ID id) {
        return this.repository
                .findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Entidade não encontrada"));
    }

    public List<T> findByIds(List<ID> ids) {
        return this.repository.findByIds(ids);
    }

    @Override
    public DTO findByIdAndReturnDto(ID id) {
        return this.mapper.toDto(findById(id));
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
    public T updateById(ID id, T entity) {
        T databaseEntity = findById(id);
        this.mapper.copyProperties(entity, databaseEntity);
        update(databaseEntity);
        return databaseEntity;
    }

    @Transactional
    @Override
    public DTO updateByIdAndReturnDto(ID id, T entity) {
        return this.mapper.toDto(updateById(id, entity));
    }

    @Transactional
    public T update(T entity) {
        return this.repository.getEntityManager().merge(entity);
    }

    @Transactional
    @Override
    public void deleteById(ID id) {
        T entity = this.repository.findByIdOptional(id).orElseThrow(() -> new NotFoundException("Entity not found"));
        try {
            repository.getEntityManager().remove(entity);
            repository.getEntityManager().flush();
        } catch (ConstraintViolationException e) {
            throw new IllegalStateException("Não é possível excluir o item, pois ele está sendo referenciado.");
        }
    }

    @Transactional
    @Override
    public T activateById(ID id) {
        boolean exists = existsById(id);
        if (!exists) {
            throw new NotFoundException("Entidade não encontrada");
        }
        repository.activeById(id);
        return findById(id);
    }

    @Transactional
    @Override
    public DTO activateByIdAndReturnDto(ID id) {
        return this.mapper.toDto(activateById(id));
    }

    @Transactional
    @Override
    public T disableById(ID id) {
        boolean exists = existsById(id);
        if (!exists) {
            throw new NotFoundException("Entidade não encontrada");
        }
        repository.desactiveById(id);
        return findById(id);
    }

    @Transactional
    @Override
    public DTO disableByIdAndReturnDto(ID id) {
        return this.mapper.toDto(disableById(id));
    }

    @Override
    public boolean existsById(ID id) {
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
    protected Class<T> getEntityClass() {
        return entityClass;
    }

    @Override
    protected EntityManager getEntityManager() {
        return repository.getEntityManager();
    }

}