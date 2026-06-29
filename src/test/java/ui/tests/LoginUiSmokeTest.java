package ui.tests;

import framework.BaseTest;
import framework.ConfigManager;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.pages.LoginPage;

public class LoginUiSmokeTest extends BaseTest {
    @Test(enabled = false, description = "Example disabled UI-only test; E2E suite covers active UI login.")
    public void loginPageLoads() {
        LoginPage loginPage = new LoginPage(getDriver()).open(ConfigManager.get("base.url"));
        Assert.assertNotNull(loginPage);
    }
}
