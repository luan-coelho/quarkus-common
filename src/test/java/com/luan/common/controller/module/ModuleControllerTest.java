package com.luan.common.controller.module;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.luan.common.model.module.MenuItem;
import com.luan.common.model.module.Module;
import com.luan.common.model.user.User;
import com.luan.common.util.JsonUtils;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class ModuleControllerTest {

    @Test
    void whenGetModules() {
        given().contentType(ContentType.JSON)
                .when()
                .get("/module")
                .then()
                .log().all()
                .statusCode(200)
                .body("$", hasKey("content"))
                .body("$", hasKey("pagination"));
    }

    @Test
    void whenCreateModule() {
        MenuItem menuItem = new MenuItem();
        menuItem.setLabel("Users");
        menuItem.setRoute("/users");
        menuItem.setIcon("fa fa-users");
        menuItem.setPosition(1);
        menuItem.setVisible(true);
        menuItem.setActive(true);

        Module module = new Module();
        module.setName("Gestão de Usuários");
        module.setMenuItems(new ArrayList<>());
        module.getMenuItems().add(menuItem);

        User user = new User();
        user.setName("Admin");
        user.setEmail("admin@gmail.com");
        user.setPassword("admin");
        user.setActive(true);
        user.setModules(new ArrayList<>());
        user.getModules().add(module);

        module.setUsers(new ArrayList<>());
        module.getUsers().add(user);

        String json;
        try {
            json = JsonUtils.toJson(module);
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

}