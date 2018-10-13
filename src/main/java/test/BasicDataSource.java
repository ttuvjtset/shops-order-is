package test;

import util.DataSourceProvider;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

class BasicDataSource {

    Connection getConnection() throws SQLException {
        DataSourceProvider.setDbUrl("jdbc:hsqldb:mem:orders");
        DataSource dataSource = DataSourceProvider.getDataSource();
        return dataSource.getConnection();
    }

}
