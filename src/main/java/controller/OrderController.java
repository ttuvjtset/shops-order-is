package controller;

import dao.OrderDao;
import model.Orders;
import model.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private OrderDao dao;

    @GetMapping("orders")
    public List<Orders> getAllOrders() {
        return dao.getAllOrders();
    }

    @GetMapping("orders/{orderId}")
    public Orders getOrderById(@PathVariable String orderId) {
        return dao.getOrderById(Integer.valueOf(orderId));
    }

    @GetMapping("orders/report")
    public Report getReport() {
        return dao.getReport();
    }

    @DeleteMapping("orders/{orderId}")
    public void deleteOrderById(@PathVariable String orderId) {
        dao.deleteOrderById(orderId);
    }

    @DeleteMapping("orders")
    public void deleteAllOrders() {
        dao.deleteAllOrders();
    }

    @PostMapping("orders")
    public Orders saveOrderByPost(@RequestBody @Valid Orders orders) {
        return dao.saveOrderByPost(orders);
    }

    @PostMapping("orders/form")
    public Orders saveOrderByForm(@RequestParam String orderNumber) {
        // no validation!
        // not the right url -> api/orders/form
        Orders orders = new Orders();
        orders.setOrderNumber(orderNumber);
        return dao.saveOrderByPost(orders);
    }
}