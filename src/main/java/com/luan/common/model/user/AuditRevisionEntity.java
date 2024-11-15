package com.luan.common.model.user;

import com.luan.common.hibernate.AuditRevisionListener;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

@Setter
@Getter
@Entity
@RevisionEntity(AuditRevisionListener.class)
@Table(name = "revinfo")
public class AuditRevisionEntity extends DefaultRevisionEntity {

    private String username;

}
