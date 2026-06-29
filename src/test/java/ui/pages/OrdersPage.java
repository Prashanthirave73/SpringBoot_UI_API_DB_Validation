package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class OrdersPage extends BasePage {
    private final By firstOrderId = By.cssSelector("[data-testid='order-id']");
    private final By firstOrderProducts = By.cssSelector("[data-testid='order-products']");
    private final By firstOrderTotal = By.cssSelector("[data-testid='order-total']");

    public OrdersPage(WebDriver driver) {
        super(driver);
    }

    public String getFirstOrderId() {
        return text(firstOrderId);
    }

    public String getFirstOrderProducts() {
        return text(firstOrderProducts);
    }

    public String getFirstOrderTotal() {
        return text(firstOrderTotal);
    }
}
