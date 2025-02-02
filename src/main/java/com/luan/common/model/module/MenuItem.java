package com.luan.common.model.module;

import com.luan.common.model.user.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.List;

@Getter
@Setter
@Audited
@Entity
@Table(name = "menu_item", uniqueConstraints = {
        @UniqueConstraint(name = "pk_menu_item", columnNames = {"id"}),
})
public class MenuItem extends BaseEntity {

    private String label;
    private String description;
    private String route;
    private String icon;

    @ManyToMany
    private List<MenuItem> subItems;

}
