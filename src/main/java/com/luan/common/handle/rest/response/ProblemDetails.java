package com.luan.common.handle.rest.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProblemDetails implements Serializable {

    @Serial
    private static final long serialVersionUID = 1308690675848099013L;

    private String type;
    private String title;
    private int status;
    private String detail;
    private String instance;

}