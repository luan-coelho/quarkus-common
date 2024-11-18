package com.luan.common.model.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.List;

@Getter
@Setter
@Entity
@Audited
@Table(name = "`user`")
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
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private List<Role> roles;

}