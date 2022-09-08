package utilities;

import com.github.javafaker.Faker;

public class CreateRandomInfo {
    private static final Faker faker = new Faker();

    public static String randomName(){
        return faker.name().fullName();
    }

    public static String randomEmail(){
        return faker.internet().emailAddress();
    }

}
