package com.luan.common.hibernate;

import com.luan.common.model.user.AuditRevisionEntity;
import org.hibernate.envers.RevisionListener;

/**
 * Classe responsável por setar o usuário e cpf que está realizando a operação de auditoria.
 */
public class AuditRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        AuditRevisionEntity revEntity = (AuditRevisionEntity) revisionEntity;
        revEntity.setUsername(getCurrentUsername());
        revEntity.setCpf(getCurrentCpf());
    }

    private String getCurrentUsername() {
        return "Luan Coelho";
    }

    private String getCurrentCpf() {
        return "12345678901";
    }

}