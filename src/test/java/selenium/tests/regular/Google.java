package selenium.tests.regular;

import com.github.jeansantos38.stf.framework.webdriver.WebDriverSeleniumHelper;
import io.qameta.allure.Description;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import selenium.base.SeleniumTestBase;

public class Google extends SeleniumTestBase {

    private WebDriverSeleniumHelper selenium;

    @BeforeClass
    @Parameters({"seleniumHost", "seleniumDriverType", "browserName", "browserVersion", "headed", "vnc", "recording", "networkInterface"})
    public void initialize(@Optional("http://localhost:4444/wd/hub") String seleniumHost,
                           @Optional("REMOTE_SELENOID") SeleniumWebDriverType seleniumDriverType,
                           @Optional("chrome") String browserName,
                           @Optional("80.0") String browserVersion,
                           @Optional("true") Boolean headed,
                           @Optional("true") Boolean vnc,
                           @Optional("false") Boolean recording,
                           @Optional("eth1") String networkInterface) throws Exception {

        selenium = startRemoteBrowser(seleniumHost, seleniumDriverType, browserName, browserVersion, headed, vnc, recording);
    }

    @AfterClass
    public void cleanup() {
        selenium.endSeleniumWebdriverUsage();
    }

    @Test
    @Description("Simple test that performs a search in google.")
    public void googleSearchTest() throws Exception {
        String toBeSearched = "PrintOS";
        By searchButton = new By.ByCssSelector("input[name='btnK']");
        By textBox = new By.ByCssSelector("input[role='combobox'][type='text']");
        By searchResultSpan = new By.ByCssSelector("a[href='https://www.printos.com/']");
        By searchResultHref = new By.ByXPath("//h3/span[text()='HP PrintOS']");
        selenium.navigate("http://www.google.com");
        selenium.waitForElementExists(searchButton, 20);
        selenium.first(textBox).sendKeys(toBeSearched);
        WebDriverWait wait = new WebDriverWait(selenium.getSeleniumDriver(), 20);
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
        selenium.waitForElementExists(searchResultHref, 5);
        selenium.waitForElementExists(searchResultSpan, 5);
    }
}