package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnectionManager {
    private static String url;
    private static String username;
    private static String password;

    private DBConnectionManager() {
    }

    public static void configure(String dbUrl, String dbUsername, String dbPassword) {
        url = dbUrl;
        username = dbUsername;
        password = dbPassword;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
