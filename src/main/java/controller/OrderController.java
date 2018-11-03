package controller;

import dao.OrderDao;
import model.Order;
import model.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class OrderController {

    @Autowired
    private OrderDao dao;

    @GetMapping("orders")
    public List<Order> getAllOrders() {
        return dao.getAllOrders();
    }

    @GetMapping("orders/{orderId}")
    public Order getOrderById(@PathVariable String orderId) {
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
    public Order saveOrderByPost(@RequestBody @Valid Order order) {
        return dao.saveOrderByPost(order);
    }

//    @PostMapping("orders/form")
//    public Order saveOrderByForm(@RequestParam Map<String, String> body) {
//        Order order = new Order();
//        order.setOrderNumber(body.get("orderNumber"));
//        return dao.saveOrderByPost(order);
//    }
}