package dao;

import model.OrderRow;
import model.Orders;
import model.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.Order;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Repository
public class OrderDaoJPA {
//    @Autowired
//    private JdbcTemplate template;

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Orders saveOrderByPost(Orders orders) {
        em.persist(orders);
        return orders;
    }

    public List<Orders> getAllOrders() {
        return em.createQuery("select p from Orders p", Orders.class).getResultList();
    }

    public Orders getOrderById(long id) {
        TypedQuery<Orders> query = em.createQuery("select o from Orders AS o WHERE o.id=:id", Orders.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Transactional
    public void deleteOrderById(long id) {
        Query query = em.createQuery("DELETE FROM Orders AS o where o.id=:id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Transactional
    public void deleteAllOrders() {
        em.createQuery("DELETE FROM Orders").executeUpdate();
    }

    public Report getReport() {
        List<Orders> allOrders = getAllOrders();
        List<Integer> priceQuantityProducts = new ArrayList<>();
        int turnoverWithoutVAT = 0;

        for (Orders allOrder : allOrders) {
            int priceQuantityProduct = 0;
            for (OrderRow order : allOrder.getOrderRows()) {
                priceQuantityProduct += order.getPrice() * order.getQuantity();
            }
            priceQuantityProducts.add(priceQuantityProduct);
            turnoverWithoutVAT += priceQuantityProduct;
        }

        int ordersCount = priceQuantityProducts.size();

        int averageOrderAmount = 0;
        OptionalDouble averageOrderAmountOptional = priceQuantityProducts.stream().mapToInt(Integer::intValue).average();
        if (averageOrderAmountOptional.isPresent()) {
            averageOrderAmount = (int) averageOrderAmountOptional.getAsDouble();
        }

        int turnoverVAT = (int) (turnoverWithoutVAT * 0.2);
        int turnoverWithVAT = (int) (turnoverWithoutVAT * 1.2);

        return new Report(ordersCount, averageOrderAmount, turnoverWithoutVAT, turnoverVAT, turnoverWithVAT);
    }

}
