package com.luan.common.model.menu;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String label;
    private String route;
    private String icon;
    private Integer order;
    private boolean visible;
    private boolean active = Boolean.TRUE;

    @OneToOne
    private MenuItem parent;

}
