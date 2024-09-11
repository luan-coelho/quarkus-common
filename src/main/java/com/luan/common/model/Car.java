package com.luan.common.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Car {

    @NotNull(message = "Name is required")
    private String name;

}
