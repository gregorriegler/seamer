package com.gregorriegler.seamer.sqlite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Sqlite {
    private static final Logger LOG = LoggerFactory.getLogger(Sqlite.class);

    private final Connection connection;

    public Sqlite(String url) {
        this.connection = connect(url);
    }

    public void command(String... commands) {
        try {
            Statement statement = createStatement();
            for (String sql : commands) {
                statement.executeUpdate(sql);
            }
        } catch (SQLException e) {
            throw new SqliteException("failed to execute sql command", e);
        }
    }

    public void parameterizedCommand(String command, Object... params) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(command);
            int i = 1;
            for (Object param : params) {
                preparedStatement.setObject(i, param);
                i++;
            }

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SqliteException("failed to execute sql command: " + command, e);
        }
    }

    public Optional<byte[]> queryBytes(String query, String param) {
        return queryOne(query, param);
    }

    public List<byte[]> queryListOfBytes(String query, String param) {
        return queryList(query, param);
    }

    private <T> Optional<T> queryOne(String query, String param) {
        List<T> result = queryList(query, param);
        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(result.get(0));
        }
    }

    private <T> List<T> queryList(String query, String param) {
        try {
            List<T> result = new ArrayList<>();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, param);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                T object = (T) resultSet.getObject(1);
                result.add(object);
            }
            return result;
        } catch (SQLException e) {
            throw new SqliteException("failed to query sql", e);
        }
    }

    public void close() {
        try {
            if (this.connection != null) {
                this.connection.close();
            }
        } catch (SQLException e) {
            logWarn(e);
        }
    }

    private Statement createStatement() throws SQLException {
        Statement statement = this.connection.createStatement();
        statement.setQueryTimeout(30);
        return statement;
    }

    private Connection connect(String url) {
        try {
            Connection connection = DriverManager.getConnection(url);
            return connection;
        } catch (SQLException e) {
            throw new SqliteException("failed to connect to sqlite " + url, e);
        }
    }

    private void logWarn(SQLException e) {
        LOG.warn("an error happened interacting with sqlite", e);
    }

    public static class SqliteException extends RuntimeException {
        public SqliteException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
