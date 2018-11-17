package main;

import conf.DbConfig;
import dao.OrderJPADao;
import model.OrderRow;
import model.Orders;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Tester {

    public static void main(String[] args) {

        ConfigurableApplicationContext ctx =
              new AnnotationConfigApplicationContext(DbConfig.class);

        OrderJPADao dao = ctx.getBean(OrderJPADao.class);

       // Orders orders = new Orders("A52");
        Orders dd = new Orders("dd");
        dd.getOrderRows().add(new OrderRow("gg",2,100));
        dd.getOrderRows().add(new OrderRow("zz",2,100));
        dao.saveOrderByPost(dd);
        System.out.println(dao.findAll());
        dao.deleteOrderById(1L);
        System.out.println(dao.findAll());



//        PersonDao dao = ctx.getBean(PersonDao.class);
//
//
//        Person jill = new Person("jill");

//
//        Address jillAddress = new Address("kase 2");
//     //   dao.saveAddress(jillAddress);
//        jill.setAddress(jillAddress);
//        jill.getPhones().add(new Phone("222"));
//        dao.save(jill);
//
//        System.out.println(dao.findAll());
//
//        Person person = dao.findPersonByName("jill");
//
//        System.out.println(person);
//
//        person.setName("jane");
//
//        dao.save(person);
//
//        System.out.println(dao.findAll());


        ctx.close();
    }
}