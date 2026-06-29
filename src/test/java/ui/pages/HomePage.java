package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.regex.Pattern;

public class HomePage extends BasePage {
    private final By displayName = By.cssSelector("[data-testid='display-name']");
    private final By displayEmail = By.cssSelector("[data-testid='display-email']");
    private final By displayMobile = By.cssSelector("[data-testid='display-mobile']");
    private final By displayAddress = By.cssSelector("[data-testid='display-address']");
    private final By refreshProfile = By.cssSelector("[data-testid='refresh-profile']");
    private final By myOrders = By.cssSelector("[data-testid='my-orders']");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return wait.until(ExpectedConditions.textMatches(displayName, Pattern.compile(".+")));
    }

    public String getDisplayedName() {
        return text(displayName);
    }

    public String getDisplayedEmail() {
        return text(displayEmail);
    }

    public String getDisplayedMobile() {
        return text(displayMobile);
    }

    public String getDisplayedAddress() {
        return text(displayAddress);
    }

    public HomePage refreshProfile() {
        click(refreshProfile);
        return this;
    }

    public OrdersPage openOrders() {
        click(myOrders);
        return new OrdersPage(driver);
    }
}
