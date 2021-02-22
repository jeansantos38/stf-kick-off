package selenium.tests.po;

import org.openqa.selenium.By;

public class HtmlPlayGroundPageObject {

    public By firstName;
    public By lastName;
    public By dropDown;
    public By checkBox1;
    public By checkBox2;
    public By checkBox3;
    public By radioButtonLeft;
    public By radioButtonRight;
    public By tooltip;
    public By uploadFileBtn;
    public By resetFieldsBtn;
    public By saveTestDataBtn;
    public By savedDataBanner;

    public HtmlPlayGroundPageObject() {
        this.firstName = new By.ByCssSelector("#fname");
        this.lastName = new By.ByCssSelector("#lname");
        this.dropDown = new By.ByCssSelector("#stacks");
        this.checkBox1 = new By.ByCssSelector("#seleniumenv1");
        this.checkBox2 = new By.ByCssSelector("#seleniumenv2");
        this.checkBox3 = new By.ByCssSelector("#seleniumenv3");
        this.radioButtonLeft = new By.ByXPath("//*[@value='left']");
        this.radioButtonRight = new By.ByCssSelector("input[value='right']");
        this.tooltip = new By.ByCssSelector("div.tooltip");
        this.uploadFileBtn = new By.ByCssSelector("input[name='fileupload']");
        this.resetFieldsBtn = new By.ByCssSelector("div form button");
        this.saveTestDataBtn = new By.ByXPath("//*/button[text()='Save data']");
        this.savedDataBanner = new By.ByCssSelector("#myDIV");
    }


}
