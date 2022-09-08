import io.restassured.response.Response;
import models.CreateUserModel;
import models.GetUserModel;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import services.GoRestService;
import utilities.CreateRandomInfo;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;

@Execution(ExecutionMode.CONCURRENT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetUserTests {

    static GetUserModel getUserModel;

    @BeforeEach
    public void createUser() {
        String name = CreateRandomInfo.randomName();
        String email = CreateRandomInfo.randomEmail();
        final CreateUserModel createUserModel = new CreateUserModel(name, "male", email, "active");
        Response response = GoRestService.createUser(createUserModel);
        response.then()
                .statusCode(SC_CREATED)
                .body("data.id", notNullValue())
                .body("data.name", equalTo(createUserModel.getName()))
                .body("data.email", equalTo(createUserModel.getEmail()))
                .body("data.gender", equalTo(createUserModel.getGender()))
                .body("data.status", equalTo(createUserModel.getStatus()));

        getUserModel = new GetUserModel(response);;
    }

    @AfterEach
    public void Delete_Created_User() {
        GoRestService.deleteUser(getUserModel.getId())
                .then()
                .statusCode(SC_NO_CONTENT);
    }

    @DisplayName("Scenario 1 : I should be able to get all users data successfully (Positive Scenario)")
    @Test
    @Order(1)
    public void Get_All_Users_Data_Successfully() {
        GoRestService.getAllUsers().then()
                .statusCode(SC_OK);
    }

    @DisplayName("Scenario 2 : I should be able to get a user data by ID successfully (Positive Scenario)")
    @Test
    @Order(2)
    public void Get_User_Data_With_ID_Successfully() {
        Response response = GoRestService.getUserWithParameter("id", String.valueOf(getUserModel.getId()));
        response.then()
                .statusCode(SC_OK)
                .body("data[0].name", equalTo(getUserModel.getName()))
                .body("data[0].email", equalTo(getUserModel.getEmail()))
                .body("data[0].gender", equalTo(getUserModel.getGender()))
                .body("data[0].status", equalTo(getUserModel.getStatus()));
    }

    @DisplayName("Scenario 3 : I should be able to get a user data by name successfully (Positive Scenario)")
    @Test
    @Order(3)
    public void Get_User_Data_With_Name_Successfully() {
        String name = (getUserModel.getName().split(" "))[0];
        Response response = GoRestService.getUserWithParameter("name", name);
        response.then()
                .statusCode(SC_OK)
                .body("data[0].id", equalTo(getUserModel.getId()))
                .body("data[0].email", equalTo(getUserModel.getEmail()))
                .body("data[0].gender", equalTo(getUserModel.getGender()))
                .body("data[0].status", equalTo(getUserModel.getStatus()));
    }

    @DisplayName("Scenario 4 : I should be able to get a user data by email successfully (Positive Scenario)")
    @Test
    @Order(4)
    public void Get_User_Data_With_Email_Successfully() {
        Response response = GoRestService.getUserWithParameter("email", getUserModel.getEmail());
        response.then()
                .statusCode(SC_OK)
                .body("data[0].id", equalTo(getUserModel.getId()))
                .body("data[0].name", equalTo(getUserModel.getName()))
                .body("data[0].gender", equalTo(getUserModel.getGender()))
                .body("data[0].status", equalTo(getUserModel.getStatus()));
    }

    @DisplayName("Scenario 5 : I should not be able to get a user data by non-existing ID (Negative Scenario)")
    @Test
    @Order(5)
    public void Get_User_Data_With_Non_Existing_ID() {
        Response response = GoRestService.getUserWithParameter("id", String.valueOf("555555555555555555"));
        response.then()
                .statusCode(SC_OK)
                .body("data.size()",is(0));
    }

    @DisplayName("Scenario 6 : I should not be able to get a user data by non-existing name (Negative Scenario)")
    @Test
    @Order(6)
    public void Get_User_Data_With_Non_Existing_Name() {
        Response response = GoRestService.getUserWithParameter("name", "AAAAAAAAAAAAAAAAAAAAA");
        response.then()
                .statusCode(SC_OK)
                .body("data.size()",is(0));
    }

    @DisplayName("Scenario 7 : I should not be able to get a user data by non-existing email (Negative Scenario)")
    @Test
    @Order(7)
    public void Get_User_Data_With_Non_Existing_Email() {
        Response response = GoRestService.getUserWithParameter("email", "AAAAAAAAAAAAAAAAAAAAA@gmail.com");
        response.then()
                .statusCode(SC_OK)
                .body("data.size()",is(0));
    }
}
