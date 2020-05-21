package com.gregorriegler.seamer.sqlite;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

public class SqliteShould {

    @Test
    void create_a_sqlite_db() {
        String expectedSeam = "expected seam name";
        String actualSeam = "";

        Connection connection = createSqliteConnection("jdbc:sqlite::memory:");

        createSchema(connection);

        try {
            Statement statement = createStatement(connection);
            statement.executeUpdate("insert into seams values('expected seam name')");
        } catch (SQLException e) {
            handleError(e);
        }

        try{
            Statement statement = createStatement(connection);
            ResultSet result = statement.executeQuery("select * from seams");
            while (result.next()) {
                actualSeam = result.getString("name");
            }
        } catch (SQLException e) {
            handleError(e);
        }

        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            handleError(e);
        }

        assertThat(actualSeam).isEqualTo(expectedSeam);
    }

    private Connection createSqliteConnection(String url) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            handleError(e);
        }
        return connection;
    }

    private void createSchema(Connection connection) {
        try {
            Statement statement = createStatement(connection);
            statement.executeUpdate("drop table if exists seams");
            statement.executeUpdate("create table seams (name string)");
        } catch (SQLException e) {
            handleError(e);
        }
    }

    private Statement createStatement(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);
        return statement;
    }

    private void handleError(SQLException e) {
        System.err.println(e.getMessage());
    }
}
