package com.luan.common.repository.user;

import com.luan.common.model.user.User;
import com.luan.common.repository.BaseRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class UserRepository extends BaseRepository<User, UUID> {

    public User findByEmail(String email) {
        return find("email", email).firstResult();
    }

    public User findByCpf(String cpf) {
        return find("cpf", cpf).firstResult();
    }

    public boolean existsByCpf(String cpf) {
        return find("cpf", cpf).count() > 0;
    }

    public boolean existsByEmail(String email) {
        return find("email", email).count() > 0;
    }

}
