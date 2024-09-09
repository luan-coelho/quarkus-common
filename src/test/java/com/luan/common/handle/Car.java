package com.luan.common.handle;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
class Car {

    @NotNull(message = "Name is required")
    private String name;

}
