package database.repositories;

import database.DBUtils;
import database.queries.OrderQueries;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OrderDbRepository {
    public Optional<OrderDbRecord> findByOrderId(long orderId) {
        List<Map<String, Object>> rows = DBUtils.query(OrderQueries.FIND_ORDER, orderId);
        return rows.stream().findFirst().map(row -> new OrderDbRecord(
                ((Number) row.get("order_id")).longValue(),
                ((Number) row.get("user_id")).longValue(),
                (BigDecimal) row.get("total_amount"),
                ((Number) row.get("product_count")).intValue()
        ));
    }
}
