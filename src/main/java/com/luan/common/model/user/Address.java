package com.luan.common.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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

    @JsonIgnore
    @OneToOne
    private User user;

}
