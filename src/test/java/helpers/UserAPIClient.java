package helpers;

import io.github.cdimascio.dotenv.Dotenv;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserAPIClient {

    public void createFile() {
        String fileName = "/a.txt";

        Response response = given()
                .queryParam("path", fileName)
                .when()
                .get("v1/disk/resources/upload")
                .then().log().all()
                .extract().response();

        String href = response.path("href");

        given()
                .when()
                .put(href)
                .then().log().all()
                .statusCode(201)
                .extract();
    }

    public void deleteFile() {
        String fileName = "/a.txt";

        given()
                .queryParam("path", fileName)
                .queryParam("permanently", true)
                .when()
                .delete("v1/disk/resources")
                .then().log().all()
                .extract();
    }
}
