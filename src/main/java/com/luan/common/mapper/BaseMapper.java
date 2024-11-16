package com.luan.common.mapper;

import com.luan.common.model.user.BaseEntity;

public interface BaseMapper<T extends BaseEntity> {

    default void copyProperties(T source, T target) {

    }

    default <S> S toResponse(T targetClass) {
        return null;
    }

}
