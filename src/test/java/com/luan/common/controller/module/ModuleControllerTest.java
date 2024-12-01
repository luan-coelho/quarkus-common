package com.luan.common.controller.module;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.luan.common.dto.module.ModuleResponseDto;
import com.luan.common.mapper.module.ModuleMapper;
import com.luan.common.model.module.MenuItem;
import com.luan.common.model.module.Module;
import com.luan.common.model.user.Address;
import com.luan.common.model.user.User;
import com.luan.common.service.module.MenuItemService;
import com.luan.common.service.module.ModuleService;
import com.luan.common.service.user.UserService;
import com.luan.common.util.JsonUtils;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Test for {@link com.luan.common.controller.module.ModuleController}.
 */
@QuarkusTest
class ModuleControllerTest extends BaseControllerTest {

    @Inject
    ModuleMapper mapper;

    @Inject
    ModuleService moduleService;

    @Inject
    UserService userService;

    @Inject
    MenuItemService menuItemService;

    @Test
    public void whenCreateModule() {
        Module module = new Module();
        module.setName("Gestão de Usuários");
        module.setMenuItems(new ArrayList<>());
        module.setUsers(new ArrayList<>());
        module.setActive(true);

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
                .post(getUrl())
                .then()
                .log().all()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .body("id", is(notNullValue()))
                .body("name", is(module.getName()))
                .body("menuItems", hasSize(0))
                .body("users", hasSize(0))
                .body("active", is(module.isActive()));
    }

    @Test
    public void whenAddUserToModule() {
        Module module = new Module();
        module.setName("Gestão de Usuários");
        module.setMenuItems(new ArrayList<>());
        module.setUsers(new ArrayList<>());
        module.setActive(true);
        moduleService.save(module);

        User user = createUser();
        userService.save(user);

        given().contentType(ContentType.JSON)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .patch("/module/{id}/add-user/{userId}", module.getId(), user.getId())
                .then()
                .log().all()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("id", is(notNullValue()))
                .body("name", is(module.getName()))
                .body("menuItems", hasSize(0))
                .body("users", hasSize(1))
                .body("users[0].id", is(notNullValue()))
                .body("users[0].name", is(user.getName()))
                .body("users[0].email", is(user.getEmail()))
                .body("active", is(module.isActive()));
    }

    @TestTransaction
    @Test
    public void whenAddLinkedUserToModule() {
        Module module = new Module();
        module.setName("Gestão de Usuários");
        module.setMenuItems(new ArrayList<>());
        module.setUsers(new ArrayList<>());
        module.setActive(true);
        saveInAnotherTransaction(module);

        User user = createUser();
        saveInAnotherTransaction(user);
        addUserToModuleInAnotherTransaction(module, user);

        given().contentType(ContentType.JSON)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .patch("/module/{id}/add-user/{userId}", module.getId(), user.getId())
                .then()
                .log().all()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body("title", is("Argumento inválido"))
                .body("status", is(Response.Status.BAD_REQUEST.getStatusCode()))
                .body("detail", is("Usuário já vinculado ao módulo"))
                .body("instance", containsString("/module/" + module.getId() + "/add-user/" + user.getId()));
    }

    @Test
    public void whenAddMenuItemToModule() {
        Module module = new Module();
        module.setName("Gestão de Usuários");
        module.setMenuItems(new ArrayList<>());
        module.setUsers(new ArrayList<>());
        module.setActive(true);
        saveInAnotherTransaction(module);

        MenuItem menuItem = new MenuItem();
        menuItem.setLabel("Usuários");
        menuItem.setRoute("/users");
        menuItem.setIcon("fa fa-users");
        menuItem.setPosition(1);
        menuItem.setActive(true);
        saveInAnotherTransaction(menuItem);

        given().contentType(ContentType.JSON)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .patch("/module/{id}/add-menu-item/{menuItemId}", module.getId(), menuItem.getId())
                .then()
                .log().all()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("id", is(notNullValue()))
                .body("name", is(module.getName()))
                .body("menuItems", hasSize(1))
                .body("menuItems[0].id", is(notNullValue()))
                .body("menuItems[0].label", is(menuItem.getLabel()))
                .body("menuItems[0].description", is(menuItem.getDescription()))
                .body("menuItems[0].route", is(menuItem.getRoute()))
                .body("menuItems[0].icon", is(menuItem.getIcon()))
                .body("menuItems[0].position", is(menuItem.getPosition()))
                .body("menuItems[0].active", is(menuItem.isActive()))
                .body("menuItems[0].parent", is(nullValue()))
                .body("active", is(module.isActive()));
    }

    @Test
    public void whenAddLinkedMenuItemToModule() {
        Module module = new Module();
        module.setName("Gestão de Usuários");
        module.setMenuItems(new ArrayList<>());
        module.setUsers(new ArrayList<>());
        module.setActive(true);
        saveInAnotherTransaction(module);

        MenuItem menuItem = new MenuItem();
        menuItem.setLabel("Usuários");
        menuItem.setRoute("/users");
        menuItem.setIcon("fa fa-users");
        menuItem.setPosition(1);
        menuItem.setActive(true);
        saveInAnotherTransaction(menuItem);
        addMenuItemToModuleInAnotherTransaction(module, menuItem);

        given().contentType(ContentType.JSON)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .patch("/module/{id}/add-menu-item/{menuItemId}", module.getId(), menuItem.getId())
                .then()
                .log().all()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body("title", is("Argumento inválido"))
                .body("status", is(Response.Status.BAD_REQUEST.getStatusCode()))
                .body("detail", is("Item de menu já vinculado ao módulo"))
                .body("instance", containsString("/module/" + module.getId() + "/add-menu-item/" + menuItem.getId()));
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    protected void addUserToModuleInAnotherTransaction(Module module, User user) {
        moduleService.addUser(module.getId(), user.getId());
        moduleService.getRepository().getEntityManager().flush();
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    protected void addMenuItemToModuleInAnotherTransaction(Module module, MenuItem menuItem) {
        moduleService.addMenuItem(module.getId(), menuItem.getId());
        moduleService.getRepository().getEntityManager().flush();
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    protected void saveInAnotherTransaction(Module module) {
        moduleService.save(module);
        moduleService.getRepository().getEntityManager().flush();
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    protected void saveInAnotherTransaction(User user) {
        userService.save(user);
        userService.getRepository().getEntityManager().flush();
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    protected void saveInAnotherTransaction(MenuItem menuItem) {
        menuItemService.save(menuItem);
        menuItemService.getRepository().getEntityManager().flush();
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