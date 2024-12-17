package com.luan.common.dto.module;

import com.luan.common.annotation.AuditFieldLabel;

import java.time.LocalDateTime;
import java.util.UUID;

public record ModuleUserResponseDto(
        UUID id,
        @AuditFieldLabel("Nome")
        String name,
        @AuditFieldLabel("E-mail")
        String email,
        @AuditFieldLabel("CPF")
        String cpf,
        @AuditFieldLabel("Data de criação")
        LocalDateTime createdAt,
        @AuditFieldLabel("Data de atualização")
        LocalDateTime updatedAt,
        @AuditFieldLabel("Situação")
        boolean active
) {

}
