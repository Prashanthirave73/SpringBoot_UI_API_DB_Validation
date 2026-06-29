package framework;

import database.DBConnectionManager;
import io.restassured.RestAssured;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;

import java.time.Duration;

public abstract class BaseTest {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    @BeforeSuite(alwaysRun = true)
    public void suiteSetup() {
        ConfigManager.load();
        RestAssured.baseURI = ConfigManager.get("base.url") + ConfigManager.get("api.base.path");
        DBConnectionManager.configure(
                ConfigManager.get("db.url"),
                ConfigManager.get("db.username"),
                ConfigManager.get("db.password")
        );
    }

    protected WebDriver getDriver() {
        if (DRIVER.get() == null) {
            DRIVER.set(createDriver());
        }
        return DRIVER.get();
    }

    private WebDriver createDriver() {
        String browser = ConfigManager.get("browser").toLowerCase();
        boolean headless = ConfigManager.getBoolean("headless");
        WebDriver driver;
        switch (browser) {
            case "firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                if (headless) {
                    options.addArguments("-headless");
                }
                driver = new FirefoxDriver(options);
            }
            case "edge" -> {
                EdgeOptions options = new EdgeOptions();
                if (headless) {
                    options.addArguments("--headless=new");
                }
                driver = new EdgeDriver(options);
            }
            default -> {
                ChromeOptions options = new ChromeOptions();
                if (headless) {
                    options.addArguments("--headless=new");
                }
                options.addArguments("--window-size=1440,1000");
                driver = new ChromeDriver(options);
            }
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        return driver;
    }

    @AfterMethod(alwaysRun = true)
    public void closeBrowser() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }
}
