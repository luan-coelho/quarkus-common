package com.luan.common.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

import java.io.Serializable;

/**
 * DTO for {@link com.luan.common.model.user.User}
 */
public record CreateAdminUserDto(@NotBlank(message = "Informe o nome") String name,
                                 @NotBlank(message = "Informe o sobrenome") String surname,
                                 @Email(message = "Informe um email válido") String email,
                                 @CPF(message = "Informe um CPF válido") String cpf,
                                 @NotBlank(message = "Informe o telefone principal") String primaryPhone,
                                 String secondaryPhone) implements Serializable {

}
