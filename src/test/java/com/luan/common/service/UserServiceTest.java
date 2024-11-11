package com.luan.common.service;

import com.luan.common.model.user.Address;
import com.luan.common.model.user.User;
import com.luan.common.util.pagination.DateUtils;
import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UserServiceTest {

    @Inject
    UserService service;

    @Test
    void testSave() {
        User user = createUser();
        LocalDate today = DateUtils.getCurrentDate();

        service.save(user);
        Address address = user.getAddress();

        /* USER */
        assertNotNull(user.getId());
        assertTrue(DateUtils.isSameDayIgnoringTime(today, user.getCreatedAt()));
        assertNull(user.getUpdatedAt());
        assertEquals(0, user.getVersion());
        assertTrue(user.isActive());
        assertNotNull(user.getAddress());

        /* ADDRESS */
        assertNotNull(address.getId());
        assertTrue(DateUtils.isSameDayIgnoringTime(today, address.getCreatedAt()));
        assertNull(address.getUpdatedAt());
        assertEquals(0, address.getVersion());
        assertTrue(address.isActive());
        assertNotNull(address.getUser());
    }

    @Transactional
    @Test
    void testUpdate() {
        User persistedUser = createUser();
        User updatedUser = createUser();
        LocalDate today = DateUtils.getCurrentDate();

        service.save(persistedUser);

        updatedUser.setName("João");
        service.updateById(updatedUser, persistedUser.getId());

        /* USER */
        assertTrue(DateUtils.isSameDayIgnoringTime(today, persistedUser.getCreatedAt()));
        assertTrue(DateUtils.isSameDayIgnoringTime(today, persistedUser.getUpdatedAt()));
        assertEquals(1, persistedUser.getVersion());
        assertTrue(persistedUser.isActive());
        assertNotNull(persistedUser.getAddress());
    }

    private User createUser() {
        User user = new User();
        user.setName("Luan");
        user.setEmail("luan@gmail.com");
        Address address = createAddress();
        user.setAddress(address);
        return user;
    }

    private Address createAddress() {
        Address address = new Address();
        address.setStreet("Test");
        address.setNumber("123");
        address.setCity("Test");
        address.setState("Test");
        address.setZipCode("12345678");
        return address;
    }

}