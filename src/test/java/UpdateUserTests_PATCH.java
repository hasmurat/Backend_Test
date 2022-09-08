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
public class UpdateUserTests_PATCH {

    static int USER_ID;
    UpdateUserModel updateUserModel = new UpdateUserModel();

    @BeforeEach
    public void createUser(){
        String name = CreateRandomInfo.randomName();
        String email = CreateRandomInfo.randomEmail();
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

    @DisplayName("Scenario 1 : I should be able to update a user with valid name successfully (Positive Scenario)")
    @Test
    @Order(1)
    public void Update_User_With_Name_Successfully(){
        String name = CreateRandomInfo.randomName();
        Response response = GoRestService.updateUserPATCH("name",name,USER_ID);
        response.then()
                .statusCode(SC_OK)
                .body("data.name", equalTo(updateUserModel.getName()));
    }

    @DisplayName("Scenario 2 : I should be able to update a user with valid email successfully (Positive Scenario)")
    @Test
    @Order(2)
    public void Update_User_With_Email_Successfully(){
        String email = CreateRandomInfo.randomEmail();;
        Response response = GoRestService.updateUserPATCH("email",email,USER_ID);
        response.then()
                .statusCode(SC_OK)
                .body("data.email", equalTo(updateUserModel.getEmail()));
    }

    @DisplayName("Scenario 3 : I should be able to update a user with valid gender successfully (Positive Scenario)")
    @Test
    @Order(3)
    public void Update_User_With_Gender_Successfully(){
        Response response = GoRestService.updateUserPATCH("gender","female",USER_ID);
        response.then()
                .statusCode(SC_OK)
                .body("data.gender", equalTo(updateUserModel.getGender()));
    }

    @DisplayName("Scenario 4 : I should be able to update a user with valid status successfully (Positive Scenario)")
    @Test
    @Order(4)
    public void Update_User_With_Status_Successfully(){
        Response response = GoRestService.updateUserPATCH("status","inactive",USER_ID);
        response.then()
                .statusCode(SC_OK)
                .body("data.status", equalTo(updateUserModel.getStatus()));

    }

    @DisplayName("Scenario 5 : I should be able to update a user without name (Negative Scenario)")
    @Test
    @Order(5)
    public void Update_User_Without_Name(){
        Response response = GoRestService.updateUserPATCH("name","",USER_ID);
        response.then()
                .statusCode(SC_UNPROCESSABLE_ENTITY)
                .body("data[0].field", equalTo("name"))
                .body("data[0].message",equalTo("can't be blank"));
    }

    @DisplayName("Scenario 6 : I should be able to update a user without email (Negative Scenario)")
    @Test
    @Order(6)
    public void Update_User_Without_Email(){
        Response response = GoRestService.updateUserPATCH("email","",USER_ID);
        response.then()
                .statusCode(SC_UNPROCESSABLE_ENTITY)
                .body("data[0].field", equalTo("email"))
                .body("data[0].message",equalTo("can't be blank"));
    }

    @DisplayName("Scenario 7 : I should be able to update a user without gender (Negative Scenario)")
    @Test
    @Order(7)
    public void Update_User_Without_Gender(){
        Response response = GoRestService.updateUserPATCH("gender","",USER_ID);
        response.then()
                .statusCode(SC_UNPROCESSABLE_ENTITY)
                .body("data[0].field", equalTo("gender"))
                .body("data[0].message",equalTo("can't be blank, can be male or female"));
    }

    @DisplayName("Scenario 8 : I should be able to update a user without status (Negative Scenario)")
    @Test
    @Order(8)
    public void Update_User_Without_Status(){
        Response response = GoRestService.updateUserPATCH("status","",USER_ID);
        response.then()
                .statusCode(SC_UNPROCESSABLE_ENTITY)
                .body("data[0].field", equalTo("status"))
                .body("data[0].message",equalTo("can't be blank"));
    }

    @DisplayName("Scenario 9 : I should not be able to update a new user with invalid name (Negative Scenario)")
    @ParameterizedTest //User should not be updated with non-alphabetical characters!
    @Order(9)
    @CsvFileSource(resources = "InvalidNames.csv")
    public void Update_User_With_InvalidNames(String invalidName){
        Response response = GoRestService.updateUserPATCH("name",invalidName,USER_ID);
        assertNotEquals(SC_OK, response.statusCode());
    }

    @DisplayName("Scenario 10 : I should not be able to update a new user with invalid email (Negative Scenario)")
    @ParameterizedTest //User should not be updated with invalid emails!
    @Order(10)
    @CsvSource({
            "mkadana.gmail.com",
            "asdanus@gmail",
            "@gmail.com"
    })
    public void Update_User_With_InvalidEmails(String invalidEmails){
        Response response = GoRestService.updateUserPATCH("email",invalidEmails,USER_ID);
        assertNotEquals(SC_OK, response.statusCode());
        assertEquals("email", response.path("data[0].field"));
        assertEquals("is invalid", response.path("data[0].message"));
    }

    @DisplayName("Scenario 11 : I should not be able to update a new user with gender (Negative Scenario)")
    @ParameterizedTest
    @Order(11)
    @CsvSource({
            "man",
            "woman",
            "child"
    })
    public void Update_User_With_Gender(String invalidGender){
        Response response = GoRestService.updateUserPATCH("gender",invalidGender,USER_ID);
        assertNotEquals(SC_OK, response.statusCode());
        assertEquals("gender", response.path("data[0].field"));
        assertEquals("can't be blank, can be male or female", response.path("data[0].message"));
    }

    @DisplayName("Scenario 12 : I should not be able to update a new user with status (Negative Scenario)")
    @ParameterizedTest
    @Order(12)
    @CsvSource({
            "HELLO",
            "WORLD",
            "REST ASSURED"
    })
    public void Update_User_With_Status(String invalidStatus){
        Response response = GoRestService.updateUserPATCH("status",invalidStatus,USER_ID);
        assertNotEquals(SC_OK, response.statusCode());
        assertEquals("status", response.path("data[0].field"));
        assertEquals("can't be blank", response.path("data[0].message"));
    }

    @DisplayName("Scenario 13 : I should not be able to update a new user with email already taken (Negative Scenario)")
    @Test
    @Order(13)
    public void Update_User_With_TakenEmail(){
        Response response = GoRestService.updateUserPATCH("email","adan@gmail.com",USER_ID);
        response.then()
                .statusCode(SC_UNPROCESSABLE_ENTITY)
                .body("data[0].field", equalTo("email"))
                .body("data[0].message",equalTo("has already been taken"));
    }

    @DisplayName("Scenario 14 : I should not be able to create a new user without authentication (Negative Scenario)")
    @Test //Status code should be 401, but it throws 404 error!
    @Order(14)
    public void Create_User_Without_Authentication(){
        String name = CreateRandomInfo.randomName();
        GoRestService.noAuthUpdateUserPatch("name", name, USER_ID)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("data.message",equalTo("Authentication failed"));
    }

}