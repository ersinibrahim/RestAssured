import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class ZippoTest {

    @Test
    public void test() {

        given()
                // hazirlik islemleri

                .when()
                // link aksiyon islemleri

                .then()
        //test ve extract islemleri

        ;

    }

    @Test
    public void statusCodeTest() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body() //.log().all() -> bütün response u gösterir
                .statusCode(200) // stattus kontrolu


        ;


    }


    @Test
    public void contentTypeTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .contentType(ContentType.JSON)

        ;


    }

    @Test
    public void logTest() {
        given()
                .log().all()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()

        ;


    }


    @Test
    public void checkStateResponseBody() {
        given()


                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("country", equalTo("United States"))
                .statusCode(200)
        ;


    }


    @Test
    public void bodyJsonPathTest2()
    {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places[0].state",equalTo("California"))
                ;



    }
}
