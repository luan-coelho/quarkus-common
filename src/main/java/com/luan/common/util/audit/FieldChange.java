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

    private String label;
    private String name;
    private Object oldValue;
    private Object newValue;
    private int order;
//    private List<FieldChange> subFieldChanges;

}