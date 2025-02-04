package com.luan.common.util.audit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FieldChange {

    private String name;
    private String label;
    private Object oldValue;
    private Object newValue;
    private int order;
    private List<FieldChange> subFieldChanges;

}