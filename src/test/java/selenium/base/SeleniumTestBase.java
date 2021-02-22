package selenium.base;

import com.github.jeansantos38.stf.framework.logger.TestLog;
import com.github.jeansantos38.stf.framework.webdriver.WebDriverSeleniumHelper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import rest.base.MainTestBase;

import java.net.URI;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class SeleniumTestBase extends MainTestBase {

    private WireMockServer wireMockServer;

    protected WebDriverSeleniumHelper startRemoteBrowser(
            String seleniumHost,
            SeleniumWebDriverType webDriverType,
            String browserName,
            String browserVersion,
            boolean headed,
            boolean vnc,
            boolean recording) throws Exception {
        return startRemoteBrowser("", "", seleniumHost, webDriverType, browserName, browserVersion, headed, vnc, recording);
    }

    protected WebDriverSeleniumHelper startRemoteBrowser(
            String remoteWebDriverProxy,
            String remoteWebDriverProxyPort,
            String seleniumHost,
            SeleniumWebDriverType webDriverType,
            String browserName,
            String browserVersion,
            boolean headed,
            boolean enableVNC,
            boolean enableRecording) throws Exception {
        WebDriver driver;

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
        if (webDriverType == SeleniumWebDriverType.REMOTE_SELENOID) {
            capabilities.setCapability("enableVNC", enableVNC);
            capabilities.setCapability("enableVideo", enableRecording);
        }
        switch (webDriverType) {
            case REMOTE_SELENIUM_GRID:
            case REMOTE_SELENOID:
                driver = new RemoteWebDriver(URI.create(seleniumHost).toURL(), capabilities);
                break;
            default:
                throw new Exception(String.format("The local webdriver %s is not supported for remote selenium server!", webDriverType.toString()));
        }
        WebDriverSeleniumHelper webDriverSeleniumHelper = new WebDriverSeleniumHelper(30, new TestLog());
        webDriverSeleniumHelper.setSeleniumDriver(driver);
        return webDriverSeleniumHelper;
    }

    protected WebDriverSeleniumHelper startLocalBrowser(
            SeleniumWebDriverType webDriverType,
            String webdriverPath,
            boolean headed) throws Exception {
        WebDriver driver;
        switch (webDriverType) {
            case CHROME_DRIVER:
                System.setProperty("webdriver.chrome.driver", webdriverPath);
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                if (!headed)
                    options.addArguments("--headless");
                driver = new ChromeDriver(options);
                break;
            default:
                throw new Exception(String.format("The webdriver type %s is not supported!", webDriverType.toString()));
        }
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

    protected enum SeleniumWebDriverType {
        REMOTE_SELENIUM_GRID, REMOTE_SELENOID, CHROME_DRIVER
    }
}
