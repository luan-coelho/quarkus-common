package com.luan.common.dto.user;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * DTO for {@link com.luan.common.model.user.User}
 */
public record CreateNaturalPersonDto(@NotBlank(message = "Informe o nome") String name) implements Serializable {

}
