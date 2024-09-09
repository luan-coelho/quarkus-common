package com.luan.common.handle.rest.response;

import jakarta.validation.ConstraintViolation;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ConstraintErrorResponse extends ErrorResponse {

    private Map<String, List<String>> errors;

    public void addError(String field, String errorMessage) {
        if (this.errors == null) {
            this.errors = new HashMap<>();
        }

        if (this.errors.containsKey(field)) {
            this.errors.get(field).add(errorMessage);
        } else {
            this.errors.put(field, new ArrayList<>());
            this.errors.get(field).add(errorMessage);
        }
    }

    public void addErrors(Set<ConstraintViolation<?>> constraintViolations) {
        if (this.errors == null) {
            this.errors = new HashMap<>();
        }

        constraintViolations.forEach(constraintViolation -> {
            String field = constraintViolation.getPropertyPath().toString();
            String errorMessage = constraintViolation.getMessage();
            if (this.errors.containsKey(field)) {
                this.errors.get(field).add(errorMessage);
            } else {
                this.errors.put(field, new ArrayList<>());
                this.errors.get(field).add(errorMessage);
            }
        });
    }

}
