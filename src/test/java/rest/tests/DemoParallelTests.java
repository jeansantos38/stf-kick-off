package rest.tests;

import com.github.jeansantos38.stf.dataclasses.web.http.HttpDetailedHeader;
import com.github.jeansantos38.stf.enums.http.HttpRequestMethod;
import com.github.jeansantos38.stf.framework.io.InputOutputHelper;
import io.qameta.allure.Description;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import rest.base.HttpClientTestBase;

import java.io.IOException;

public class DemoParallelTests extends HttpClientTestBase {

    byte[] requestBody;
    byte[] responseBodyToBeUsedByMock;
    String endpoint;
    String url;
    HttpDetailedHeader httpDetailedHeader = new HttpDetailedHeader("stf", "qa");

    @BeforeClass
    public void initializeMocks() throws IOException {

        requestBody = InputOutputHelper.readContentFromFile(discoverAbsoluteFilePath("requestBody.json")).getBytes();
        responseBodyToBeUsedByMock = InputOutputHelper.readContentFromFile(discoverAbsoluteFilePath("responseBooksStore.json")).getBytes();
        endpoint = "/deserialize/some/bookstore";
        url = baseServerUrl + endpoint;
        configureDelayedStub(wireMockServer, responseBodyToBeUsedByMock, endpoint);
    }

    @Test
    @Description("A simple rest test that calls an slow API.")
    public void slowTest1() throws Exception {
        httpClient.performHttpRequest(HttpRequestMethod.POST, url, httpDetailedHeader, requestBody)
                .assertExpectedStatusCode(200)
                .assertResponseElapsedTimeInMs(7000);
    }

    @Test
    @Description("A simple rest test that calls an slow API.")
    public void slowTest2() throws Exception {
        httpClient.performHttpRequest(HttpRequestMethod.POST, url, httpDetailedHeader, requestBody)
                .assertExpectedStatusCode(200)
                .assertResponseElapsedTimeInMs(7000);
    }

    @Test
    @Description("A simple rest test that calls an slow API.")
    public void slowTest3() throws Exception {
        httpClient.performHttpRequest(HttpRequestMethod.POST, url, httpDetailedHeader, requestBody)
                .assertExpectedStatusCode(200)
                .assertResponseElapsedTimeInMs(7000);
    }

    @Test
    @Description("A simple rest test that calls an slow API.")
    public void slowTest4() throws Exception {
        httpClient.performHttpRequest(HttpRequestMethod.POST, url, httpDetailedHeader, requestBody)
                .assertExpectedStatusCode(200)
                .assertResponseElapsedTimeInMs(7000);
    }

    @Test
    @Description("A simple rest test that calls an slow API.")
    public void slowTest5() throws Exception {
        httpClient.performHttpRequest(HttpRequestMethod.POST, url, httpDetailedHeader, requestBody)
                .assertExpectedStatusCode(200)
                .assertResponseElapsedTimeInMs(7000);
    }
}