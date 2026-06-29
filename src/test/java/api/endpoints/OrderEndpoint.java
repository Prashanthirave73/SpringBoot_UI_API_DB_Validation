package api.endpoints;

import api.models.OrderModel;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public final class OrderEndpoint {
    private OrderEndpoint() {
    }

    public static Response create(OrderModel order) {
        return given()
                .contentType("application/json")
                .body(order)
                .when()
                .post("/orders");
    }

    public static Response get(long orderId) {
        return given()
                .when()
                .get("/orders/{orderId}", orderId);
    }
}
