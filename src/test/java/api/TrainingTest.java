package api;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.restassured.http.ContentType;
import org.junit.Rule;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static util.DataTest.HOST;
import static util.DataTest.NOK_MSG;
import static util.DataTest.PATH_REC;
import static util.DataTest.PARAMS;
import static util.MockedApi.mockRecognitionEndpoint;

public class TrainingTest {


    @Rule public WireMockRule wireMockRule = new WireMockRule(8085);

    @Test
    public void shouldSucceedRecognition() {
        mockRecognitionEndpoint(200, "{\"food\": \"soup\", \"position\": 10}");
        given().queryParams(PARAMS).when()
                .get(HOST + PATH_REC)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body("food", equalTo("soup"))
                .body("position", equalTo(10));
    }

    @Test
    public void shouldFailRecognition() {
        mockRecognitionEndpoint(400, NOK_MSG);
        given().queryParams(PARAMS).when()
                .get(HOST + PATH_REC)
                .then()
                .statusCode(400)
                .body(equalTo(NOK_MSG));

    }

}
