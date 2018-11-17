package dao;

import model.Orders;
import model.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class OrderJPADao{
    @Autowired
    private JdbcTemplate template;


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
        String sql = "SELECT " +
                "COUNT(a.total) AS arv, " +
                "AVG(a.total) AS averageOrderAmount, " +
                "SUM(a.total) AS turnoverWithoutVAT, " +
                "SUM(a.total)*.2 AS turnoverVAT, " +
                "SUM(a.total)*1.2 AS turnoverWithVAT\n" +
                "FROM (SELECT orderId, SUM(quantity*price) AS total FROM orderrow GROUP BY orderId) AS a;";

        Report report = template.queryForObject(sql, (rs, rowNum) -> new Report(
                rs.getInt("arv"),
                rs.getInt("averageOrderAmount"),
                rs.getInt("turnoverWithoutVAT"),
                rs.getInt("turnoverVAT"),
                rs.getInt("turnoverWithVAT")
        ));

        return report;
    }

}
