package com.luan.common.repository;

import com.luan.common.model.user.User;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class UserRepository extends Repository<User, UUID> {


}
