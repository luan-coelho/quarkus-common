package com.luan.common.dto.user;

import com.luan.common.annotation.AuditFieldLabel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.luan.common.model.user.User}
 */
public record UserResponseDto(UUID id,
                              @AuditFieldLabel("Nome")
                              String name,
                              @AuditFieldLabel("E-mail")
                              String email,
                              @AuditFieldLabel("CPF")
                              String cpf,
                              @AuditFieldLabel("Situação")
                              boolean active,
                              AddressResponseDto address,
                              @AuditFieldLabel("Telefone Principal")
                              String primaryPhone,
                              @AuditFieldLabel("Telefone Secundário")
                              String secondaryPhone) implements Serializable {
}