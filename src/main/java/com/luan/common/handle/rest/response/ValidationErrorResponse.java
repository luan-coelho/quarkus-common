package com.luan.common.handle.rest.response;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class ValidationErrorResponse extends ErrorResponse {

    private Map<String, Object> invalidFields;

    public void addInvalidField(String field, Object errorMessage) {
        if (this.invalidFields == null) {
            this.invalidFields = new HashMap<>();
        }
        this.invalidFields.put(field, errorMessage);
    }

}
