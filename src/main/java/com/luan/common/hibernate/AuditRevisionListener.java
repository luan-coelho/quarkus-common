package com.luan.common.hibernate;

import com.luan.common.model.user.AuditRevisionEntity;
import com.luan.common.model.user.User;
import com.luan.common.service.user.UserService;
import jakarta.enterprise.inject.spi.CDI;
import org.hibernate.envers.RevisionListener;

/**
 * Classe responsável por setar o usuário e cpf que está realizando a operação de auditoria.
 */
public class AuditRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        AuditRevisionEntity auditRevisionEntity = (AuditRevisionEntity) revisionEntity;
        UserService userService = CDI.current().select(UserService.class).get();
        User adminUser = userService.saveAdmin();
        auditRevisionEntity.setUser(adminUser);
    }

}