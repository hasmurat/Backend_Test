import io.restassured.response.Response;
import models.CreateUserModel;
import models.UpdateUserModel;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import services.GoRestService;
import utilities.CreateRandomInfo;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Execution(ExecutionMode.CONCURRENT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UpdateUserTests_PUT {

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

    @AfterEach
    public void Delete_Created_User() {
        GoRestService.deleteUser(USER_ID)
                .then()
                .statusCode(SC_NO_CONTENT);
    }

    @DisplayName("Scenario 1 : I should be able to update a user with valid information successfully (Positive Scenario)")
    @Test
    @Order(1)
    public void Update_User_Successfully(){
        name = CreateRandomInfo.randomName();
        email = CreateRandomInfo.randomEmail();
        final UpdateUserModel updateUserModel = new UpdateUserModel(name, "male", email, "active");
        Response response = GoRestService.updateUserPUT(updateUserModel,USER_ID);
        response.then()
                .statusCode(SC_OK)
                .body("data.id", notNullValue())
                .body("data.name", equalTo(updateUserModel.getName()))
                .body("data.email",equalTo(updateUserModel.getEmail()))
                .body("data.gender",equalTo(updateUserModel.getGender()))
                .body("data.status",equalTo(updateUserModel.getStatus()));
    }

    @DisplayName("Scenario 2 : I should not be able to update a user without name (Negative Scenario)")
    @Test
    @Order(2)
    public void Update_User_Without_Name(){
        email = CreateRandomInfo.randomEmail();
        final UpdateUserModel updateUserModel = new UpdateUserModel("", "male", email, "active");
        Response response = GoRestService.updateUserPUT(updateUserModel,USER_ID);
        response.then()
                .statusCode(SC_UNPROCESSABLE_ENTITY)
                .body("data[0].field", equalTo("name"))
                .body("data[0].message",equalTo("can't be blank"));
    }

    @DisplayName("Scenario 3 : I should not be able to update a user without email (Negative Scenario)")
    @Test
    @Order(3)
    public void Update_User_Without_Email(){
        name = CreateRandomInfo.randomName();
        final UpdateUserModel updateUserModel = new UpdateUserModel(name, "male", "", "active");
        Response response = GoRestService.updateUserPUT(updateUserModel,USER_ID);
        response.then()
                .statusCode(SC_UNPROCESSABLE_ENTITY)
                .body("data[0].field", equalTo("email"))
                .body("data[0].message",equalTo("can't be blank"));
    }

    @DisplayName("Scenario 4 : I should not be able to update a user without gender (Negative Scenario)")
    @Test
    @Order(4)
    public void Update_User_Without_Gender(){
        name = CreateRandomInfo.randomName();
        email = CreateRandomInfo.randomEmail();
        final UpdateUserModel updateUserModel = new UpdateUserModel(name, "", email, "active");
        Response response = GoRestService.updateUserPUT(updateUserModel,USER_ID);
        response.then()
                .statusCode(SC_UNPROCESSABLE_ENTITY)
                .body("data[0].field", equalTo("gender"))
                .body("data[0].message",equalTo("can't be blank, can be male or female"));
    }

    @DisplayName("Scenario 5 : I should not be able to update a user without status (Negative Scenario)")
    @Test
    @Order(5)
    public void Update_User_Without_Status(){
        name = CreateRandomInfo.randomName();
        email = CreateRandomInfo.randomEmail();
        final UpdateUserModel updateUserModel = new UpdateUserModel(name, "male", email, "");
        Response response = GoRestService.updateUserPUT(updateUserModel,USER_ID);
        response.then()
                .statusCode(SC_UNPROCESSABLE_ENTITY)
                .body("data[0].field", equalTo("status"))
                .body("data[0].message",equalTo("can't be blank"));
    }

    @DisplayName("Scenario 6 : I should not be able to update a new user without any information (Negative Scenario)")
    @Test
    @Order(6)
    public void Update_User_Without_AnyInformation(){
        final UpdateUserModel updateUserModel = new UpdateUserModel("", "", "", "");
        Response response = GoRestService.updateUserPUT(updateUserModel,USER_ID);
        response.then()
                .statusCode(SC_UNPROCESSABLE_ENTITY)
                .body("data[0].field", equalTo("email"))
                .body("data[0].message", equalTo("can't be blank"))
                .body("data[1].field", equalTo("name"))
                .body("data[1].message", equalTo("can't be blank"))
                .body("data[2].field", equalTo("gender"))
                .body("data[2].message", equalTo("can't be blank, can be male or female"))
                .body("data[3].field", equalTo("status"))
                .body("data[3].message", equalTo("can't be blank"));
    }

    @DisplayName("Scenario 7 : I should not be able to update a new user with invalid name (Negative Scenario)")
    @ParameterizedTest //User should not be updated with non-alphabetical characters!
    @Order(7)
    @CsvFileSource(resources = "InvalidNames.csv")
    public void Update_User_With_InvalidNames(String invalidName){
        email = CreateRandomInfo.randomEmail();
        final UpdateUserModel updateUserModel = new UpdateUserModel(invalidName, "male", email, "active");
        Response response = GoRestService.updateUserPUT(updateUserModel,USER_ID);
        assertNotEquals(SC_OK, response.statusCode());
    }

    @DisplayName("Scenario 8 : I should not be able to update a new user with invalid email (Negative Scenario)")
    @ParameterizedTest //User should not be updated with invalid emails!
    @Order(8)
    @CsvSource({
            "mkadana.gmail.com",
            "asdanus@gmail",
            "@gmail.com"
    })
    public void Update_User_With_InvalidEmails(String invalidEmails){
        name = CreateRandomInfo.randomName();
        final UpdateUserModel updateUserModel = new UpdateUserModel(name, "male", invalidEmails, "active");
        Response response = GoRestService.updateUserPUT(updateUserModel,USER_ID);
        assertNotEquals(SC_OK, response.statusCode());
        assertEquals("email", response.path("data[0].field"));
        assertEquals("is invalid", response.path("data[0].message"));
    }

    @DisplayName("Scenario 9 : I should not be able to update a new user with gender (Negative Scenario)")
    @ParameterizedTest
    @Order(9)
    @CsvSource({
            "man",
            "woman",
            "child"
    })
    public void Update_User_With_Gender(String invalidGender){
        name = CreateRandomInfo.randomName();
        email = CreateRandomInfo.randomEmail();
        final UpdateUserModel updateUserModel = new UpdateUserModel(name, invalidGender, email, "active");
        Response response = GoRestService.updateUserPUT(updateUserModel,USER_ID);
        assertNotEquals(SC_OK, response.statusCode());
        assertEquals("gender", response.path("data[0].field"));
        assertEquals("can't be blank, can be male or female", response.path("data[0].message"));
    }

    @DisplayName("Scenario 10 : I should not be able to update a new user with status (Negative Scenario)")
    @ParameterizedTest
    @Order(10)
    @CsvSource({
            "HELLO",
            "WORLD",
            "REST ASSURED"
    })
    public void Update_User_With_Status(String invalidStatus){
        name = CreateRandomInfo.randomName();
        email = CreateRandomInfo.randomEmail();
        final UpdateUserModel updateUserModel = new UpdateUserModel(name, "male", email, invalidStatus);
        Response response = GoRestService.updateUserPUT(updateUserModel,USER_ID);
        assertNotEquals(SC_OK, response.statusCode());
        assertEquals("status", response.path("data[0].field"));
        assertEquals("can't be blank", response.path("data[0].message"));
    }

    @DisplayName("Scenario 11 : I should not be able to update a new user with email already taken (Negative Scenario)")
    @Test
    @Order(11)
    public void Update_User_With_TakenEmail(){
        name = CreateRandomInfo.randomName();
        final UpdateUserModel updateUserModel = new UpdateUserModel(name, "male", "adan@gmail.com", "active");
        Response response = GoRestService.updateUserPUT(updateUserModel,USER_ID);
        response.then()
                .statusCode(SC_UNPROCESSABLE_ENTITY)
                .body("data[0].field", equalTo("email"))
                .body("data[0].message",equalTo("has already been taken"));
    }

    @DisplayName("Scenario 12 : I should not be able to create a new user without authentication (Negative Scenario)")
    @Test//Status code should be 401, but it throws 404 error!
    @Order(12)
    public void Create_User_Without_Authentication(){
        name = CreateRandomInfo.randomName();
        email = CreateRandomInfo.randomEmail();
        final UpdateUserModel updateUserModel = new UpdateUserModel(name, "male", email, "active");
        GoRestService.noAuthUpdateUserPUT(updateUserModel,USER_ID)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("data.message",equalTo("Authentication failed"));

    }

    }
