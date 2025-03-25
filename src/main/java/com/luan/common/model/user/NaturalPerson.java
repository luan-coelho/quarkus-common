package com.luan.common.model.user;

import org.hibernate.envers.Audited;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Audited
@Entity
@Table(name = "natural_persons", uniqueConstraints = {
        @UniqueConstraint(name = "pk_natural_person", columnNames = { "id" }),
})
public class NaturalPerson extends BaseEntity {

    @OneToOne
    private User user;

}
