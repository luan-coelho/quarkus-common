package com.luan.common.model.module;

import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.envers.Audited;
import org.hibernate.type.SqlTypes;

import com.luan.common.annotation.AuditFieldLabel;
import com.luan.common.model.user.BaseEntity;
import com.luan.common.model.user.User;
import com.luan.common.model.user.Company;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Audited
@Entity
@Table(name = "modules", uniqueConstraints = {
                @UniqueConstraint(name = "pk_module", columnNames = { "module_id" }),
})
public class Module extends BaseEntity {

        @AuditFieldLabel("Nome")
        private String name;

        @AuditFieldLabel("Descrição")
        private String description;

        @Column(columnDefinition = "json", name = "menu_items")
        @ManyToMany
        @JoinTable(name = "module_menu_item", joinColumns = @JoinColumn(name = "module_id"), inverseJoinColumns = @JoinColumn(name = "menu_item_id"))
        private List<MenuItem> menuItems;

        @Column(columnDefinition = "json", name = "menu_items_order")
        @JdbcTypeCode(SqlTypes.JSON)
        private String menuItemsOrder;

        @ManyToMany(mappedBy = "modules")
        private List<User> users;

        @ManyToMany
        @JoinTable(name = "company_module", joinColumns = @JoinColumn(name = "module_id"), inverseJoinColumns = @JoinColumn(name = "company_id"))
        private List<Company> companies;
}
