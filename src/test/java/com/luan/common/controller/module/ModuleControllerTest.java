package com.luan.common.controller.module;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.luan.common.dto.module.ModuleResponseDto;
import com.luan.common.mapper.module.ModuleMapper;
import com.luan.common.model.module.MenuItem;
import com.luan.common.model.module.Module;
import com.luan.common.model.user.Address;
import com.luan.common.model.user.User;
import com.luan.common.service.module.ModuleService;
import com.luan.common.service.user.UserService;
import com.luan.common.util.JsonUtils;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class ModuleControllerTest extends BaseControllerTest {

    @Inject
    ModuleMapper mapper;

    @Inject
    ModuleService moduleService;

    @Inject
    UserService userService;

    @Test
    void whenCreateModule() {
        MenuItem menuItemParent = new MenuItem();
        menuItemParent.setLabel("Usuários");
        menuItemParent.setRoute("/users");
        menuItemParent.setIcon("fa fa-users");
        menuItemParent.setPosition(1);
        menuItemParent.setVisible(true);
        menuItemParent.setActive(true);

        MenuItem menuItem = new MenuItem();
        menuItem.setLabel("Funcionários");
        menuItem.setRoute("/employees");
        menuItem.setIcon("fa fa-users");
        menuItem.setPosition(1);
        menuItem.setVisible(true);
        menuItem.setActive(true);
        menuItem.setParent(menuItemParent);

        Module module = new Module();
        module.setName("Gestão de Usuários");
        module.setMenuItems(new ArrayList<>());
        module.getMenuItems().add(menuItem);

        User user = createUser();
        userService.save(user);
        user.getModules().add(module);

        module.setUsers(new ArrayList<>());
        module.getUsers().add(user);

        String json;
        try {
            ModuleResponseDto dto = mapper.toDto(module);
            json = JsonUtils.toJson(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        given().contentType(ContentType.JSON)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(json)
                .when()
                .post("/module")
                .then()
                .log().all()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .body("id", is(notNullValue()))
                .body("name", is(module.getName()))
                .body("menuItems", hasSize(1))
                .body("menuItems[0].id", is(notNullValue()))
                .body("menuItems[0].label", is(menuItem.getLabel()))
                .body("menuItems[0].route", is(menuItem.getRoute()))
                .body("menuItems[0].icon", is(menuItem.getIcon()))
                .body("menuItems[0].position", is(menuItem.getPosition()))
                .body("menuItems[0].visible", is(menuItem.isVisible()))
                .body("menuItems[0].active", is(menuItem.isActive()))
                .body("menuItems[0].parent", is(nullValue()))
                .body("users", hasSize(1))
                .body("users[0].id", is(notNullValue()))
                .body("users[0].name", is(user.getName()))
                .body("users[0].email", is(user.getEmail()))
                .body("active", is(module.isActive()));
    }

    private User createUser() {
        User user = new User();
        user.setName("Alexandre Enrico Castro");
        user.setCpf("839.007.511-37");
        user.setEmail("alexandre@oana.com.br");
        user.setPassword("admin");
        user.setActive(true);
        user.setModules(new ArrayList<>());
        user.setAddress(createAddress());
        return user;
    }

    private Address createAddress() {
        Address address = new Address();
        address.setStreet("Rua dos Monarcas");
        address.setNumber("258");
        address.setCity("Pici");
        address.setState("Fortaleza");
        address.setZipCode("60510460");
        return address;
    }

    @Override
    String getUrl() {
        return "/module";
    }

}