package com.luan.common.dto.user;

public record UserCreationResponseDto(Long id,
                                      String name,
                                      String email,
                                      String cpf,
                                      String primaryPhone,
                                      String secondaryPhone) {

}
