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

        Connection connection = Sqlite.connect("jdbc:sqlite::memory:");

        Sqlite.createSchema(connection);

        String sql = "insert into seams values('expected seam name')";
        Sqlite.executeUpdate(connection, sql);

        actualSeam = Sqlite.getSeam(connection);

        Sqlite.close(connection);

        assertThat(actualSeam).isEqualTo(expectedSeam);
    }

    public static class Sqlite {

        public static Connection connect(String url) {
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(url);
            } catch (SQLException e) {
                handleError(e);
            }
            return connection;
        }

        public static void createSchema(Connection connection) {
            try {
                Statement statement = createStatement(connection);
                statement.executeUpdate("drop table if exists seams");
                statement.executeUpdate("create table seams (name string)");
            } catch (SQLException e) {
                handleError(e);
            }
        }

        public static Statement createStatement(Connection connection) throws SQLException {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            return statement;
        }

        public static void handleError(SQLException e) {
            System.err.println(e.getMessage());
        }

        public static void executeUpdate(Connection connection, String sql) {
            try {
                Statement statement = createStatement(connection);
                statement.executeUpdate(sql);
            } catch (SQLException e) {
                handleError(e);
            }
        }

        public static void close(Connection connection) {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                handleError(e);
            }
        }

        public static String getSeam(Connection connection) {
            String resultAsString = "";
            try{
                Statement statement = createStatement(connection);
                ResultSet result = statement.executeQuery("select * from seams");
                while (result.next()) {
                    resultAsString = result.getString("name");
                }
            } catch (SQLException e) {
                handleError(e);
            }
            return resultAsString;
        }
    }
}
