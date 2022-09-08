package services;

import io.restassured.response.Response;
import models.CreateUserModel;
import models.UpdateUserModel;


public class GoRestService extends BaseService {


    public static Response createUser(final CreateUserModel createUserModel) {
        return defaultRequestSpecification()
                .body(createUserModel)
                .when()
                .post("/public/v1/users");
    }

    public static Response noAuthCreateUser(final CreateUserModel createUserModel) {
        return noAuthentication()
                .body(createUserModel)
                .when()
                .post("/public/v1/users");
    }

    public static Response updateUserPUT(final UpdateUserModel updateUserModelPUT, int id) {
        return defaultRequestSpecification()
                .body(updateUserModelPUT)
                .when()
                .put("/public/v1/users/" + id);
    }

    public static Response noAuthUpdateUserPUT(final UpdateUserModel updateUserModelPUT, int id) {
        return noAuthentication()
                .body(updateUserModelPUT)
                .when()
                .put("/public/v1/users/" + id);
    }

    public static Response updateUserPATCH(String key, String value, int id) {
        return defaultRequestSpecification()
                .body(UpdateUserModel.updatePatch(key, value))
                .when()
                .patch("/public/v1/users/" + id);
    }

    public static Response noAuthUpdateUserPatch(String key, String value, int id) {
        return noAuthentication()
                .body(UpdateUserModel.updatePatch(key, value))
                .when()
                .patch("/public/v1/users/" + id);
    }

    public static Response getAllUsers(){
        return defaultRequestSpecification()
                .when()
                .get("/public/v1/users");
    }

    public static Response getUserWithParameter(String key, String value){
        return defaultRequestSpecification()
                .when()
                .queryParam(key,value)
                .get("/public/v1/users/");
    }

    public static Response deleteUser(int id) {
        return defaultRequestSpecification()
                .when()
                .delete("/public/v1/users/" + id);
    }

    public static Response noAuthDeleteUser(int id) {
        return noAuthentication()
                .when()
                .delete("/public/v1/users/" + id);
    }
}
