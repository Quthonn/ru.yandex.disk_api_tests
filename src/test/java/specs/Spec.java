package specs;

import io.github.cdimascio.dotenv.Dotenv;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.filter.log.LogDetail.BODY;

public class Spec {
    public static RequestSpecification requestSpec(){
        Dotenv dotenv = Dotenv.load();

        String TOKEN = dotenv.get("TOKEN");

        return new RequestSpecBuilder()
                .setAuth(RestAssured.oauth2(TOKEN))
                .setContentType(ContentType.JSON)
                .build();
    }

    public static ResponseSpecification responseSpecOK(int code){
        return new ResponseSpecBuilder()
                .expectStatusCode(code)
                .build();
    }

    public static void installSpecifications(RequestSpecification request, ResponseSpecification response) {
        RestAssured.requestSpecification = request;
        RestAssured.responseSpecification = response;
    }

    public static void installRequestSpecifications(RequestSpecification request) {
        RestAssured.requestSpecification = request;
    }
}
