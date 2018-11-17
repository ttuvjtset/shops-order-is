package dao;

import model.Orders;
import model.Report;

import java.util.List;


public interface OrderDao {
    List<Orders> getAllOrders();

    Orders getOrderById(long parsedKey);

    Report getReport();

    void deleteOrderById(long id);

    void deleteAllOrders();

    Orders saveOrderByPost(Orders orders);
}
