package servlet;

import util.DataSourceProvider;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class BasicDataSource {

    public Connection getConnection() throws SQLException {
        DataSourceProvider.setDbUrl("jdbc:hsqldb:file:${user.home}/data/jdbc/db;shutdown=true");
        DataSource dataSource = DataSourceProvider.getDataSource(); // max 3 connections
        return dataSource.getConnection();
    }

}
