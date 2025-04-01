package com.luan.common.model.user;

import com.luan.common.model.module.Module;
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
        @UniqueConstraint(name = "pk_company", columnNames = { "company_id" }),
})
public class Company extends BaseEntity {

    private String name;
    private String cnpj;
    private String stateRegistration;
    private String corporateName;

    @ManyToMany(mappedBy = "companies")
    private List<Employee> employees;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    @ManyToMany(mappedBy = "companies")
    private List<Module> modules;
}