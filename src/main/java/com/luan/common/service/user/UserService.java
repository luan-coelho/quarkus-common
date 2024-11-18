package com.luan.common.service.user;

import com.luan.common.mapper.user.UserMapper;
import com.luan.common.model.user.Address;
import com.luan.common.model.user.User;
import com.luan.common.repository.user.UserRepository;
import com.luan.common.service.BaseService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.UUID;

@ApplicationScoped
public class UserService extends BaseService<User, UUID, UserRepository, UserMapper> {

    protected UserService() {
        super(User.class);
    }

    @Transactional
    @Override
    public User save(User entity) {
        Address address = entity.getAddress();
        address.setUser(entity);
        return super.save(entity);
    }

}
