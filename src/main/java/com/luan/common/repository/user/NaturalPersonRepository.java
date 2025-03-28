package com.luan.common.repository.user;

import java.util.UUID;

import com.luan.common.model.user.NaturalPerson;
import com.luan.common.repository.BaseRepository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NaturalPersonRepository extends BaseRepository<NaturalPerson, UUID> {

}
