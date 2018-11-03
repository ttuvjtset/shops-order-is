package test;

import model.Order;
import model.OrderRow;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        OrderRow orderRow40 = new OrderRow("tt", 1, 40);
        OrderRow orderRow60 = new OrderRow("tt", 1, 60);
        ArrayList<OrderRow> orderRows = new ArrayList<>();
        orderRows.add(orderRow40);
        orderRows.add(orderRow60);

        Order order = new Order(null, "A15", orderRows);
        OrderDao orderDao = new OrderDao();
        orderDao.saveOrderByPost(order);
    }
}
