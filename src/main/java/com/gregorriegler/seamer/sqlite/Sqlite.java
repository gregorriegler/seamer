package com.gregorriegler.seamer.sqlite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
            handleError(e);
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
            handleError(e);
        }
    }

    public Optional<byte[]> queryBytes(String query, String param) {
        return queryOne(query, param, byte[].class);
    }

    public <T> Optional<T> queryOne(String query, String param, Class<T> clazz) {
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, param);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                byte[] bytes = resultSet.getBytes(1);
                return Optional.of((T) bytes);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            handleError(e);
            return Optional.empty();
        }
    }

    public void close() {
        try {
            if (this.connection != null)
                this.connection.close();
        } catch (SQLException e) {
            handleError(e);
        }
    }

    private Statement createStatement() throws SQLException {
        Statement statement = this.connection.createStatement();
        statement.setQueryTimeout(30);
        return statement;
    }

    private Connection connect(String url) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            handleError(e);
        }
        return connection;
    }

    private void handleError(SQLException e) {
        LOG.warn("an error happened interacting with sqlite", e);
    }
}
