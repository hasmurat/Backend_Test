package models;

import java.util.HashMap;
import java.util.Map;

public class UpdateUserModel {
    private static String name;
    private static String gender;
    private static String email;
    private static String status;

    public UpdateUserModel() {
    }

    public UpdateUserModel(String name,
                           String gender,
                           String email,
                           String status){
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static Map<String,String> updatePatch (String key, String value){
        Map<String,String> requestBody = new HashMap<>();
        requestBody.put(key,value);
        switch (key){
            case "name":
                name=value;
                break;
            case "email":
                email=value;
                break;
            case "gender":
                gender=value;
                break;
            case "status":
                status=value;
                break;
        }
        return requestBody;
    }

}
