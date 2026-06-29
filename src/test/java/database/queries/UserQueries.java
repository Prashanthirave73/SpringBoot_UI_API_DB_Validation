package database.queries;

public final class UserQueries {
    public static final String FIND_BY_EMAIL = "SELECT * FROM users WHERE email = ?";
    public static final String FIND_BY_ID = "SELECT * FROM users WHERE id = ?";

    private UserQueries() {
    }
}
