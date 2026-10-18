package apiTests;

import config.TestBaseAPI;
import helpers.UserAPIClient;
import io.github.cdimascio.dotenv.Dotenv;
import io.qameta.allure.*;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import specs.Spec;
import static io.netty.handler.codec.rtsp.RtspHeaders.Values.URL;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import io.restassured.response.Response;

public class DiskTests extends TestBaseAPI {
    UserAPIClient userAPIClient = new UserAPIClient();

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Tag("Positive")
    @DisplayName("Тест создания файла")
    @Owner("Quthon")
    public void createFileTest() {
        Spec.installRequestSpecifications(Spec.requestSpec());
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

        String method = response.path("method");
        Boolean templated = response.path("templated");
        String operationId = response.path("operation_id");

        assertEquals("PUT", method, "Метод загрузки должен быть PUT");
        assertNotNull(href, "В ответ API Яндекса должен отправить ссылку");
        assertEquals(false, templated, "Значение templated по умолчанию должно быть false, " +
                "т. к. оно не заполнено");
        assertNotNull(operationId, "operationId должен быть в ответе");

        //пост условие
        userAPIClient.deleteFile();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Tag("Positive")
    @DisplayName("Тест скачивания файла на ПК")
    @Owner("Quthon")
    public void downloadInPCFileTest() {
        Spec.installRequestSpecifications(Spec.requestSpec());
        userAPIClient.createFile();

        String fileName = "/a.txt";

        Response response = given()
                .queryParam("path", fileName)
                .when()
                .get("v1/disk/resources/download")
                .then().log().all()
                .extract().response();

        String href = response.path("href");

        given()
                .urlEncodingEnabled(false)
                .when()
                .get(href)
                .then().log().all()
                .statusCode(200)
                .extract();

        String method = response.path("method");
        Boolean templated = response.path("templated");

        assertEquals("GET", method, "Метод загрузки должен быть GET");
        assertNotNull(href, "В ответ API Яндекса должен отправить ссылку");
        assertEquals(false, templated, "Значение templated по умолчанию должно быть false, " +
                "т. к. оно не заполнено");

        userAPIClient.deleteFile();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Tag("Positive")
    @DisplayName("Тест удаления файла")
    @Owner("Quthon")
    public void deleteFileTest() {
        Spec.installRequestSpecifications(Spec.requestSpec());

        //предусловие на создание файла который автотест будет удалять
        userAPIClient.createFile();

        String fileName = "/a.txt";

        given()
                .queryParam("path", fileName)
                .queryParam("permanently", true)
                .when()
                .delete("v1/disk/resources")
                .then().log().all()
                .statusCode(204)
                .extract();
    }

}
