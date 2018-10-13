package servlet;

import util.FileUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@WebListener()
public class Listener implements ServletContextListener {
    //public static String URL = "jdbc:hsqldb:file:${user.home}/data/jdbc/db;shutdown=true";
    public static String URL = "jdbc:hsqldb:mem:orders";

    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("contextInitialized");
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(FileUtil.readFileFromClasspath("schema.sql"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(FileUtil.readFileFromClasspath("destruct.sql"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
