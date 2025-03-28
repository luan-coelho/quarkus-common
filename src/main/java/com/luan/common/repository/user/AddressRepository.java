package com.luan.common.repository.user;

import com.luan.common.model.user.Address;
import com.luan.common.repository.BaseRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class AddressRepository extends BaseRepository<Address, UUID> {

}
