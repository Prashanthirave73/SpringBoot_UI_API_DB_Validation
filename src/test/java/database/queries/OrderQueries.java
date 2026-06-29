package database.queries;

public final class OrderQueries {
    public static final String FIND_ORDER = """
            SELECT o.order_id, o.user_id, o.total_amount, COUNT(oi.id) AS product_count
            FROM orders o
            LEFT JOIN order_items oi ON oi.order_id = o.order_id
            WHERE o.order_id = ?
            GROUP BY o.order_id, o.user_id, o.total_amount
            """;

    private OrderQueries() {
    }
}
