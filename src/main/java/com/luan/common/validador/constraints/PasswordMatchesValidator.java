package com.luan.common.validador.constraints;

import com.luan.common.dto.validador.constraints.PasswordValidationDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, PasswordValidationDto> {

    @Override
    public boolean isValid(PasswordValidationDto validationDto, ConstraintValidatorContext context) {
        if (validationDto == null) {
            return true;
        }
        return validationDto.password().equals(validationDto.matchingPassword());
    }

}
