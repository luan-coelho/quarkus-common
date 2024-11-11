package com.luan.common.service;

import com.luan.common.mapper.UserMapper;
import com.luan.common.model.user.Address;
import com.luan.common.model.user.User;
import com.luan.common.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class UserService extends BaseService<User, UUID, UserRepository, UserMapper> {

    protected UserService() {
        super(User.class);
    }

    @Override
    public User save(User entity) {
        Address address = entity.getAddress();
        address.setUser(entity);
        return super.save(entity);
    }

}
