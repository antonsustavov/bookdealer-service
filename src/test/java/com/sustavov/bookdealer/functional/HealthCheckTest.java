package com.sustavov.bookdealer.functional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

public class HealthCheckTest extends BaseFunctionalTest {

    @Test
    void shouldCheckHealth() {
        given().port(managementPort)
                .when().get("/health")
                .then()
                .log()
                .all()
                .statusCode(SC_OK)
                .body("status", Matchers.is("UP"));
    }
}
