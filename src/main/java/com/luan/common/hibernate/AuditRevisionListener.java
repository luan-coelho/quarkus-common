package com.luan.common.hibernate;

import com.luan.common.model.user.AuditRevisionEntity;
import org.hibernate.envers.RevisionListener;

public class AuditRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        AuditRevisionEntity revEntity = (AuditRevisionEntity) revisionEntity;
        revEntity.setUsername(getCurrentUsername());
    }

    private String getCurrentUsername() {
        return "Luan Coelho";
    }

}