package main;

import conf.DbConfig;
import dao.OrderDaoJPA;
import model.OrderRow;
import model.Orders;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Tester {

    public static void main(String[] args) {

        ConfigurableApplicationContext ctx =
              new AnnotationConfigApplicationContext(DbConfig.class);

        OrderDaoJPA dao = ctx.getBean(OrderDaoJPA.class);

        dao.saveOrderByPost(new Orders("A52"));

        Orders order2 = new Orders("order2");
        order2.getOrderRows().add(new OrderRow("gg",2,100));
        order2.getOrderRows().add(new OrderRow("zz",2,100));

        dao.saveOrderByPost(order2);

        System.out.println(dao.getAllOrders());
        //dao.deleteOrderById(1L);
        System.out.println(dao.getOrderById(2));
        System.out.println(dao.getAllOrders());

        ctx.close();
    }
}