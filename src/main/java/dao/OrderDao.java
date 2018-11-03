package dao;

import model.Order;
import model.Report;

import java.util.List;


public interface OrderDao {
    List<Order> getAllOrders();

    Order getOrderById(int parsedKey);
//
    Report getReport();
//
    void deleteOrderById(String id);

    void deleteAllOrders();

    Order saveOrderByPost(Order order);

}
