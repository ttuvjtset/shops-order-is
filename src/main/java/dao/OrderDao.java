package dao;

import model.Order;
import model.Report;

import java.util.ArrayList;


public interface OrderDao {
    ArrayList<Order> getAllOrders();

    Order getOrderById(int parsedKey);

    Report getReport();

    void deleteOrderById(String id);

    void deleteAllOrders();

    Order getOrderRows(Order order);

    long saveOrderByPost(Order order);

    void saveOrderRows(Order order);
}
