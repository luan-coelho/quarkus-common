package com.luan.common;

import com.luan.common.model.user.AuditRevisionEntity;
import org.hibernate.envers.RevisionListener;

import java.util.Date;

public class AuditRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        AuditRevisionEntity revEntity = (AuditRevisionEntity) revisionEntity;
        revEntity.setUsername(getCurrentUsername());
        revEntity.setRevisionDate(new Date());
    }

    private String getCurrentUsername() {
        return "Luan Coelho";
    }

}