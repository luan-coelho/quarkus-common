package com.luan.common.service.user.naturalperson;

import java.util.UUID;

import com.luan.common.dto.user.naturalperson.NaturalPersonResponseDto;
import com.luan.common.mapper.user.NaturalPersonMapper;
import com.luan.common.model.user.NaturalPerson;
import com.luan.common.repository.user.NaturalPersonRepository;
import com.luan.common.service.BaseService;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NaturalPersonService extends
        BaseService<NaturalPerson, NaturalPersonResponseDto, UUID, NaturalPersonRepository, NaturalPersonMapper> {

    protected NaturalPersonService() {
        super(NaturalPerson.class);
    }

}
