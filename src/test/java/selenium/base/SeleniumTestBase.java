package selenium.base;

import com.github.jeansantos38.stf.framework.logger.TestLog;
import com.github.jeansantos38.stf.framework.webdriver.WebDriverSeleniumHelper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import rest.base.MainTestBase;

import java.net.MalformedURLException;
import java.net.URI;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class SeleniumTestBase extends MainTestBase {

    protected void endSession(WebDriverSeleniumHelper webDriverSeleniumHelper) {
        webDriverSeleniumHelper.endSeleniumWebdriverUsage();
    }

    private WireMockServer wireMockServer;

    protected WebDriverSeleniumHelper startBrowser(
            String seleniumHost,
            String browserName,
            String browserVersion,
            boolean vnc,
            boolean recording) throws MalformedURLException {
        return startBrowser("", "", seleniumHost, browserName, browserVersion, vnc, recording);
    }

    protected WebDriverSeleniumHelper startBrowser(
            String remoteWebDriverProxy,
            String remoteWebDriverProxyPort,
            String seleniumHost,
            String browserName,
            String browserVersion,
            boolean enableVNC,
            boolean enableRecording) throws MalformedURLException {
        if (!remoteWebDriverProxy.isEmpty() && !remoteWebDriverProxyPort.isEmpty()) {
            System.getProperties().put("http.proxyHost", remoteWebDriverProxy);
            System.getProperties().put("http.proxyPort", remoteWebDriverProxyPort);
            System.getProperties().put("https.proxyHost", remoteWebDriverProxy);
            System.getProperties().put("https.proxyPort", remoteWebDriverProxyPort);
        }
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(browserName);
        capabilities.setVersion(browserVersion);
        //If not selenoid/moon grid solution, do not set it even to false, might crash regular selenium grid
        if (enableRecording || enableVNC) {
            capabilities.setCapability("enableVNC", enableVNC);
            capabilities.setCapability("enableVideo", enableRecording);
        }
        RemoteWebDriver driver = new RemoteWebDriver(
                URI.create(seleniumHost).toURL(),
                capabilities
        );
        WebDriverSeleniumHelper webDriverSeleniumHelper = new WebDriverSeleniumHelper(30, new TestLog());
        webDriverSeleniumHelper.setSeleniumDriver(driver);
        return webDriverSeleniumHelper;
    }

    protected String stubWebPage(
            String hostAddress,
            int port,
            String urlToBeMocked,
            String resourcesPath,
            String htmlFileName,
            int millisecondsDelay) {
        wireMockServer = new WireMockServer(options().bindAddress(hostAddress).port(port).usingFilesUnderClasspath(resourcesPath));
        wireMockServer.start();
        wireMockServer.stubFor(any(urlEqualTo(urlToBeMocked))
                .willReturn(aResponse()
                        .withLogNormalRandomDelay(millisecondsDelay, 0.1)
                        .withStatus(200)
                        .withHeader("Content-Type", "text/html")
                        .withHeader("Set-Cookie", "stf_id=smartTestFrameworkUiCookie123")
                        .withBodyFile(htmlFileName)));
        return String.format("http://%s:%s%s", hostAddress, port, urlToBeMocked);
    }

    protected void endWebPageStub() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }
}
