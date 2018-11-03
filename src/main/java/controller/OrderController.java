package controller;

import dao.OrderHsqlDao;
import model.Order;
import model.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
public class OrderController {

    @Autowired
    private OrderHsqlDao dao;

//    @GetMapping("posts")
//    public List<Post> getPosts() {
//        return dao.findAll();
//    }
//
//    @PostMapping("posts")
//    public void save(@RequestBody @Valid Post post) {
//        dao.save(post);
//    }
//
//    @DeleteMapping("posts/{postId}")
//    public void deletePost(@PathVariable("postId") Long postId) {
//        dao.delete(postId);
//    }



    @GetMapping("orders")
    public ArrayList<Order> getAllOrders() {
        return dao.getAllOrders();
    }

    @GetMapping("orders/{orderId}")
    public Order getOrderById(@PathVariable String orderId) {
        return dao.getOrderById(Integer.valueOf(orderId));
    }

    @GetMapping("/orders/report")
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
    public void saveOrderByPost(@RequestBody @Valid Order order) {
        dao.saveOrderByPost(order); //!!!! long???
    }

    @PostMapping("orders")
    public void saveOrderRows(Order order) {

    }

}