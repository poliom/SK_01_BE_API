package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatabaseUtils {

    private static final Logger log = LogManager.getLogger(DatabaseUtils.class);

    private Connection connection;

    public void connect() throws SQLException, IOException {
        Properties props = new Properties();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (is == null) {
                throw new RuntimeException("db.properties not found in classpath");
            }
            props.load(is);
        }

        String driver = props.getProperty("db.driver");
        String url = props.getProperty("db.url");

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("DB driver not found: " + driver, e);
        }

        connection = buildConnection(url);
        log.info("DB connection established: {}", url.replaceAll(":[^:@]+@", ":***@"));
    }

    private static final Pattern LIBPQ_URL =
            Pattern.compile("^jdbc:postgresql://([^:]+):([^@]+)@(.+)$");

    private Connection buildConnection(String url) throws SQLException {
        // Handles libpq-style: jdbc:postgresql://user:password@host:port/db
        // Regex avoids URI.create() which rejects special characters in passwords
        Matcher m = LIBPQ_URL.matcher(url);
        if (m.matches()) {
            String user     = m.group(1);
            String password = m.group(2);
            String jdbcUrl  = "jdbc:postgresql://" + m.group(3);
            return DriverManager.getConnection(jdbcUrl, user, password);
        }
        return DriverManager.getConnection(url);
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                log.info("DB connection closed.");
            } catch (SQLException e) {
                log.error("Failed to close DB connection: {}", e.getMessage());
            }
            connection = null;
        }
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        log.info("Executing query: {}", sql);
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(sql);
    }

    public int executeUpdate(String sql) throws SQLException {
        log.info("Executing update: {}", sql);
        try (Statement stmt = connection.createStatement()) {
            int affected = stmt.executeUpdate(sql);
            log.info("Rows affected: {}", affected);
            return affected;
        }
    }
}
