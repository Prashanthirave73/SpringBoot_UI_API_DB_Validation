package api.endpoints;

import api.models.UserModel;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public final class UserEndpoint {
    private UserEndpoint() {
    }

    public static Response create(UserModel user) {
        return given()
                .contentType("application/json")
                .body(user)
                .when()
                .post("/users");
    }

    public static Response get(long userId) {
        return given()
                .when()
                .get("/users/{id}", userId);
    }

    public static Response update(long userId, UserModel user) {
        return given()
                .contentType("application/json")
                .body(user)
                .when()
                .put("/users/{id}", userId);
    }

    public static Response patch(long userId, Map<String, Object> fields) {
        return given()
                .contentType("application/json")
                .body(fields)
                .when()
                .patch("/users/{id}", userId);
    }

    public static Response delete(long userId) {
        return given()
                .when()
                .delete("/users/{id}", userId);
    }
}
