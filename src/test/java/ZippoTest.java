import POJO.Location;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

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
    public void bodyJsonPathTest2andHasItem() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places[0].state", equalTo("California")) // listin 0 indexli state degerini verir, 1 Deger
        // .body("places.'state'",hasItem("California")) //Bütün listteki stateleri bir list olarak verir
        // (bütün statelerde aranan eleman var mi?)
        ;
//        places[0].state -> listin 0 indexli elemanının state değerini verir, 1 değer
//        places.state ->    Bütün listteki state leri bir list olarak verir : California,California2   hasItem
//        List<String> list= {'California','California2'}


    }


    @Test
    public void bodyJsonPathTest4() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places[0].'place name'", equalTo("Beverly Hills"))
                // arasında bosluk olan key lerde keyin başına ve sonuna tek tırnak konur ('place name')
                .statusCode(200)
        ;
    }

    @Test
    public void bodyArrayHasSizeTest() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .body("places", hasSize(1)) // verilen path deki listin size kontrolü
                .log().body()
                .statusCode(200)
        ;
    }

    @Test
    public void combiningTest() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .body("places", hasSize(1))
                .body("places.state", hasItem("California"))
                .body("places[0].'place name'", equalTo("Beverly Hills"))
                // 1 den fazla assertion yapılabilir.
                .statusCode(200)
        ;
    }

    @Test
    public void pathParamTest() {
        String country = "us";
        String zipKod = "90210";

        given()
                .pathParam("country", country)
                .pathParam("zipKod", zipKod)
                .log().uri() //request linki

                .when()
                .get("http://api.zippopotam.us/{country}/{zipKod}")

                .then()
                .log().body()
                .body("places", hasSize(1))
        ;
    }


    @Test
    public void pathParamTest2() {
        String country = "us";

        for (int i = 90210; i < 90214; i++) {
            //String zipKod = i.toString();

            given()
                    .pathParam("country", country)
                    .pathParam("zipKod", i)
                    .log().uri() //request linki

                    .when()
                    .get("http://api.zippopotam.us/{country}/{zipKod}")

                    .then()
                    .log().body()
                    .body("places", hasSize(1))
            ;
        }

    }


    // https://gorest.co.in/public/v1/users?page=1
    @Test
    public void queryParamTest() {
        given()
                .param("page", 1)
                .log().uri()

                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                .log().body()
                .body("meta.pagination.page", equalTo(1));

    }


    @Test
    public void queryParamTestCoklu() {
        for (int page = 1; page <= 10; page++) {
            given()
                    .param("page", page)
                    .log().uri()

                    .when()
                    .get("https://gorest.co.in/public/v1/users")

                    .then()
                    .log().body()
                    .body("meta.pagination.page", equalTo(page));
        }

    }

    private ResponseSpecification responseSpecification;
    private RequestSpecification requestSpecification;

    @BeforeClass
    public void setup() {
        baseURI = "http://api.zippopotam.us";  //RestAssured kendi statik degiskeni tanimli deger ataniyor.
        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.BODY)
                .build();

        requestSpecification = new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setAccept(ContentType.JSON)
                .build();
    }

    @Test
    public void bodyArrayHasSizeTest_baseUriTest() {
        given()
                .log().uri()
                .when()
                .get("/us/90210")//buradaki url nin basinda hhtp yoksa baseUri deki deger otomatik geliyor

                .then()
                .body("places", hasSize(1)) // verilen path deki listin size kontrolü
                .log().body()
                .statusCode(200)
        ;
    }


    @Test
    public void bodyArrayHasSizeTest_responseSpecification() {
        given()
                .log().uri()
                .when()
                .get("/us/90210")//buradaki url nin basinda hhtp yoksa baseUri deki deger otomatik geliyor

                .then()
                .body("places", hasSize(1)) // verilen path deki listin size kontrolü
                .spec(responseSpecification)
        ;
    }


    //JSON extract

    @Test
    public void extractingJsonPath() {
        String place_name = given()
                .spec(requestSpecification)
                .when()
                .get("/us/90210")
                .then()
                .spec(responseSpecification)
                .extract().path("places[0].'place name'") // extract metodu ile given ile baslayan satir,bir deger
                // döndürür hale geldi

                ;


        System.out.println("place_name = " + place_name);


    }


    @Test
    public void extractingJsonPathInt() {
       int limit = given()
               .param("page",1)
               //.log().uri()
                .when()
                .get("https://gorest.co.in/public/v1/users")
                .then()
               //.log().body()
                .extract().path("meta.pagination.limit") // extract metodu ile given ile baslayan satir,bir deger
                // döndürür hale geldi

                ;


        System.out.println("limit = " + limit);


    }


    @Test
    public void extractingJsonPathIntList() {
        List<Integer> idS = given()
                .param("page",1)
                //.log().uri()
                .when()
                .get("https://gorest.co.in/public/v1/users")
                .then()
                //.log().body()
                .extract().path("data.id") // extract metodu ile given ile baslayan satir,bir deger
                // döndürür hale geldi

                ;


        System.out.println("idS = " + idS);


    }

    @Test
    public void extractingJsonPathStringList() {
        List<String> districts = given()
               .spec(requestSpecification)
                .when()
                .get("/tr/35030")
                .then()
               .spec(responseSpecification)
                .extract().path("places.'place name'") // extract metodu ile given ile baslayan satir,bir deger
                // döndürür hale geldi

                ;


        System.out.println("dstricts = " + districts);
        Assert.assertTrue(districts.contains("İnönü Mah."));


    }


    @Test
    public void extractingJsonPOJO()
    {
        Location location=
        given()

                .when()
                .get("/tr/35030")

                .then()
                .extract().as(Location.class)
                ;


        System.out.println("location = " + location);
        System.out.println("location.getCountry() = " + location.getCountry());
        System.out.println("location.getPlaces() = " + location.getPlaces());
        System.out.println("location.getPlaces().get(6).getPlacename() = " + location.getPlaces().get(6).getPlacename());


    }
}