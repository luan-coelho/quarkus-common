package com.luan.common.dto.user.naturalperson;

import java.util.UUID;

/**
 * DTO for {@link com.luan.common.model.user.NaturalPerson}
 */
public record NaturalPersonResponseDto(
                UUID id,
                String name,
                String email,
                String phone,
                String address,
                String city,
                String state,
                String zipCode) {

}