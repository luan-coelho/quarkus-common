package com.luan.common.service.user;

import com.luan.common.dto.user.UserResponseDto;
import com.luan.common.mapper.user.UserMapper;
import com.luan.common.model.user.Address;
import com.luan.common.model.user.User;
import com.luan.common.repository.user.UserRepository;
import com.luan.common.service.BaseService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.UUID;

@ApplicationScoped
public class UserService extends BaseService<User, UserResponseDto, UUID, UserRepository, UserMapper> {

    public UserService() {
        super(User.class);
    }

    @Transactional
    public User save(User entity) {
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

}
