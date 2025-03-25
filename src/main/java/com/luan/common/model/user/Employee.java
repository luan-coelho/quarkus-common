package com.luan.common.model.user;

import java.util.List;

import org.hibernate.envers.Audited;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Audited
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "employees", uniqueConstraints = {
        @UniqueConstraint(name = "pk_employee", columnNames = { "employee_id" }),
})
public abstract class Employee extends BaseEntity {

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    @ManyToMany
    @JoinTable(name = "employee_company", joinColumns = @JoinColumn(name = "employee_id"), inverseJoinColumns = @JoinColumn(name = "company_id"))
    private List<Company> companies;

}
