package com.luan.common.repository.module;

import com.luan.common.model.module.Module;
import com.luan.common.repository.Repository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ModuleRepository extends Repository<Module, UUID> {

    @Override
    public Optional<Module> findByIdOptional(UUID id) {
        // language=jpaql
        String query = """
                SELECT m
                FROM Module m
                    LEFT JOIN FETCH m.menuItems
                    LEFT JOIN m.users
                WHERE m.id = ?1
                """;
        return find(query, id).singleResultOptional();
    }

    public List<Module> findByUserId(UUID userId) {
        // language=jpaql
        String query = """
                SELECT m
                FROM Module m
                    LEFT JOIN FETCH m.menuItems
                    LEFT JOIN m.users u
                WHERE u.id = ?1
                """;
        return list(query, userId);
    }

}
