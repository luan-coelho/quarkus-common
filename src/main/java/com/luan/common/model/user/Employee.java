package com.luan.common.model.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Employee extends BaseEntity {

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

}
