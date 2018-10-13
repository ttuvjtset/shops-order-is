package test;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener()
public class Listener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        System.out.println(1111);
    }

    public void contextDestroyed(ServletContextEvent sce) {

    }
}
