package com.luan.common.model.user;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "`user`")
public class User extends BaseEntity {

    private String name;
    private String email;
    private String cpf;
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Address address;

    private String primaryPhone;
    private String secondaryPhone;

}