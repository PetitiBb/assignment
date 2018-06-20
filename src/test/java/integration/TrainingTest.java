package integration;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.jayway.jsonpath.JsonPath;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import io.restassured.http.ContentType;
import java.io.IOException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static util.DataTest.FAILING_MSG;
import static util.DataTest.HOST;
import static util.DataTest.NOK_MSG;
import static util.DataTest.PATH_REC;
import static util.DataTest.PATH_TRAIN;
import static util.DataTest.params;
import static util.DataTestLoader.readJsonFromFile;
import static util.MockedApi.mockRecognitionEndpoint;
import static util.MockedApi.mockTrainingEndpoint;


@RunWith(DataProviderRunner.class)
public class TrainingTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8085);

    @DataProvider
    public static Object[][] testCases() {
        return new Object[][] {
                {"training_sentences_2"},
                {"training_sentences_10"}
        };
    }

    @Test
    @UseDataProvider("testCases")
    public void shouldSucceedEnoughTrainingSentences(String fileName) throws IOException {
        //precondition: the food shouldn't be recognized already
        mockRecognitionEndpoint(400, NOK_MSG);
        given().queryParams(params).get(HOST + PATH_REC)
                .then().statusCode(400);

        //get number of sentences & name of the food from the test case file
        String data = readJsonFromFile(fileName);
        int count = JsonPath.read(data, "$.training_sentences.length()");
        String foodName = JsonPath.read(data, "$.food_name");

        //mock the training endpoint for the new food "soup"
        mockTrainingEndpoint(200, "{\"food\": \"" + foodName + "\", \"sentences_count\": " + count + "}");

        //check the response from the training endpoint
        given().body(data).when().post(HOST + PATH_TRAIN).then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body("food", equalTo(foodName))
                .body("sentences_count", equalTo(count));

        //check final recognition
        mockRecognitionEndpoint(200, "{\"food\": \"soup\", \"position\": 10}");
        given().queryParams(params).get(HOST + PATH_REC)
                .then().statusCode(200);
    }

    @Test
    public void shouldFailNotEnoughTrainingSentences() throws IOException {
        //precondition: the food shouldn't be recognized already
        mockRecognitionEndpoint(400, NOK_MSG);
        given().queryParams(params).get(HOST + PATH_REC)
                .then().statusCode(400);

        //mock training endpoint to reply with 405 when the input data is not enough
        mockTrainingEndpoint(405, FAILING_MSG);

        //check the response from the training endpoint
        given().body(readJsonFromFile("training_sentences_1"))
                .when()
                .post(HOST + PATH_TRAIN)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(405)
                .body(equalTo(FAILING_MSG));

        //check the final recognition doesn't work
        mockRecognitionEndpoint(400, NOK_MSG);
        given().queryParams(params).get(HOST + PATH_REC)
                .then().statusCode(400);
    }
}
