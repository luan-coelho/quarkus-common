
package integration.com.luan.common.controller.module;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.luan.common.dto.module.MenuItemResponseDto;
import com.luan.common.mapper.module.MenuItemMapper;
import com.luan.common.model.module.MenuItem;
import com.luan.common.service.module.MenuItemService;
import com.luan.common.util.JsonUtils;
import integration.com.luan.common.controller.BaseControllerTest;
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
                .post("/menu-item")
                .then()
                .log().all()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .body("id", is(notNullValue()))
                .body("label", is(menuItem.getLabel()))
                .body("description", is(menuItem.getDescription()))
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
                .put("/menu-item/" + menuItem.getId())
                .then()
                .log().all()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("id", equalTo(menuItem.getId().toString()))
                .body("label", is(menuItem.getLabel()))
                .body("description", is(menuItem.getDescription()))
                .body("route", is(menuItem.getRoute()))
                .body("icon", is(menuItem.getIcon()))
                .body("position", is(menuItem.getPosition()))
                .body("active", is(menuItem.isActive()));
    }

    @TestTransaction
    @Test
    public void whenCreateWithSubItem() {
        MenuItem subItem = createMenuItem("Usuários", "/users", "fa fa-users");
        saveMenuItemInOtherTransaction(subItem);

        MenuItem menuItem = createMenuItem("Configurações", "/settings", "fa fa-cogs");
        menuItem.setSubItems(new ArrayList<>());
        menuItem.getSubItems().add(subItem);

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
                .post("/menu-item")
                .then()
                .log().all()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .body("id", is(notNullValue()))
                .body("label", is(menuItem.getLabel()))
                .body("route", is(menuItem.getRoute()))
                .body("icon", is(menuItem.getIcon()))
                .body("position", is(menuItem.getPosition()))
                .body("active", is(menuItem.isActive()))
                .body("subItems", hasSize(1))
                .body("subItems[0].id", is(subItem.getId().toString()))
                .body("subItems[0].label", is(subItem.getLabel()))
                .body("subItems[0].route", is(subItem.getRoute()))
                .body("subItems[0].icon", is(subItem.getIcon()))
                .body("subItems[0].position", is(subItem.getPosition()))
                .body("subItems[0].active", is(subItem.isActive()));
    }

    @TestTransaction
    @Test
    public void whenAddSubItem() {
        MenuItem menuItem = createMenuItem("Configurações", "/settings", "fa fa-cogs");
        menuItem.setSubItems(new ArrayList<>());
        saveMenuItemInOtherTransaction(menuItem);

        MenuItem subItem = createMenuItem("Usuários", "/users", "fa fa-users");
        saveMenuItemInOtherTransaction(subItem);

        given().contentType(ContentType.JSON)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .patch("/menu-item/" + menuItem.getId() + "/add-sub-item/" + subItem.getId())
                .then()
                .log().all()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("id", is(menuItem.getId().toString()))
                .body("label", is(menuItem.getLabel()))
                .body("description", is(menuItem.getDescription()))
                .body("route", is(menuItem.getRoute()))
                .body("icon", is(menuItem.getIcon()))
                .body("position", is(menuItem.getPosition()))
                .body("active", is(menuItem.isActive()))
                .body("subItems", hasSize(1))
                .body("subItems[0].id", is(subItem.getId().toString()))
                .body("subItems[0].label", is(subItem.getLabel()))
                .body("subItems[0].description", is(subItem.getDescription()))
                .body("subItems[0].route", is(subItem.getRoute()))
                .body("subItems[0].icon", is(subItem.getIcon()))
                .body("subItems[0].position", is(subItem.getPosition()))
                .body("subItems[0].active", is(subItem.isActive()));
    }

    @Test
    public void whenDeleteMenuItem() {
        MenuItem menuItem = createMenuItem("Usuários", "/users", "fa fa-users");
        menuItemService.save(menuItem);

        given().contentType(ContentType.JSON)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .delete("/menu-item/" + menuItem.getId())
                .then()
                .log().all()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    public void whenDeleteAssociatedMenuItemThenConflict() {
        MenuItem subItem = createMenuItem("Usuários", "/users", "fa fa-users");
        saveMenuItemInOtherTransaction(subItem);

        MenuItem menuItem = createMenuItem("Configurações", "/settings", "fa fa-cogs");
        menuItem.setSubItems(new ArrayList<>());
        menuItem.getSubItems().add(subItem);

        menuItemService.save(menuItem);

        given().contentType(ContentType.JSON)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .delete("/menu-item/" + subItem.getId())
                .then()
                .log().all()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    private MenuItem createMenuItem(String label, String route, String icon) {
        MenuItem menuItem = new MenuItem();
        menuItem.setLabel(label);
        menuItem.setRoute(route);
        menuItem.setDescription("Descrição do menu");
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
    }

    @Override
    public String getUrl() {
        return "/menu-item";
    }

}