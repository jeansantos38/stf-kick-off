package selenium.tests.parallel;

import com.github.jeansantos38.stf.framework.network.NetworkHelper;
import com.github.jeansantos38.stf.framework.webdriver.WebDriverSeleniumHelper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import selenium.base.SeleniumTestBase;
import selenium.tests.po.HtmlPlayGroundPageObject;

public class Parallel1 extends SeleniumTestBase {

    private WebDriverSeleniumHelper selenium;
    private String urlToMock = "/stf/web/ui/test";
    private String url;
    private HtmlPlayGroundPageObject testHtmlPageObject;

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

        url = stubWebPage(NetworkHelper.retrieveLocalIPv4Address(networkInterface), 8079, urlToMock, "src/test/resources", "stfHtmlPlayground.html", 2000);
        selenium = startRemoteBrowser(seleniumHost, seleniumDriverType, browserName, browserVersion, headed, vnc, recording);
        testHtmlPageObject = new HtmlPlayGroundPageObject();

        selenium.navigate(url);
        selenium.waitForElementExists(testHtmlPageObject.firstName, 10);
    }

    @AfterClass
    public void cleanup() {
        selenium.endSeleniumWebdriverUsage();
        endWebPageStub();
    }

    @Test
    public void textBoxTest() throws Exception {
        String cbx1 = "superTest1";
        String cbx2 = "superTest2";

        // one way to do it
        selenium.first(testHtmlPageObject.firstName).sendKeys(cbx1);
        Assert.assertEquals(selenium.first(testHtmlPageObject.firstName).getAttribute("value"), cbx1);

        // another way
        WebElement lastName = selenium.first(testHtmlPageObject.lastName);
        lastName.sendKeys(cbx2);
        Assert.assertEquals(lastName.getAttribute("value"), cbx2);
    }

    @Test
    public void dropDownTest() throws Exception {
        String optionText = "STAGING";
        String optionValue = "stg";
        Assert.assertNotEquals(selenium.first(testHtmlPageObject.dropDown).getAttribute("value"), optionValue, "Wrong default value!");
        selenium.first(testHtmlPageObject.dropDown).sendKeys(optionText);
        Assert.assertEquals(selenium.first(testHtmlPageObject.dropDown).getAttribute("value"), optionValue);
    }

    @Test
    public void checkBoxTest() throws Exception {
        selenium.first(testHtmlPageObject.checkBox1).click();
        Assert.assertTrue(selenium.first(testHtmlPageObject.checkBox1).isSelected());

        selenium.first(testHtmlPageObject.checkBox2).click();
        Assert.assertFalse(selenium.first(testHtmlPageObject.checkBox2).isSelected());

        selenium.first(testHtmlPageObject.checkBox3).click();
        Assert.assertTrue(selenium.first(testHtmlPageObject.checkBox3).isSelected());
    }

    @Test
    public void radioButton() throws Exception {
        WebElement rightRadioBtn = selenium.first(testHtmlPageObject.radioButtonRight);
        rightRadioBtn.click();
        Assert.assertTrue(rightRadioBtn.isSelected());
        Assert.assertFalse(selenium.first(testHtmlPageObject.radioButtonLeft).isSelected());
    }

    @Test
    public void clickAndWaitBanner() throws Exception {
        Assert.assertFalse(selenium.elementIsVisible(testHtmlPageObject.savedDataBanner, 2));
        selenium.first(testHtmlPageObject.saveTestDataBtn).click();
        Assert.assertTrue(selenium.elementIsVisible(testHtmlPageObject.savedDataBanner, 6));
        selenium.first(testHtmlPageObject.saveTestDataBtn).click();
        WebDriverWait wait = new WebDriverWait(selenium.getSeleniumDriver(), 6);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(testHtmlPageObject.savedDataBanner));
        Assert.assertFalse(selenium.elementIsVisible(testHtmlPageObject.savedDataBanner, 1));
    }
}