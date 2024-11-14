package com.luan.common.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.luan.common.annotation.AuditFieldLabel;
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

    @AuditFieldLabel("CEP")
    private String zipCode;

    @AuditFieldLabel("Rua")
    private String street;

    @AuditFieldLabel("Bairro")
    private String city;

    @AuditFieldLabel("Estado")
    private String state;

    @AuditFieldLabel("Número")
    private String number;

    @AuditFieldLabel("Complemento")
    private String complement;

    @JsonIgnore
    @OneToOne
    private User user;

}
