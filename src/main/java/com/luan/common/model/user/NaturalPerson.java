package com.luan.common.model.user;

import org.hibernate.envers.Audited;
import com.luan.common.model.module.Module;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Audited
@Entity
@Table(name = "natural_persons", uniqueConstraints = {
        @UniqueConstraint(name = "pk_natural_person", columnNames = { "natural_person_id" }),
})
public class NaturalPerson extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String rg;

    @ManyToMany
    @JoinTable(name = "natural_person_module", joinColumns = @JoinColumn(name = "natural_person_id"), inverseJoinColumns = @JoinColumn(name = "module_id"))
    private List<Module> contractedModules;
}
