package integration.com.luan.common.util.pagination;

import com.luan.common.model.module.MenuItem;
import com.luan.common.service.module.MenuItemService;
import com.luan.common.util.pagination.Pagination;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.withArgs;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class PanacheFilterUtilsTest {

    @Inject
    MenuItemService menuItemService;

    @TestTransaction
    @AfterEach
    public void cleanup() {
        menuItemService.getRepository().deleteAll();
        menuItemService.getRepository().flush();
    }

    @TestTransaction
    @Test
    public void whenValidFiltersThenReturnMenuItem() {
        MenuItem menuItem = createMenuItem("Usuários", "/users", "fa fa-users");
        saveMenuItemInOtherTransaction(menuItem);

        String filter = "?filters=label:eq:Usuários";

        given().contentType(ContentType.JSON)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .get("/menu-item" + filter)
                .then()
                .log().all()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("$", hasKey("content"))
                .body("content", hasSize(greaterThanOrEqualTo(1)))
                .body("content.find { it.label == '%s' }", withArgs(menuItem.getLabel()), notNullValue())
                .body("content.find { it.label == '%s' }.description", withArgs(menuItem.getLabel()), is(menuItem.getDescription()))
                .body("content.find { it.label == '%s' }.route", withArgs(menuItem.getLabel()), is(menuItem.getRoute()))
                .body("content.find { it.label == '%s' }.icon", withArgs(menuItem.getLabel()), is(menuItem.getIcon()))
                .body("$", hasKey("pagination"))
                .body("pagination", not(nullValue()))
                .body("pagination.currentPage", greaterThan(0))
                .body("pagination.itemsPerPage", greaterThan(0))
                .body("pagination.totalPages", greaterThan(0))
                .body("pagination.totalItems", greaterThan(0));
    }

    @TestTransaction
    @Test
    public void whenInvalidFiltersThenReturnEmpty() {
        String filter = "?filters=label:eq:xxxxxx";

        given().contentType(ContentType.JSON)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .get("/menu-item" + filter)
                .then()
                .log().all()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("$", hasKey("content"))
                .body("content", hasSize(0))
                .body("$", hasKey("pagination"))
                .body("pagination", not(nullValue()))
                .body("pagination.currentPage", equalTo(1))
                .body("pagination.itemsPerPage", equalTo(Pagination.STANDARD_PAGE_SIZE))
                .body("pagination.totalPages", equalTo(0))
                .body("pagination.totalItems", equalTo(0));
    }

    @TestTransaction
    @Test
    public void whenValidFiltersAndSortThenReturnMenuItem() {
        MenuItem menuItem = createMenuItem("Usuários", "/users", "fa fa-users");
        saveMenuItemInOtherTransaction(menuItem);

        String filter = "?filters=label:eq:Usuários&sort=label:asc";

        given().contentType(ContentType.JSON)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .get("/menu-item" + filter)
                .then()
                .log().all()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("$", hasKey("content"))
                .body("content", hasSize(greaterThanOrEqualTo(1)))
                .body("content.find { it.label == '%s' }", withArgs(menuItem.getLabel()), notNullValue())
                .body("content.find { it.label == '%s' }.description", withArgs(menuItem.getLabel()), is(menuItem.getDescription()))
                .body("content.find { it.label == '%s' }.route", withArgs(menuItem.getLabel()), is(menuItem.getRoute()))
                .body("content.find { it.label == '%s' }.icon", withArgs(menuItem.getLabel()), is(menuItem.getIcon()))
                .body("$", hasKey("pagination"))
                .body("pagination", not(nullValue()))
                .body("pagination.currentPage", greaterThan(0))
                .body("pagination.itemsPerPage", greaterThan(0))
                .body("pagination.totalPages", greaterThan(0))
                .body("pagination.totalItems", greaterThan(0));
    }

    // Quando tiver dois menu item e fazer um filtro por label deve retornar apenas um
    @TestTransaction
    @Test
    public void whenValidFiltersAndSortThenReturnOneMenuItem() {
        MenuItem menuItem = createMenuItem("Usuários", "/users", "fa fa-users");
        saveMenuItemInOtherTransaction(menuItem);

        MenuItem menuItem2 = createMenuItem("Configurações", "/settings", "fa fa-cogs");
        saveMenuItemInOtherTransaction(menuItem2);

        String filter = "?filters=label:eq:Usuários";

        given().contentType(ContentType.JSON)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .get("/menu-item" + filter)
                .then()
                .log().all()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("$", hasKey("content"))
                .body("content", hasSize(1))
                .body("content.find { it.label == '%s' }", withArgs(menuItem.getLabel()), notNullValue())
                .body("content.find { it.label == '%s' }.description", withArgs(menuItem.getLabel()), is(menuItem.getDescription()))
                .body("content.find { it.label == '%s' }.route", withArgs(menuItem.getLabel()), is(menuItem.getRoute()))
                .body("content.find { it.label == '%s' }.icon", withArgs(menuItem.getLabel()), is(menuItem.getIcon()))
                .body("$", hasKey("pagination"))
                .body("pagination", not(nullValue()))
                .body("pagination.currentPage", greaterThan(0))
                .body("pagination.itemsPerPage", greaterThan(0))
                .body("pagination.totalPages", greaterThan(0))
                .body("pagination.totalItems", greaterThan(0));
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void saveMenuItemInOtherTransaction(MenuItem menuItem) {
        menuItemService.save(menuItem);
        menuItemService.getRepository().flush();
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

}