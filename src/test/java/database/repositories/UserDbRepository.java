package database.repositories;

import api.models.UserModel;
import database.DBUtils;
import database.queries.UserQueries;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserDbRepository {
    public Optional<UserModel> findByEmail(String email) {
        return mapFirst(DBUtils.query(UserQueries.FIND_BY_EMAIL, email));
    }

    public Optional<UserModel> findById(long id) {
        return mapFirst(DBUtils.query(UserQueries.FIND_BY_ID, id));
    }

    private Optional<UserModel> mapFirst(List<Map<String, Object>> rows) {
        return rows.stream().findFirst().map(this::mapUser);
    }

    private UserModel mapUser(Map<String, Object> row) {
        UserModel user = new UserModel();
        user.setId(((Number) row.get("id")).longValue());
        user.setName((String) row.get("name"));
        user.setEmail((String) row.get("email"));
        user.setPassword((String) row.get("password"));
        user.setMobile((String) row.get("mobile"));
        user.setAddress((String) row.get("address"));
        return user;
    }
}
