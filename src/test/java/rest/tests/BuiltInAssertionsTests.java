package rest.tests;

import com.github.jeansantos38.stf.dataclasses.web.http.HttpDetailedHeader;
import com.github.jeansantos38.stf.dataclasses.web.http.HttpDetailedResponse;
import com.github.jeansantos38.stf.enums.http.HttpRequestMethod;
import com.github.jeansantos38.stf.framework.io.InputOutputHelper;
import io.qameta.allure.Description;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import rest.base.HttpClientTestBase;

import java.io.IOException;

public class BuiltInAssertionsTests extends HttpClientTestBase {

    byte[] requestBody;
    byte[] responsePayload;
    String endpoint;
    String url;
    HttpDetailedHeader httpDetailedHeader = new HttpDetailedHeader("stf", "qa");

    @BeforeClass
    public void initializeMocks() throws IOException {
        requestBody = InputOutputHelper.readContentFromFile(discoverAbsoluteFilePath("requestBody.json")).getBytes();
        responsePayload = InputOutputHelper.readContentFromFile(discoverAbsoluteFilePath("responseBooksStore.json")).getBytes();
        endpoint = "/deserialize/some/pojos.bookstore";
        url = baseServerUrl + endpoint;
        configureStub(wireMockServer, responsePayload, endpoint);
    }

    @Test
    @Description("Happy path test. Using built in assertions!")
    public void builtInAssertionsTest() throws Exception {
        httpClient.performHttpRequest(HttpRequestMethod.POST, url, httpDetailedHeader, requestBody)
                .assertExpectedStatusCode(200)
                .assertResponseElapsedTimeInMs(2000)
                .assertResponseContainsHeader("Content-Type", "text/plain")
                .assertResponseContainsHeader("Matched-Stub-Id")
                .assertResponseBodyContainsText("The Lord of the Rings")
                .assertJsonPathFromResponse(HttpDetailedResponse.Operator.EQUAL, "$['store']['book'][3]['title']", "The Lord of the Rings");
    }

    @Test
    @Description("Happy path test! A bit more verbose mode.")
    public void builtInAssertionsTest2() throws Exception {
        HttpDetailedResponse httpDetailedResponse = httpClient.performHttpRequest(HttpRequestMethod.POST, url, httpDetailedHeader, requestBody);
        httpDetailedResponse.assertExpectedStatusCode(200);
        httpDetailedResponse.assertResponseElapsedTimeInMs(2000);
        httpDetailedResponse.assertResponseContainsHeader("Content-Type", "text/plain");
        httpDetailedResponse.assertResponseContainsHeader("Matched-Stub-Id");
        httpDetailedResponse.assertResponseBodyContainsText("The Lord of the Rings");
        httpDetailedResponse.assertJsonPathFromResponse(HttpDetailedResponse.Operator.EQUAL, "$['store']['book'][3]['title']", "The Lord of the Rings");
    }
}