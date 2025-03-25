package com.luan.common.model.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.List;

@Getter
@Setter
@Audited
@Entity
@Table(name = "companies", uniqueConstraints = {
        @UniqueConstraint(name = "pk_company", columnNames = { "id" }),
})
public class Company extends BaseEntity {

    private String name;
    private String cnpj;
    private String stateRegistration;
    private String corporateName;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Employee> employees;

}