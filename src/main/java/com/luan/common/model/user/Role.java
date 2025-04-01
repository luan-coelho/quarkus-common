package com.luan.common.model.user;

public enum Role {
    SYSTEM_ADMIN, // Administrador do sistema
    COMPANY_ADMIN, // Administrador da empresa cliente
    COMPANY_USER, // Funcionário comum da empresa cliente
    NATURAL_CUSTOMER, // Cliente pessoa física
    LEGAL_CUSTOMER // Cliente pessoa jurídica (empresa)
}
