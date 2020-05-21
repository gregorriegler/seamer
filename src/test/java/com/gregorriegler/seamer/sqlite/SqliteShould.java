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

        Sqlite sqlite = new Sqlite("jdbc:sqlite::memory:");
        sqlite.createSchema();
        sqlite.executeUpdate("insert into seams values('expected seam name')");
        actualSeam = sqlite.getSeam();
        sqlite.close();

        assertThat(actualSeam).isEqualTo(expectedSeam);
    }

    public static class Sqlite {

        Connection connection;

        public Sqlite(String url) {
            this.connection = connect(url);
        }

        public static Connection connect(String url) {
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(url);
            } catch (SQLException e) {
                handleError(e);
            }
            return connection;
        }

        public void createSchema() {
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

        public void executeUpdate(String sql) {
            try {
                Statement statement = createStatement(this.connection);
                statement.executeUpdate(sql);
            } catch (SQLException e) {
                handleError(e);
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

        public String getSeam() {
            String resultAsString = "";
            try{
                Statement statement = createStatement(this.connection);
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
