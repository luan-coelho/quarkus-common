package com.luan.common.util.audit;

import com.luan.common.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.RevisionType;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Revision<T> {

    private Number id;
    private RevisionType type;
    private User user;
    private Date date;
    private T entity;

}
