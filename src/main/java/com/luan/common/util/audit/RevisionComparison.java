package com.luan.common.util.audit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RevisionComparison {

    private String revisionAuthor;
    private Date revisionDateTime;
    private List<FieldChange> fieldChanges;
    private String revisionType;

}
