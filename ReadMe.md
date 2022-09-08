# QA Engineer Test
- This framework is about Backend Automation Testing
- I started my work by analyzing API document
- Then I created my scenarios, and I executed them manually on Postman
- After this I wrote my test scripts by using RestAssured-Java. 
- Framework supports parallel execution and data-driven testing
- Framework generates surefire-reports under target folder
- Tools : Postman, Maven, Java, RestAssured, JUnit Jupiter, JavaFaker


# Following scenarios are executed in this framework and failed ones are declared.

## Create User Scenarios:
    Scenario 1 : Create a new user with valid information successfully (Positive Scenario)
    Scenario 2 : Create a new user without **name** (Negative Scenario)
    Scenario 3 : Create a new user without **email** (Negative Scenario)
    Scenario 4 : Create a new user without **gender** (Negative Scenario)
    Scenario 5 : Create a new user without **status** (Negative Scenario)
    Scenario 6 : Create a new user without **any information** (Negative Scenario)
    Scenario 7 : Create a new user with **invalid name** (Negative Scenario)
                 FAILED : User should not be created with non-alphabetical characters!
    Scenario 8 : Create a new user with **invalid email** (Negative Scenario)
                 FAILED : User should not be created with invalid emails!
    Scenario 9 : Create a new user with **invalid gender** (Negative Scenario)
    Scenario 10 : Create a new user with **invalid status** (Negative Scenario)
    Scenario 11 : Create a new user with **email that already taken** (Negative Scenario)
    Scenario 12 : Create a new user without **authentication** (Negative Scenario)

## Update User with PUT Request Scenarios:

    Scenario 1 : Update a user with valid information successfully (Positive Scenario)
    Scenario 2 : Update a new user without **name** (Negative Scenario)
    Scenario 3 : Update a new user without **email** (Negative Scenario)
    Scenario 4 : Update a new user without **gender** (Negative Scenario)
    Scenario 5 : Update a new user without **status** (Negative Scenario)
    Scenario 6 : Update a new user without **any information** (Negative Scenario)
    Scenario 7 : Update a new user with **invalid name** (Negative Scenario)
                 FAILED : User should not be updated with non-alphabetical characters!
    Scenario 8 : Update a new user with **invalid email** (Negative Scenario)
                 FAILED : User should not be updated with invalid emails!
    Scenario 9 : Update a new user with **invalid gender** (Negative Scenario)
    Scenario 10 : Update a new user with **invalid status** (Negative Scenario)
    Scenario 11 : Update a new user with **email that already taken** (Negative Scenario)
    Scenario 12 : Update a new user without **authentication** (Negative Scenario)
                  FAILED : Status code should be 401, but it throws 404 error!

## Update User with PATCH Request Scenarios:

    Scenario 1 : Update a user with valid name successfully (Positive Scenario)
    Scenario 2 : Update a user with valid email successfully (Positive Scenario)
    Scenario 3 : Update a user with valid gender successfully (Positive Scenario)
    Scenario 4 : Update a user with valid status successfully (Positive Scenario)
    Scenario 5 : Update a new user without **name** (Negative Scenario)
    Scenario 6 : Update a new user without **email** (Negative Scenario)
    Scenario 7 : Update a new user without **gender** (Negative Scenario)
    Scenario 8 : Update a new user without **status** (Negative Scenario)
    Scenario 9 : Update a new user with **invalid name** (Negative Scenario)
                 FAILED : User should not be updated with non-alphabetical characters!
    Scenario 10 : Update a new user with **invalid email** (Negative Scenario)
                 FAILED : User should not be updated with invalid emails!
    Scenario 11 : Update a new user with **invalid gender** (Negative Scenario)
    Scenario 12 : Update a new user with **invalid status** (Negative Scenario)
    Scenario 13 : Update a new user with **email that already taken** (Negative Scenario)
    Scenario 14 : Update a new user without **authentication** (Negative Scenario)
                  FAILED : Status code should be 401, but it throws 404 error!

## Get User Data Scenarios:

    Scenario 1 : Get all users data successfully (Positive Scenario)
    Scenario 2 : Get a user data by ID successfully (Positive Scenario)
    Scenario 3 : Get a user data by name successfully (Positive Scenario)
    Scenario 4 : Get a user data by email successfully (Positive Scenario)
    Scenario 5 : Get a user data by non-existing ID (Negative Scenario)
    Scenario 6 : Get a user data by non-existing name (Negative Scenario)
    Scenario 7 : Get a user data by non-existing email (Negative Scenario)

## Delete User Scenarios:

    Scenario 1 : Delete a user by ID successfully (Positive Scenario)
    Scenario 2 : Delete a user by non-existing ID (Negative Scenario)
    Scenario 3 : Delete a user without **authentication** (Negative Scenario)
                 FAILED : Status code should be 401, but it throws 404 error!
