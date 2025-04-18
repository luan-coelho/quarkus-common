package com.luan.common.model.user;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Getter
@Setter
@Audited
@Entity
@Table(name = "addresses", uniqueConstraints = {
        @UniqueConstraint(name = "pk_address", columnNames = { "address_id" }),
})
public class Address extends BaseEntity {

    private String zipCode;
    private String street;
    private String city;
    private String state;
    private String number;
    private String complement;

    @OneToOne(mappedBy = "address")
    private User user;

    @OneToOne(mappedBy = "address")
    private Company company;
}
