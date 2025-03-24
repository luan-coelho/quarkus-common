package com.luan.common.controller;

import com.luan.common.model.user.BaseEntity;
import com.luan.common.service.Service;

import jakarta.inject.Inject;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.Getter;

@Getter
@Produces(MediaType.APPLICATION_JSON)
@SuppressWarnings({ "CdiInjectionPointsInspection", "RestParamTypeInspection" })
public abstract class BaseController<T extends BaseEntity, DTO, UUID, S extends Service<T, DTO, UUID>>
        implements CrudController<T, DTO, UUID, S>,
        RevisionController<T, DTO, UUID, S>,
        PaginationController<T, DTO, UUID, S> {

    @Inject
    protected S service;

}
