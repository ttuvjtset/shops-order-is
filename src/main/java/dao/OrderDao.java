package dao;

import model.Orders;
import model.Report;

import java.util.List;


public interface OrderDao {
    List<Orders> getAllOrders();

    Orders getOrderById(int parsedKey);

    Report getReport();

    void deleteOrderById(String id);

    void deleteAllOrders();

    Orders saveOrderByPost(Orders orders);
}
