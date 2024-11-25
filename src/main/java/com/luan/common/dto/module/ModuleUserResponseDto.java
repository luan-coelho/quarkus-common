package com.luan.common.dto.module;

import com.luan.common.annotation.AuditFieldLabel;

import java.util.UUID;

public record ModuleUserResponseDto(
        UUID id,
        @AuditFieldLabel("Nome")
        String name,
        @AuditFieldLabel("E-mail")
        String email,
        @AuditFieldLabel("CPF")
        String cpf,
        @AuditFieldLabel("Situação")
        boolean active
) {

}
