package com.luan.common.util.audit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.RevisionType;

import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Revision<T> {

    private Number revisionId;
    private RevisionType revisionType;
    private String username;
    private String cpf;
    private Date revisionDate;
    private T entity;

}
