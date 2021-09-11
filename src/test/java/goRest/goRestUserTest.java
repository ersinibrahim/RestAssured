package goRest;

import goRest.model.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class goRestUserTest {

    @Test
    public void getUsers() {
        List<User> userList =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .log().body()
                        .extract().jsonPath().getList("data", User.class)// aradaki parcayi almak icin jsonPath() kullanildi
                ;


        for (User u : userList) {
            System.out.println("u = " + u);
        }

        // Daha önceki örneklerde Clas dönüşümleri için tüm yapıya karşılık gelen
        // gereken tüm classları yazarak dönüştürüp istediğimiz elemanlara ulaşıyorduk.
        // Burada ise aradaki bir veriyi clasa dönüştürerek bir list olarak almamıza
        // imkan veren JSONPATH i kullandık.Böylece tek class ise veri alınmış oldu
        // diğer class lara gerek kalmadan

        // path : class veya tip dönüşümüne imkan veremeyen direk veriyi verir. List<String> gibi
        // jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek , veriyi istediğimiz formatta verir.

    }

    // Create User için POSTMAN de yapılanlar

//    JSON olarak gidecek body  {"name":"{{$randomFullName}}", "gender":"male", "email":"{{$randomEmail}}", "status":"active"}
//
//    header ın içine
//    Authorization  Bearer 36e95c8fd3e7eb89a65bad6edf4c0a62ddb758f9ed1e15bb98421fb0f1f3e57f
//
//    POST ile https://gorest.co.in/public/v1/users çağırdık
//    id yi okuduk ve global bir değişkene attık ki, diğer request larde kullanabilelim




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


    @Test(dependsOnMethods = "createUser", priority = 1)
    public void getUserByID() {

        given()
                .pathParam("userID", userID)
                .log().uri()


                .when()
                .get("https://gorest.co.in/public/v1/users/{userID}")


                .then()
                .log().body()
                .statusCode(200)
                .body("data.id", equalTo(userID))

        ;


    }

    @Test(dependsOnMethods = "createUser", priority = 2)
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


        //deleteUserById

    @Test(dependsOnMethods = "createUser", priority = 3)
    public void deleteUserById() {
        given()
                .header("Authorization", "Bearer 36e95c8fd3e7eb89a65bad6edf4c0a62ddb758f9ed1e15bb98421fb0f1f3e57f")
                .pathParam("userID", userID)  // int sayi=5 gibi
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
               // .log().body()
                .statusCode(404)


        ;


    }

    @Test
    public void responseSample() {
        Response turnResult =   // dönen sonuclarin hepsi tek bir degiskene atildi
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        .log().body()
                        .extract().response();

        List<User> userList = turnResult.jsonPath().getList("data", User.class);
        int total = turnResult.jsonPath().getInt("meta.pagination.total");
        int limit=turnResult.jsonPath().getInt("meta.pagination.limit");
        User firstUser = turnResult.jsonPath().getObject("data[0]", User.class);

        System.out.println("firstUser = " + firstUser);
        System.out.println("total = " + total);
        System.out.println("limit = " + limit);
        System.out.println("userList = " + userList);


/*int total=
        given()
                .when()
                .get("https://gorest.co.in/public/v1/users")
                .then()
                .log().body()
                .extract().jsonPath().getInt("meta.pagination.total")
        ;
*/
    }

@Test
    public void createUserBodyMap() {

        Map<String,String> newUser=new HashMap<>();  // verileri istersek Map olarak gönderebiliriz, Content.JSON onu JSON sa çeviriyor
        newUser.put("name","ersin");
        newUser.put("gender","male");
        newUser.put("email",getRandomEmail());
        newUser.put("status","active");



        userID =
                given()
                        .header("Authorization", "Bearer 36e95c8fd3e7eb89a65bad6edf4c0a62ddb758f9ed1e15bb98421fb0f1f3e57f")
                        .contentType(ContentType.JSON)
                        .body(newUser)

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


}
