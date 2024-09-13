package com.luan.common.handle.rest.response;

import jakarta.validation.ConstraintViolation;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@EqualsAndHashCode(callSuper = false)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ConstraintErrorResponse extends ErrorResponse {

    @Serial
    private static final long serialVersionUID = 1778598015378290270L;

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
            String field = String.valueOf(StreamSupport.stream(constraintViolation
                    .getPropertyPath()
                    .spliterator(), false).reduce((first, second) -> second).orElse(null));
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
