package com.luan.common.model.module;

import com.luan.common.model.user.BaseEntity;
import com.luan.common.model.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "module", uniqueConstraints = {
        @UniqueConstraint(name = "pk_module", columnNames = {"id"}),
})
public class Module extends BaseEntity {

    private String name;

    @Column(columnDefinition = "json", name = "menu_items")
    @ManyToMany
    @JoinTable(name = "module_menu_item",
            joinColumns = @JoinColumn(name = "module_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_item_id"))
    private List<MenuItem> menuItems;

    @Column(columnDefinition = "json", name = "menu_items_order")
    @JdbcTypeCode(SqlTypes.JSON)
    private String menuItemsOrder;

    @ManyToMany(mappedBy = "modules")
    private List<User> users;

}
