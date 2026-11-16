package app.persistence.repository;

import app.persistence.mapper.RowMapper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class JdbcSupport {
    protected final Connection connection;
    JdbcSupport(Connection connection) { this.connection = connection; }
    protected int execute(String sql, Object... values) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) statement.setObject(i + 1, values[i]);
            return statement.executeUpdate();
        }
    }
    protected <T> List<T> query(String sql, RowMapper<T> mapper, Object... values) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) statement.setObject(i + 1, values[i]);
            try (ResultSet result = statement.executeQuery()) {
                List<T> rows = new ArrayList<>();
                while (result.next()) rows.add(mapper.mapRow(result));
                return rows;
            }
        }
    }
    protected RuntimeException failure(SQLException e) { return new RuntimeException("Error de base de datos: " + e.getMessage(), e); }
    protected <T> T first(List<T> rows) { return rows.isEmpty() ? null : rows.get(0); }
    @FunctionalInterface protected interface Work<T> { T run() throws SQLException; }
    protected <T> T transaction(Work<T> work) {
        try {
            boolean autoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            try {
                T result = work.run();
                connection.commit();
                return result;
            } catch (SQLException | RuntimeException e) {
                try { connection.rollback(); } catch (SQLException rollback) { e.addSuppressed(rollback); }
                throw e;
            } finally { connection.setAutoCommit(autoCommit); }
        } catch (SQLException e) { throw failure(e); }
    }
}
