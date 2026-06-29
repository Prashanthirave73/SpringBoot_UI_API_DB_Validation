package com.example.app.repository;

import com.example.app.model.Order;
import com.example.app.model.OrderItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Order> orderRowMapper = (rs, rowNum) -> {
        Order order = new Order();
        order.setOrderId(rs.getLong("order_id"));
        order.setUserId(rs.getLong("user_id"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        return order;
    };

    private final RowMapper<OrderItem> itemRowMapper = (rs, rowNum) -> {
        OrderItem item = new OrderItem();
        item.setId(rs.getLong("id"));
        item.setOrderId(rs.getLong("order_id"));
        item.setProductName(rs.getString("product_name"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnitPrice(rs.getBigDecimal("unit_price"));
        return item;
    };

    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public Order create(Order order) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO orders(user_id, total_amount) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setLong(1, order.getUserId());
            ps.setBigDecimal(2, order.getTotalAmount());
            return ps;
        }, keyHolder);
        long orderId = keyHolder.getKey().longValue();
        order.setOrderId(orderId);

        for (OrderItem item : order.getItems()) {
            jdbcTemplate.update(
                    "INSERT INTO order_items(order_id, product_name, quantity, unit_price) VALUES (?, ?, ?, ?)",
                    orderId, item.getProductName(), item.getQuantity(), item.getUnitPrice()
            );
        }
        return findById(orderId).orElseThrow();
    }

    public Optional<Order> findById(long orderId) {
        List<Order> orders = jdbcTemplate.query("SELECT * FROM orders WHERE order_id = ?", orderRowMapper, orderId);
        orders.forEach(this::attachItems);
        return orders.stream().findFirst();
    }

    public List<Order> findByUserId(long userId) {
        List<Order> orders = jdbcTemplate.query("SELECT * FROM orders WHERE user_id = ? ORDER BY order_id DESC", orderRowMapper, userId);
        orders.forEach(this::attachItems);
        return orders;
    }

    private void attachItems(Order order) {
        List<OrderItem> items = jdbcTemplate.query("SELECT * FROM order_items WHERE order_id = ?", itemRowMapper, order.getOrderId());
        order.setItems(items);
    }
}
