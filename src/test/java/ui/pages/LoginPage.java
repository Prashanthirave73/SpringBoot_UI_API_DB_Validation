package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
    private final By emailInput = By.cssSelector("[data-testid='email']");
    private final By passwordInput = By.cssSelector("[data-testid='password']");
    private final By loginButton = By.cssSelector("[data-testid='login-button']");
    private final By loginMessage = By.id("login-message");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open(String baseUrl) {
        driver.get(baseUrl);
        return this;
    }

    public HomePage login(String email, String password) {
        type(emailInput, email);
        type(passwordInput, password);
        click(loginButton);
        return new HomePage(driver);
    }

    public String loginExpectingFailure(String email, String password) {
        type(emailInput, email);
        type(passwordInput, password);
        click(loginButton);
        return text(loginMessage);
    }
}
