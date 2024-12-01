package com.luan.common.model.module;

import com.luan.common.model.user.BaseEntity;
import com.luan.common.service.module.MenuItemService;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "menu_item", uniqueConstraints = {
        @UniqueConstraint(name = "pk_menu_item", columnNames = {"id"}),
})
public class MenuItem extends BaseEntity {

    private String label;
    private String description;
    private String route;
    private String icon;
    private Integer position;

    @ManyToMany
    private List<MenuItem> subItems;

}
