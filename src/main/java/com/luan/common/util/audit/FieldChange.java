package com.luan.common.util.audit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FieldChange {

    private String fieldName;
    private Object oldValue;
    private Object newValue;
    private String revisionAuthor;
    private Date revisionDateTime;

}