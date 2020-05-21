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

        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            statement.executeUpdate("drop table if exists seams");
            statement.executeUpdate("create table seams (name string)");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            statement.executeUpdate("insert into seams values('expected seam name')");
            ResultSet result = statement.executeQuery("select * from seams");
            while (result.next()) {
                actualSeam = result.getString("name");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        assertThat(actualSeam).isEqualTo(expectedSeam);
    }

    private Connection createSqliteConnection(String url) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return connection;
    }
}
