import io.restassured.response.Response;
import models.CreateUserModel;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import services.GoRestService;
import utilities.CreateRandomInfo;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@Execution(ExecutionMode.CONCURRENT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DeleteUsersTests {
    static int USER_ID;
    String name;
    String email;

    @BeforeEach
    public void createUser(){
        name = CreateRandomInfo.randomName();
        email = CreateRandomInfo.randomEmail();
        final CreateUserModel createUserModel = new CreateUserModel(name, "male", email, "active");
        Response response = GoRestService.createUser(createUserModel);
        response.then()
                .statusCode(SC_CREATED)
                .body("data.id", notNullValue())
                .body("data.name", equalTo(createUserModel.getName()))
                .body("data.email",equalTo(createUserModel.getEmail()))
                .body("data.gender",equalTo(createUserModel.getGender()))
                .body("data.status",equalTo(createUserModel.getStatus()));

        USER_ID = response.body().path("data.id");
    }

    @DisplayName("Scenario 1 : I should be able to delete a user by ID successfully (Positive Scenario)")
    @Test
    @Order(1)
    public void Delete_Created_User() {
        GoRestService.deleteUser(USER_ID)
                .then()
                .statusCode(SC_NO_CONTENT);
    }

    @DisplayName("Scenario 2 : I should not be able to delete a user by non-existing ID (Negative Scenario)")
    @Test
    @Order(2)
    public void Delete_Created_User_With_Non_Existing_ID() {
        GoRestService.deleteUser(55555555)
                .then()
                .statusCode(SC_NOT_FOUND)
                .body("data.message", Matchers.equalTo("Resource not found"));
    }

    @DisplayName("Scenario 3 : I should not be able to delete a user without authentication (Negative Scenario)")
    @Test //Status code should be 401, but it throws 404 error!
    @Order(2)
    public void Delete_Created_User_Without_Authentication() {
        GoRestService.noAuthDeleteUser(USER_ID)
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }
}
