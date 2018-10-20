package test;

import util.FileUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@WebListener()
public class Listener implements ServletContextListener {
    private DataSourceBasic dataSourceBasic;

    public Listener() {
        dataSourceBasic = new DataSourceBasic();
    }

    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("contextInitialized");
        try (Connection conn = dataSourceBasic.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(FileUtil.readFileFromClasspath("schema.sql"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {

    }
}
