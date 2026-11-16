package app.persistence.dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DataBaseConnection {
    private static DataBaseConnection instance;
    private final Connection connection;

    private DataBaseConnection() {
        try { connection = open(); }
        catch (SQLException e) { throw new RuntimeException("Error al conectar a la base de datos", e); }
    }

    public static synchronized DataBaseConnection getInstance() {
        if (instance == null) instance = new DataBaseConnection();
        return instance;
    }

    public Connection getConnection() { return connection; }

    public static Connection open() throws SQLException {
        String url = System.getenv().getOrDefault("CINE_DB_URL", "jdbc:mysql://localhost:3306/cine_cesde?serverTimezone=UTC");
        String user = System.getenv().getOrDefault("CINE_DB_USER", "root");
        String password = System.getenv().getOrDefault("CINE_DB_PASSWORD", "");
        return DriverManager.getConnection(url, user, password);
    }
}
