package com.luan.common.model.user;

import com.luan.common.model.module.Module;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Audited
@Table(name = "user_module_accesses", uniqueConstraints = {
        @UniqueConstraint(name = "pk_user_module_access", columnNames = { "id" }),
})
public class UserModuleAccess extends BaseEntity {

    @ManyToOne
    private User user;

    @ManyToOne
    private Module module;

    @Column(name = "access_start_date")
    private LocalDate accessStartDate;

    @Column(name = "access_end_date")
    private LocalDate accessEndDate;

}
