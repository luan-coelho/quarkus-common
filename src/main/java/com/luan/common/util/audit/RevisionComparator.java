package com.luan.common.util.audit;

import com.luan.common.model.user.AuditRevisionEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditReader;

@Setter
@Getter
public final class RevisionComparator<T> {

    AuditReader auditReader;
    T previousEntity;
    T currentEntity;
    AuditRevisionEntity currentRevisionEntity;
    int currentRevisionIndex;

}
