package com.luan.common.model.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Company extends BaseEntity {

    private String name;
    private String cnpj;
    private String stateRegistration;
    private String corporateName;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id", referencedColumnName = "company_id",
            foreignKey = @ForeignKey(name = "fk_company_employee"))
    private List<Employee> employees;

}