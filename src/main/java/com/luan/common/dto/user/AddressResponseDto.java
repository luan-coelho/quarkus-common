package com.luan.common.dto.user;

import com.luan.common.annotation.AuditFieldLabel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.luan.common.model.user.Address}
 */
public record AddressResponseDto(UUID id,
                                 @AuditFieldLabel("CEP")
                                 String zipCode,
                                 @AuditFieldLabel("Rua")
                                 String street,
                                 @AuditFieldLabel("Município")
                                 String city,
                                 @AuditFieldLabel("Estado")
                                 String state,
                                 @AuditFieldLabel("Número")
                                 String number,
                                 @AuditFieldLabel("Complemento")
                                 String complement,
                                 boolean active) implements Serializable {
}