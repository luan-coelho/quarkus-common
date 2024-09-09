package com.luan.common.handle;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@QuarkusTest
class HandleExceptionMapperTest {

    @Test
    void shouldReturnConstraintViolationException() {
        Car car = new Car();
        car.setName(null);
        given().
                body(car)
                .when()
                .post("/cars")
                .then()
                .statusCode(400)
                .body("type", equalTo("about:blank"))
                .body("title", equalTo(HttpResponseStatus.BAD_REQUEST.reasonPhrase()))
                .body("status", equalTo(HttpResponseStatus.BAD_REQUEST.code()))
                .body("detail", equalTo("Validation failed"))
                .body("errors", equalTo("[]"))
                .body("instance", equalTo(""));
    }

}