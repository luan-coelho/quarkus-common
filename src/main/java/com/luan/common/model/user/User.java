package com.luan.common.model.user;

import com.luan.common.model.module.Module;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.util.List;

@Getter
@Setter
@Entity
@Audited
@Table(name = "`user`", uniqueConstraints = {
        @UniqueConstraint(name = "pk_user", columnNames = {"id"}),
})
public class User extends BaseEntity {

    private String name;
    private String email;
    private String cpf;
    private String password;
    private String primaryPhone;
    private String secondaryPhone;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", foreignKey = @ForeignKey(name = "fk_user_address"))
    private Address address;

    @ElementCollection(targetClass = Role.class)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private List<Role> roles;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToMany
    @JoinTable(name = "user_module",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "module_id"))
    private List<Module> modules;

}