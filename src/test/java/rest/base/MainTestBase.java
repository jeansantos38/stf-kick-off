package rest.base;


import com.github.jeansantos38.stf.framework.logger.TestLog;
import com.github.jeansantos38.stf.framework.wait.WaitHelper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.File;

public class MainTestBase {

    protected static TestLog testLog;
    protected WaitHelper waitHelper;


    @BeforeClass
    @Parameters({"optionalGlobalParameterExample"})
    public void mainTestBaseInitialize(@Optional("") String param1) throws Exception {
        testLog = new TestLog(true);
        testLog.logIt("Parameter value provided from testRunConfig:" + param1);
        waitHelper = new WaitHelper();
    }

    protected String discoverAbsoluteFilePath(String filename) {
        return new File(String.format("src/test/resources/%s", filename)).getAbsolutePath();
    }
}