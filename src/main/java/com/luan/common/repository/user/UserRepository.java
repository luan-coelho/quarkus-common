package com.luan.common.repository.user;

import com.luan.common.model.user.User;
import com.luan.common.repository.Repository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class UserRepository extends Repository<User, UUID> {

}
