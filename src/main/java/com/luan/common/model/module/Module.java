package com.luan.common.model.module;

import com.luan.common.model.user.BaseEntity;
import com.luan.common.model.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "module", uniqueConstraints = {
        @UniqueConstraint(name = "pk_module", columnNames = {"id"}),
})
public class Module extends BaseEntity {

    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private List<MenuItem> menuItems;

    @ManyToMany(mappedBy = "modules")
    private List<User> users;

}
