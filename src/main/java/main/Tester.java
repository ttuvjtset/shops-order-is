package main;

import conf.DbConfig;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Tester {

    public static void main(String[] args) {

        ConfigurableApplicationContext ctx =
              new AnnotationConfigApplicationContext(DbConfig.class);

//        PersonDao dao = ctx.getBean(PersonDao.class);
//
//
//        Person jill = new Person("jill");
//        dao.save(new Person("dd"));
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