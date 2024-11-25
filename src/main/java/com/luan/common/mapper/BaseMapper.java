package com.luan.common.mapper;

import com.luan.common.model.user.BaseEntity;
import com.luan.common.util.pagination.DataPagination;

import java.util.List;

public interface BaseMapper<T extends BaseEntity, DTO> {

    default void copyProperties(T source, T target) {

    }

    DTO toDto(T target);

    List<DTO> toDto(List<T> target);

    DataPagination<DTO> toDto(DataPagination<T> target);

}
