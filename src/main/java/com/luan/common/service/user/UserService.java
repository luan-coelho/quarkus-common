package com.luan.common.service.user;

import com.luan.common.dto.user.CreateAdminUserDto;
import com.luan.common.dto.user.UserResponseDto;
import com.luan.common.mapper.user.UserMapper;
import com.luan.common.model.user.Address;
import com.luan.common.model.user.User;
import com.luan.common.repository.user.UserRepository;
import com.luan.common.service.BaseService;
import com.luan.common.util.BrazilUtils;
import com.luan.common.util.ValidationUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
public class UserService extends BaseService<User, UserResponseDto, UUID, UserRepository, UserMapper> {

    public UserService() {
        super(User.class);
    }

    @Transactional
    public User save(CreateAdminUserDto dto) {
        Set<ConstraintViolation<?>> violations = new HashSet<>();

        if (existsByCpf(dto.cpf())) {
            violations.add(ValidationUtil.createViolation("cpf", "O CPF informado já está cadastrado"));
        }

        if (existsByEmail(dto.email())) {
            violations.add(ValidationUtil.createViolation("email", "O email informado já está cadastrado"));
        }

        ValidationUtil.validateWithCustomViolations(dto, violations);

        User entity = getMapper().toEntity(dto);
        entity.setCpf(BrazilUtils.removeCpfMask(entity.getCpf()));
        Address address = entity.getAddress();
        if (address != null) {
            address.setUser(entity);
        }
        return super.save(entity);
    }

    @Transactional
    public User saveAdmin() {
        User administrator = getRepository().findByEmail("admin@gmail.com");
        if (administrator == null) {
            administrator = new User();
            administrator.setName("Admin");
            administrator.setEmail("admin@gmail.com");
            administrator.setPassword("admin");
            administrator.setCpf("00000000000");
            save(administrator);
        }
        return administrator;
    }

    public boolean existsByCpf(String cpf) {
        return getRepository().existsByCpf(BrazilUtils.removeCpfMask(cpf));
    }

    public boolean existsByEmail(String email) {
        return getRepository().existsByEmail(email);
    }

}
