package database.repositories;

import java.math.BigDecimal;

public class OrderDbRecord {
    private final long orderId;
    private final long userId;
    private final BigDecimal totalAmount;
    private final int productCount;

    public OrderDbRecord(long orderId, long userId, BigDecimal totalAmount, int productCount) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.productCount = productCount;
    }

    public long getOrderId() {
        return orderId;
    }

    public long getUserId() {
        return userId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public int getProductCount() {
        return productCount;
    }
}
