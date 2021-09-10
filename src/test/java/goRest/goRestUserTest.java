package goRest;

import goRest.model.user;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class goRestUserTest {

    @Test
    public void getUsers() {
        List<user> userList =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        //  .log().body()
                        .extract().jsonPath().getList("data", user.class)// aradaki parcayi almak icin jsonPath() kullanildi
                ;


        for (user u : userList) {
            System.out.println("u = " + u);
        }
    }

    int userID;

    @Test
    public void createUser() {
        userID =
                given()
                        .header("Authorization", "Bearer 36e95c8fd3e7eb89a65bad6edf4c0a62ddb758f9ed1e15bb98421fb0f1f3e57f")
                        .contentType(ContentType.JSON)
                        .body("{\"name\":\"ersin\", \"gender\":\"male\", \"email\":\"" + getRandomEmail() + "\", \"status\":\"active\"}")

                        .when()
                        .post("https://gorest.co.in/public/v1/users")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().jsonPath().getInt("data.id")


        ;
        System.out.println("userID = " + userID);


    }

    public String getRandomEmail() {
        String randomString = RandomStringUtils.randomAlphabetic(8).toLowerCase();

        return randomString + "@gmail.com";
    }


    @Test(dependsOnMethods = "createUser",priority = 1)
    public void getUserByID() {

        given()
                .pathParam("userID", userID)
                .log().uri()


                .when()
                .get("https://gorest.co.in/public/v1/users/{userID}")


                .then()
                .statusCode(200)
                .body("data.id", equalTo(userID))

        ;


    }

    @Test(dependsOnMethods = "createUser",priority = 2)
    public void updateUserById() {
        String name = "ersin ibrahim oral";
        given()
                .header("Authorization", "Bearer 36e95c8fd3e7eb89a65bad6edf4c0a62ddb758f9ed1e15bb98421fb0f1f3e57f")
                .contentType(ContentType.JSON)
                .body("{\"name\":\"" + name + "\"}")
                .pathParam("userID", userID)
                //.log().uri()


                .when()
                .put("https://gorest.co.in/public/v1/users/{userID}")


                .then()
                // .log().body()
                .statusCode(200)
                .body("data.name", equalTo(name))
        ;


    }

    @Test(dependsOnMethods = "createUser",priority = 3)
    public void deleteUserById() {
        given()
                .header("Authorization", "Bearer 36e95c8fd3e7eb89a65bad6edf4c0a62ddb758f9ed1e15bb98421fb0f1f3e57f")
                .pathParam("userID", userID)
                .log().uri()
                .when()
                .delete("https://gorest.co.in/public/v1/users/{userID}")
                // pathParam :  https://gorest.co.in/public/v1/users/1802
                // param  :    https://gorest.co.in/public/v1/users/%7BuserID%7D?userID=1803

                .then()
                .statusCode(204)
                .log().body()


        ;


    }


    @Test(dependsOnMethods = "deleteUserById")
    public void deleteUserByIdNegative() {
        given()
                .header("Authorization", "Bearer 36e95c8fd3e7eb89a65bad6edf4c0a62ddb758f9ed1e15bb98421fb0f1f3e57f")
                .pathParam("userID", userID)

                .when()
                .delete("https://gorest.co.in/public/v1/users/{userID}")


                .then()
                .log().body()
                .statusCode(404)



        ;


    }
}
