package com.example.app.repository;

import com.example.app.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> rowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setMobile(rs.getString("mobile"));
        user.setAddress(rs.getString("address"));
        return user;
    };

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User create(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO users(name, email, password, mobile, address) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getMobile());
            ps.setString(5, user.getAddress());
            return ps;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    public Optional<User> findById(long id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id = ?", rowMapper, id);
        return users.stream().findFirst();
    }

    public Optional<User> findByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email = ?", rowMapper, email);
        return users.stream().findFirst();
    }

    public User update(long id, User user) {
        jdbcTemplate.update(
                "UPDATE users SET name = ?, email = ?, password = ?, mobile = ?, address = ? WHERE id = ?",
                user.getName(), user.getEmail(), user.getPassword(), user.getMobile(), user.getAddress(), id
        );
        return findById(id).orElseThrow();
    }

    public User patch(long id, Map<String, Object> fields) {
        User existing = findById(id).orElseThrow();
        if (fields.containsKey("name")) {
            existing.setName(String.valueOf(fields.get("name")));
        }
        if (fields.containsKey("email")) {
            existing.setEmail(String.valueOf(fields.get("email")));
        }
        if (fields.containsKey("password")) {
            existing.setPassword(String.valueOf(fields.get("password")));
        }
        if (fields.containsKey("mobile")) {
            existing.setMobile(String.valueOf(fields.get("mobile")));
        }
        if (fields.containsKey("address")) {
            existing.setAddress(String.valueOf(fields.get("address")));
        }
        return update(id, existing);
    }

    public boolean delete(long id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id = ?", id) > 0;
    }
}
