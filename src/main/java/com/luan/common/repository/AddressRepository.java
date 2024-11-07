package com.luan.common.repository;

import com.luan.common.model.user.Address;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class AddressRepository implements Repository<Address, UUID> {


}
