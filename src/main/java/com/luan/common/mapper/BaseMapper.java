package com.luan.common.mapper;

import com.luan.common.model.user.BaseEntity;
import com.luan.common.util.pagination.DataPagination;

import java.util.List;

public interface BaseMapper<T extends BaseEntity> {

    default void copyProperties(T source, T target) {

    }

    default <DTO> DTO toDto(T target) {
        return null;
    }

    default <DTO> List<DTO> toDto(List<T> target) {
        return null;
    }

    default <DTO, E> DTO toDto(E target) {
        return null;
    }

}
