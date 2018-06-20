package util;

import com.github.tomakehurst.wiremock.client.WireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static util.DataTest.PATH_REC;
import static util.DataTest.PATH_TRAIN;

public class MockedApi {

    public static void mockRecognitionEndpoint(int status, String body) {
        stubFor(get(urlPathEqualTo(PATH_REC))
                .willReturn(aResponse()
                        .withStatus(status)
                        .withHeader("Content-Type", "application/json")
                        .withBody(body)));
    }

    public static void mockTrainingEndpoint(int status, String body) {
        WireMock.stubFor((post(urlEqualTo(PATH_TRAIN))
                .willReturn(aResponse()
                        .withStatus(status)
                        .withHeader("Content-Type", "application/json")
                        .withBody(body))));

    }
}
