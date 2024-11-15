package com.luan.common.model.user;

import com.luan.common.annotation.AuditFieldLabel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Getter
@Setter
@Entity
@Audited
@Table(name = "`user`")
public class User extends BaseEntity {

    @AuditFieldLabel("Nome")
    private String name;

    @AuditFieldLabel("E-mail")
    private String email;

    @AuditFieldLabel("CPF")
    private String cpf;

    @AuditFieldLabel(ignore = true)
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", foreignKey = @ForeignKey(name = "fk_user_address"))
    private Address address;

    @AuditFieldLabel("Telefone Principal")
    private String primaryPhone;

    @AuditFieldLabel("Telefone Secundário")
    private String secondaryPhone;

}