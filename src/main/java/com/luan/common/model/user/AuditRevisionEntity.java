package com.luan.common.model.user;

import com.luan.common.AuditRevisionListener;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import java.util.Date;

@Setter
@Getter
@RevisionEntity(AuditRevisionListener.class)
public class AuditRevisionEntity extends DefaultRevisionEntity {

    private String username;
    private Date revisionDate;

}
