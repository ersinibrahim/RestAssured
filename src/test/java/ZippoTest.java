import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;



public class ZippoTest {

    @Test
    public void test()
    {

        given()
                // hazirlik islemleri

        .when()
                // link aksiyon islemleri

        .then()
                //test ve extract islemleri

;

    }

    @Test
    public void statusCodeTest()
    {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body() //.log().all() -> bütün response u gösterir



        ;



    }




}
