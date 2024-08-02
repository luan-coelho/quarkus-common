package com.luan.common.dto.validador.constraints;

import com.luan.common.validador.constraints.PasswordMatches;

@PasswordMatches
public record PasswordValidationDto(String password, String matchingPassword) {

}
