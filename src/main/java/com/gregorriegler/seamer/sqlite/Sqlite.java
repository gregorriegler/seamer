package com.gregorriegler.seamer.sqlite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Sqlite {
    private static final Logger LOG = LoggerFactory.getLogger(Sqlite.class);

    Connection connection;

    public Sqlite(String url) {
        this.connection = connect(url);
    }

    public void createSchema() {
        try {
            Statement statement = createStatement();
            statement.executeUpdate("drop table if exists seams");
            statement.executeUpdate("create table seams (name string)");
        } catch (SQLException e) {
            handleError(e);
        }
    }

    public void executeUpdate(String sql) {
        try {
            Statement statement = createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            handleError(e);
        }
    }

    public String getSeam() {
        String resultAsString = "";
        try{
            Statement statement = createStatement();
            ResultSet result = statement.executeQuery("select * from seams");
            while (result.next()) {
                resultAsString = result.getString("name");
            }
        } catch (SQLException e) {
            handleError(e);
        }
        return resultAsString;
    }

    public Statement createStatement() throws SQLException {
        Statement statement = this.connection.createStatement();
        statement.setQueryTimeout(30);
        return statement;
    }

    public void close() {
        try {
            if (this.connection != null)
                this.connection.close();
        } catch (SQLException e) {
            handleError(e);
        }
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