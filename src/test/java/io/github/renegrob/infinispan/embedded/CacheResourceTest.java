package io.github.renegrob.infinispan.embedded;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;

@QuarkusTest
public class CacheResourceTest {

    // @Test
    // public void testCacheEndpoint() {
    //     given()
    //       .when().get("/cache/testKey")
    //       .then()
    //          .statusCode(200)
    //          .body(startsWith("{\"createdAt\":\""));
    // }

}