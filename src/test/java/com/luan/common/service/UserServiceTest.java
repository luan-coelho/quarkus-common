package com.luan.common.service;

import com.luan.common.model.user.Address;
import com.luan.common.model.user.User;
import com.luan.common.util.pagination.DateUtils;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
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
        assertNotNull(user.getUpdatedAt());
        assertEquals(0, user.getVersion());
        assertTrue(user.isActive());
        assertNotNull(user.getAddress());

        /* ADDRESS */
        assertNotNull(address.getId());
        assertTrue(DateUtils.isSameDayIgnoringTime(today, address.getCreatedAt()));
        assertNotNull(address.getUpdatedAt());
        assertEquals(0, address.getVersion());
        assertTrue(address.isActive());
        assertNotNull(address.getUser());
    }

    @Test
    void testUpdate() {
        User user = createUser();
        LocalDate today = DateUtils.getCurrentDate();

        service.save(user);
        Address address = user.getAddress();

        user.setName("Test 2");
        address.setStreet("Test 2");
        service.updateById(user, user.getId());

        /* USER */
        assertTrue(DateUtils.isSameDayIgnoringTime(today, user.getCreatedAt()));
        assertTrue(DateUtils.isSameDayIgnoringTime(today, user.getUpdatedAt()));
        assertEquals(1, user.getVersion());
        assertTrue(user.isActive());
        assertNotNull(user.getAddress());

        /* ADDRESS */
        assertTrue(DateUtils.isSameDayIgnoringTime(today, address.getCreatedAt()));
        assertTrue(DateUtils.isSameDayIgnoringTime(today, address.getUpdatedAt()));
        assertEquals(1, address.getVersion());
        assertTrue(address.isActive());
        assertNotNull(address.getUser());
    }

    private User createUser() {
        User user = new User();
        user.setName("Test");
        user.setEmail("test@gmail.com");
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