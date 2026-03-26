package vn.edu.ute.quanlybanhang.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Đơn giản nhất: dùng DriverManager (phù hợp bài thực hành cơ bản).
 * Nếu muốn tối ưu, bạn có thể nâng cấp sang connection pool (HikariCP).
 */
public final class DBConnection {
    private static final String PROP_FILE = "db.properties";

    private static String url;
    private static String user;
    private static String password;

    static {
        loadConfig();
        // MySQL Connector/J 8+ tự động đăng ký driver, không cần Class.forName.
    }

    private DBConnection() {}

    private static void loadConfig() {
        Properties props = new Properties();
        try (InputStream is = DBConnection.class.getClassLoader().getResourceAsStream(PROP_FILE)) {
            if (is == null) {
                throw new IllegalStateException("Không tìm thấy " + PROP_FILE + " trong resources.");
            }
            props.load(is);
            url = props.getProperty("db.url");
            user = props.getProperty("db.user");
            password = props.getProperty("db.password");
        } catch (IOException e) {
            throw new RuntimeException("Lỗi đọc cấu hình DB: " + e.getMessage(), e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
