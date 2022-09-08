package models;

import io.restassured.response.Response;

public class GetUserModel {
    private int id;
    private String name;
    private String gender;
    private String email;
    private String status;

    public GetUserModel(Response response) {
        id = response.body().path("data.id");
        name = response.body().path("data.name");
        email = response.body().path("data.email");
        gender = response.body().path("data.gender");
        status = response.body().path("data.status");
    }

    public int getId() {
        return  id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }

}
