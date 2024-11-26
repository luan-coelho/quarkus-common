
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
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

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
                .body("visible", is(menuItem.isVisible()))
                .body("active", is(menuItem.isActive()));
    }

    @TestTransaction
    @Test
    public void whenCreateWithParent() {
        MenuItem parent = createMenuItem("Configurações", "/settings", "fa fa-cogs");
        saveParent(parent);

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
                .body("visible", is(menuItem.isVisible()))
                .body("active", is(menuItem.isActive()))
                .body("parent.id", is(parent.getId().toString()))
                .body("parent.label", is(parent.getLabel()))
                .body("parent.route", is(parent.getRoute()))
                .body("parent.icon", is(parent.getIcon()))
                .body("parent.position", is(parent.getPosition()))
                .body("parent.visible", is(parent.isVisible()))
                .body("parent.active", is(parent.isActive()));
    }

    private MenuItem createMenuItem(String label, String route, String icon) {
        MenuItem menuItem = new MenuItem();
        menuItem.setLabel(label);
        menuItem.setRoute(route);
        menuItem.setIcon(icon);
        menuItem.setPosition(1);
        menuItem.setVisible(true);
        menuItem.setActive(true);
        return menuItem;
    }

    /**
     * Responsável por salvar o menu pai em uma transação separada, para que seja visível para os testes.
     *
     * @param parent menu pai
     */
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void saveParent(MenuItem parent) {
        menuItemService.save(parent);
        menuItemService.getRepository().flush();
    }

    @Override
    String getUrl() {
        return "/menu-item";
    }

}