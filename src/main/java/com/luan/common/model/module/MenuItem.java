package com.luan.common.model.module;

import com.luan.common.model.user.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "menu_item", uniqueConstraints = {
        @UniqueConstraint(name = "pk_menu_item", columnNames = {"id"}),
})
public class MenuItem extends BaseEntity {

    private String label;
    private String route;
    private String icon;
    private Integer position;

    @ManyToOne
    private MenuItem parent;

}
