package rest.base;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.File;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class WireMockTestBase extends MainTestBase {

    protected Integer wiremockPort = 7654;
    protected WireMockServer wireMockServer;
    protected String baseServerUrl;

    @BeforeClass
    public void initializeMockServer() {
        wireMockServer = new WireMockServer(options().port(wiremockPort));
        wireMockServer.start();
        baseServerUrl = String.format("http://127.0.0.1:%s", wiremockPort);
    }

    @AfterClass
    public void endMockServer() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }

    protected void configureStub(WireMockServer wireMockServer, byte[] responseBody, String endpoint) {
        wireMockServer.stubFor(any(urlEqualTo(endpoint))
                .withHeader("stf", containing("qa"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/plain")
                        .withBody(responseBody)));
    }
    protected void configureDelayedStub(WireMockServer wireMockServer, byte[] responseBody, String endpoint) {
        wireMockServer.stubFor(any(urlEqualTo(endpoint))
                .withHeader("stf", containing("qa"))
                .willReturn(aResponse()
                        .withLogNormalRandomDelay(5000, 0.1)
                        .withStatus(200)
                        .withHeader("Content-Type", "text/plain")
                        .withBody(responseBody)));
    }

}