package misc;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import rest.base.MainTestBase;

import static com.github.jeansantos38.stf.framework.misc.CalendarHelper.convertTimestampToDate;

public class CalendarTests extends MainTestBase {

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("A QA should be capable of easily handling date formats using STF")
    @Description("Converting a timestamp in human readable date format")
    public void convertTimestampToDateTest() {
        String extractedDate = convertTimestampToDate(Long.valueOf("1564800583569"));
        //The results depends on where it runs.
        String expected1 = "08/02/2019 23:49:43";
        String expected2 = "08/03/2019 02:49:43";
        Assert.assertTrue(extractedDate.equals(expected1) || extractedDate.equals(expected2),
                String.format("Expected [%s] or [%s], but got %s", expected1, expected2, extractedDate));
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("A QA should be capable of easily handling date formats using STF")
    @Description("Converting a timestamp in specific time format")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void convertTimestampUsingPattern() {
        String extractedDate = convertTimestampToDate(Long.valueOf("1564800583569"), "yyyy/dd/MM' 'HH:mm:ss");
        String expected1 = "2019/02/08 23:49:43";
        String expected2 = "2019/02/08 02:49:43";
        //The results depends on where it runs.
        Assert.assertTrue(extractedDate.equals(expected1) || extractedDate.equals(expected2),
                String.format("Expected [%s] or [%s], but got %s", expected1, expected2, extractedDate));
    }
}