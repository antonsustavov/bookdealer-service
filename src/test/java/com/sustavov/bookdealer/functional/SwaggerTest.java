package com.sustavov.bookdealer.functional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

public class SwaggerTest extends BaseFunctionalTest {

    @Test
    void shouldExposeSwaggerUi() {
        when().get("/swagger-ui.html")
                .then()
                .statusCode(SC_OK);
    }

    @Test
    void shouldExposeSwaggerApi() {
        when().get("/v3/api-docs")
                .then()
                .statusCode(SC_OK)
                .body("info.title", Matchers.is("Book Dealer Service"));
    }
}
