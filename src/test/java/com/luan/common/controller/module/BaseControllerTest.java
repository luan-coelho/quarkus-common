package com.luan.common.controller.module;


import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.nullValue;

public abstract class BaseControllerTest {

    @Test
    void whenGetAll() {
        given().contentType(ContentType.JSON)
                .when()
                .get(getUrl())
                .then()
                .log().all()
                .statusCode(200)
                .body("$", hasKey("content"))
                .body("content", not(nullValue()))
                .body("$", hasKey("pagination"))
                .body("pagination", not(nullValue()));
    }

    abstract String getUrl();

}
