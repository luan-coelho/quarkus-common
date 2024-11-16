package com.luan.common.model.user;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Getter
@Setter
@Audited
@Entity
public class Address extends BaseEntity {

    private String zipCode;
    private String street;
    private String city;
    private String state;
    private String number;
    private String complement;

    @OneToOne
    private User user;

}
