package com.luan.common.util.audit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.RevisionType;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Revision<T> {

    private Number revisionId;
    private RevisionType revisionType;
    private T entity;

}
