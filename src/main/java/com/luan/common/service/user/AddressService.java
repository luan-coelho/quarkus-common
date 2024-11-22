package com.luan.common.service.user;

import com.luan.common.dto.user.AddressResponseDto;
import com.luan.common.mapper.user.AddressMapper;
import com.luan.common.model.user.Address;
import com.luan.common.repository.user.AddressRepository;
import com.luan.common.service.BaseService;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class AddressService extends BaseService<Address, AddressResponseDto, UUID, AddressRepository, AddressMapper> {

    protected AddressService() {
        super(Address.class);
    }

}
