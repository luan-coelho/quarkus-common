package com.luan.common.service;

import com.luan.common.mapper.AddressMapper;
import com.luan.common.model.user.Address;
import com.luan.common.repository.AddressRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class AddressService extends BaseService<Address, UUID, AddressRepository, AddressMapper> {

    protected AddressService() {
        super(Address.class);
    }

    @Override
    public Address save(Address entity) {
        return super.save(entity);
    }

}
