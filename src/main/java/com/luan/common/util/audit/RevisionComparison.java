package com.luan.common.util.audit;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RevisionComparison<T> {

    private Revision<T> revision;
    private List<FieldChange> fieldChanges;

}
