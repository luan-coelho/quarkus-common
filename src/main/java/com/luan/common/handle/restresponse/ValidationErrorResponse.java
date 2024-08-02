package com.luan.common.handle.restresponse;

import com.luan.common.exception.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorResponse extends ErrorResponse {

    private Map<String, Object> invalidFields;

    public void addInvalidField(String fieldName, Object errorMessage) {
        if (this.invalidFields == null) {
            this.invalidFields = new HashMap<>();
        }
        this.invalidFields.put(fieldName, errorMessage);
    }

}