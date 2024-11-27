
package com.luan.common.controller.module;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.luan.common.dto.module.MenuItemResponseDto;
import com.luan.common.mapper.module.MenuItemMapper;
import com.luan.common.model.module.MenuItem;
import com.luan.common.service.module.MenuItemService;
import com.luan.common.util.JsonUtils;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Test for {@link com.luan.common.controller.module.MenuItemController}.
 */
@QuarkusTest
class MenuItemControllerTest extends BaseControllerTest {

    @Inject
    MenuItemMapper mapper;

    @Inject
    MenuItemService menuItemService;

    @Test
    void whenCreateMenuItem() {
        MenuItem menuItem = createMenuItem("Usuários", "/users", "fa fa-users");

        String json;
        try {
            MenuItemResponseDto dto = mapper.toDto(menuItem);
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
                .body("label", is(menuItem.getLabel()))
                .body("route", is(menuItem.getRoute()))
                .body("icon", is(menuItem.getIcon()))
                .body("position", is(menuItem.getPosition()))
                .body("active", is(menuItem.isActive()));
    }

    @Test
    public void whenUpdateMenuItem() {
        MenuItem menuItem = createMenuItem("Usuários", "/users", "fa fa-users");
        menuItemService.save(menuItem);

        menuItem.setLabel("Funcionários");
        menuItem.setRoute("/employees");
        menuItem.setIcon("fa fa-users");
        menuItem.setPosition(1);
        menuItem.setActive(true);

        String json;
        try {
            MenuItemResponseDto dto = mapper.toDto(menuItem);
            json = JsonUtils.toJson(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        given().contentType(ContentType.JSON)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(json)
                .when()
                .put(getUrl() + "/" + menuItem.getId())
                .then()
                .log().all()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("id", equalTo(menuItem.getId().toString()))
                .body("label", is(menuItem.getLabel()))
                .body("route", is(menuItem.getRoute()))
                .body("icon", is(menuItem.getIcon()))
                .body("position", is(menuItem.getPosition()))
                .body("active", is(menuItem.isActive()));
    }

    @TestTransaction
    @Test
    public void whenCreateWithParent() {
        MenuItem parent = createMenuItem("Configurações", "/settings", "fa fa-cogs");
        saveMenuItemInOtherTransaction(parent);

        MenuItem menuItem = createMenuItem("Usuários", "/users", "fa fa-users");
        menuItem.setParent(parent);

        String json;
        try {
            MenuItemResponseDto dto = mapper.toDto(menuItem);
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
                .body("label", is(menuItem.getLabel()))
                .body("route", is(menuItem.getRoute()))
                .body("icon", is(menuItem.getIcon()))
                .body("position", is(menuItem.getPosition()))
                .body("active", is(menuItem.isActive()))
                .body("parent.id", equalTo(parent.getId().toString()))
                .body("parent.label", is(parent.getLabel()))
                .body("parent.route", is(parent.getRoute()))
                .body("parent.icon", is(parent.getIcon()))
                .body("parent.position", is(parent.getPosition()))
                .body("parent.active", is(parent.isActive()));
    }

    @TestTransaction
    @Test
    public void whenUpdateParent() {
        MenuItem parent = createMenuItem("Configurações", "/settings", "fa fa-cogs");
        saveMenuItemInOtherTransaction(parent);

        MenuItem menuItem = createMenuItem("Usuários", "/users", "fa fa-users");
        menuItem.setParent(parent);
        saveMenuItemInOtherTransaction(menuItem);

        MenuItem newParent = createMenuItem("Gestão", "/management", "fa fa-cogs");
        saveMenuItemInOtherTransaction(newParent);
        menuItem.setParent(newParent);

        String json;
        try {
            MenuItemResponseDto dto = mapper.toDto(menuItem);
            json = JsonUtils.toJson(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        given().contentType(ContentType.JSON)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body(json)
                .when()
                .put(getUrl() + "/" + menuItem.getId())
                .then()
                .log().all()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("id", is(menuItem.getId().toString()))
                .body("label", is(menuItem.getLabel()))
                .body("route", is(menuItem.getRoute()))
                .body("icon", is(menuItem.getIcon()))
                .body("position", is(menuItem.getPosition()))
                .body("active", is(menuItem.isActive()))
                .body("parent.id", equalTo(newParent.getId().toString()))
                .body("parent.label", is(newParent.getLabel()))
                .body("parent.route", is(newParent.getRoute()))
                .body("parent.icon", is(newParent.getIcon()))
                .body("parent.position", is(newParent.getPosition()))
                .body("parent.active", is(newParent.isActive()));
    }

    @Test
    public void whenDeleteMenuItem() {
        MenuItem menuItem = createMenuItem("Usuários", "/users", "fa fa-users");
        menuItemService.save(menuItem);

        given().contentType(ContentType.JSON)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .delete(getUrl() + "/" + menuItem.getId())
                .then()
                .log().all()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    public void whenDeleteAssociatedMenuItemThenConflict() {
        MenuItem parent = createMenuItem("Configurações", "/settings", "fa fa-cogs");
        menuItemService.save(parent);

        MenuItem menuItem = createMenuItem("Usuários", "/users", "fa fa-users");
        menuItem.setParent(parent);
        menuItemService.save(menuItem);

        given().contentType(ContentType.JSON)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .delete(getUrl() + "/" + parent.getId())
                .then()
                .log().all()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    private MenuItem createMenuItem(String label, String route, String icon) {
        MenuItem menuItem = new MenuItem();
        menuItem.setLabel(label);
        menuItem.setRoute(route);
        menuItem.setIcon(icon);
        menuItem.setPosition(1);
        menuItem.setActive(true);
        return menuItem;
    }

    /**
     * Responsável por salvar um item de menu em uma transação separada, para que seja visível para os testes.
     *
     * @param menuItem menu pai
     */
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void saveMenuItemInOtherTransaction(MenuItem menuItem) {
        menuItemService.save(menuItem);
        menuItemService.getRepository().flush();
//        menuItemService.getRepository().getEntityManager().clear();
    }

    @Override
    String getUrl() {
        return "/menu-item";
    }

}