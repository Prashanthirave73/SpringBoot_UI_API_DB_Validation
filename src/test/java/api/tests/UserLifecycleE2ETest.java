package api.tests;

import api.endpoints.OrderEndpoint;
import api.endpoints.UserEndpoint;
import api.models.OrderItemModel;
import api.models.OrderModel;
import api.models.UserModel;
import api.utilities.TestDataProvider;
import database.repositories.OrderDbRecord;
import database.repositories.OrderDbRepository;
import database.repositories.UserDbRepository;
import framework.BaseTest;
import framework.ConfigManager;
import io.restassured.response.Response;

import org.checkerframework.checker.guieffect.qual.UI;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.mysql.cj.x.protobuf.MysqlxCrud.Delete;

import ui.pages.HomePage;
import ui.pages.LoginPage;
import ui.pages.OrdersPage;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class UserLifecycleE2ETest extends BaseTest {
        private final UserDbRepository userDbRepository = new UserDbRepository();
        private final OrderDbRepository orderDbRepository = new OrderDbRepository();

        @Test(dataProvider = "userLifecycleData", dataProviderClass = TestDataProvider.class)
        public void validateUserLifecycleAcrossApiUiAndDatabase(UserModel newUser) {

                // ============================================================
                // STEP 1 : Create User via API
                // ============================================================
                // Send POST /users request with test user data
                log.info("Creating user through API: {}", newUser.getEmail());

                Response createResponse = UserEndpoint.create(newUser);

                // Validate API response status code and schema
                createResponse.then()
                                .statusCode(201)
                                .body(matchesJsonSchemaInClasspath("schemas/user-schema.json"));

                // Deserialize response into UserModel object
                UserModel createdUser = createResponse.as(UserModel.class);

                // Verify user creation response data
                Assert.assertNotNull(createdUser.getId(),
                                "Created user id should be present");
                Assert.assertEquals(createdUser.getEmail(),
                                newUser.getEmail());

                // ============================================================
                // STEP 2 : Validate User Record in Database
                // ============================================================
                // Fetch inserted user directly from database
                UserModel dbUser = userDbRepository.findByEmail(newUser.getEmail())
                                .orElseThrow(() -> new AssertionError("User was not inserted into database"));

                // Verify DB values match request payload
                Assert.assertEquals(dbUser.getName(), newUser.getName());
                Assert.assertEquals(dbUser.getEmail(), newUser.getEmail());
                Assert.assertEquals(dbUser.getMobile(), newUser.getMobile());

                // ============================================================
                // STEP 3 : Login Through UI Using Created User
                // ============================================================
                // Open application login page
                LoginPage loginPage = new LoginPage(getDriver()).open(ConfigManager.get("base.url"));

                // Perform UI login
                HomePage homePage = loginPage.login(newUser.getEmail(), newUser.getPassword());

                // Validate profile information displayed in UI
                Assert.assertEquals(homePage.getDisplayedName(), newUser.getName());

                Assert.assertEquals(homePage.getDisplayedEmail(), newUser.getEmail());

                // ============================================================
                // STEP 4 : Fetch User via GET API
                // ============================================================
                // Call GET /users/{id}
                Response getResponse = UserEndpoint.get(createdUser.getId());

                // Validate response code
                getResponse.then().statusCode(200);

                // Convert response to model object
                UserModel apiUser = getResponse.as(UserModel.class);

                // Cross validate API response with DB values
                Assert.assertEquals(apiUser.getName(), dbUser.getName());
                Assert.assertEquals(apiUser.getEmail(), dbUser.getEmail());
                Assert.assertEquals(apiUser.getMobile(), dbUser.getMobile());

                // ============================================================
                // STEP 5 : Partial Update User via PATCH API
                // ============================================================
                // Update only address field using PATCH request
                Response patchResponse = UserEndpoint.patch(createdUser.getId(),
                                Map.of("address", "44 Patch Street, Chennai"));

                patchResponse.then().statusCode(200)
                                .body(matchesJsonSchemaInClasspath("schemas/user-schema.json"));

                // Validate updated address in database
                UserModel patchedDbUser = userDbRepository.findByEmail(newUser.getEmail())
                                .orElseThrow(() -> new AssertionError("Patched user not found in database"));

                Assert.assertEquals(patchedDbUser.getAddress(), "44 Patch Street, Chennai");

                // ============================================================
                // STEP 6 : Full Update User via PUT API
                // ============================================================
                // Prepare updated user payload
                UserModel updatedUser = new UserModel(
                                "Ava Sharma Updated",
                                newUser.getEmail(),
                                newUser.getPassword(),
                                "9000001999",
                                "88 Automation Avenue, Bengaluru");

                // Send PUT request
                Response updateResponse = UserEndpoint.update(createdUser.getId(), updatedUser);

                // Validate response
                updateResponse.then().statusCode(200)
                                .body(matchesJsonSchemaInClasspath("schemas/user-schema.json"));

                UserModel updatedApiUser = updateResponse.as(UserModel.class);

                // Verify API response values
                Assert.assertEquals(updatedApiUser.getName(), updatedUser.getName());

                Assert.assertEquals(updatedApiUser.getMobile(), updatedUser.getMobile());

                Assert.assertEquals(updatedApiUser.getAddress(), updatedUser.getAddress());

                // ============================================================
                // STEP 7 : Validate Updated Data in Database
                // ============================================================
                UserModel updatedDbUser = userDbRepository.findByEmail(newUser.getEmail())
                                .orElseThrow(() -> new AssertionError("Updated user not found in database"));

                Assert.assertEquals(updatedDbUser.getName(), updatedUser.getName());

                Assert.assertEquals(updatedDbUser.getMobile(), updatedUser.getMobile());

                Assert.assertEquals(updatedDbUser.getAddress(), updatedUser.getAddress());

                // ============================================================
                // STEP 8 : Validate Updated Data in UI Profile Page
                // ============================================================
                // Refresh profile page to load latest values
                homePage.refreshProfile();

                Assert.assertEquals(homePage.getDisplayedName(), updatedUser.getName());

                Assert.assertEquals(homePage.getDisplayedMobile(), updatedUser.getMobile());

                Assert.assertEquals(homePage.getDisplayedAddress(), updatedUser.getAddress());

                // ============================================================
                // STEP 9 : Create Order Through API
                // ============================================================
                OrderModel orderRequest = buildOrder(createdUser.getId());

                // Send POST /orders request
                Response orderResponse = OrderEndpoint.create(orderRequest);

                orderResponse.then().statusCode(201);

                // Deserialize order response
                OrderModel createdOrder = orderResponse.as(OrderModel.class);

                // Validate order creation response
                Assert.assertNotNull(createdOrder.getOrderId(), "Created order id should be present");

                Assert.assertEquals(createdOrder.getItems().size(), orderRequest.getItems().size());

                Assert.assertEquals(createdOrder.getTotalAmount().compareTo(orderRequest.getTotalAmount()), 0);

                // ============================================================
                // STEP 10 : Validate Order Data in Database
                // ============================================================
                OrderDbRecord dbOrder = orderDbRepository.findByOrderId(createdOrder.getOrderId())
                                .orElseThrow(() -> new AssertionError("Order was not inserted into database"));

                Assert.assertEquals(dbOrder.getOrderId(), createdOrder.getOrderId().longValue());

                Assert.assertEquals(dbOrder.getProductCount(), orderRequest.getItems().size());

                Assert.assertEquals(dbOrder.getTotalAmount().compareTo(orderRequest.getTotalAmount()), 0);

                // ============================================================
                // STEP 11 : Validate Order Information in UI
                // ============================================================
                // Navigate to Orders page
                OrdersPage ordersPage = homePage.openOrders();

                // Validate order details displayed in UI
                Assert.assertEquals(ordersPage.getFirstOrderId(), String.valueOf(createdOrder.getOrderId()));
                Assert.assertTrue(ordersPage.getFirstOrderProducts().contains("Keyboard x1"));
                Assert.assertTrue(ordersPage.getFirstOrderProducts().contains("Mouse x2"));
                Assert.assertEquals(ordersPage.getFirstOrderTotal(), "79.98");

                // ============================================================
                // STEP 12 : Delete User via API
                // ============================================================
                Response deleteResponse = UserEndpoint.delete(createdUser.getId());
                deleteResponse.then().statusCode(200);

                // ============================================================
                // STEP 13 : Validate User Removal from Database
                // ============================================================
                Assert.assertTrue(userDbRepository.findByEmail(newUser.getEmail()).isEmpty(),
                                "User should be deleted from database");

                // ============================================================
                // STEP 14 : Negative Login Validation
                // ============================================================
                // Attempt login with deleted user credentials
                String failureMessage = new LoginPage(getDriver())
                                .open(ConfigManager.get("base.url"))
                                .loginExpectingFailure(newUser.getEmail(), newUser.getPassword());

                // Verify login is blocked
                Assert.assertEquals(failureMessage, "Login failed");
        }

        // Builds the order request used to validate API, database, and UI order state.
        private OrderModel buildOrder(long userId) {
                OrderModel order = new OrderModel();
                order.setUserId(userId);
                order.setTotalAmount(new BigDecimal("79.98"));
                order.setItems(List.of(
                                new OrderItemModel("Keyboard", 1, new BigDecimal("49.99")),
                                new OrderItemModel("Mouse", 2, new BigDecimal("14.995"))));
                return order;
        }
}
