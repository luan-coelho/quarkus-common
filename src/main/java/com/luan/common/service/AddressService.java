package com.luan.common.service;

import com.luan.common.model.user.Address;
import com.luan.common.repository.AddressRepository;

import java.util.UUID;

public class AddressService extends BaseService<Address, UUID, AddressRepository> {

    @Override
    public Address save(Address entity) {
        return super.save(entity);
    }

}
