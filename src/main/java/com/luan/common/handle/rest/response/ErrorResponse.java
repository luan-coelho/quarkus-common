package com.luan.common.handle.rest.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ErrorResponse {

    private String type;
    private String title;
    private int status;
    private String detail;
    private String instance;

}