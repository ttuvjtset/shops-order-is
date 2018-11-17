package dao;

import model.Orders;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class OrderJPADao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Orders saveOrderByPost(Orders orders) {
        em.persist(orders);

        return orders;
    }

    public List<Orders> findAll() {
        return em.createQuery("select p from Orders p", Orders.class).getResultList();
    }

    @Transactional
    public void deleteOrderById(long id) {
        Query query = em.createQuery("DELETE FROM Orders AS o where o.id=:id");
        query.setParameter("id", id);
        query.executeUpdate();

//        Query query = Orders.entityManager().createQuery(
//                "DELETE FROM Seller AS o WHERE o.company=:company AND o.id=:id");
//        query.setParameter("company", company);
//        query.setParameter("id", id);
//        int result = query.executeUpdate();
    }


    public void deleteAllOrders() {
        em.createQuery("DELETE FROM Orders").executeUpdate();
    }

/*

    @Override
    public List<Orders> getAllOrders() {
        String sql = "SELECT id, orderNumber, itemName, quantity, price " +
                "FROM orders LEFT JOIN orderrow ON orderrow.orderId = orders.id;";

        List<OrderAndRowCombined> orderAndRowCombined = template.query(sql, (rs, rowNum) -> new OrderAndRowCombined(
                rs.getString("id"),
                rs.getString("orderNumber"),
                rs.getString("itemName"),
                rs.getInt("quantity"),
                rs.getInt("price")
        ));

        template.query(sql, (rs, rowNum) -> new OrderRow(
                rs.getString("itemName"),
                rs.getInt("quantity"),
                rs.getInt("price")
        ));

        List<Orders> orders = new ArrayList<>();

        for (OrderAndRowCombined combined : orderAndRowCombined) {
            Optional<Orders> orderInList = orders.stream().filter(s -> s.getId().equals(combined.getId())).findFirst();

            OrderRow orderRow = null;

            if (!(combined.getItemName() == null && combined.getQuantity() == 0 && combined.getPrice() == 0)) {
                orderRow = new OrderRow(combined.getItemName(), combined.getQuantity(), combined.getPrice());
            }

            if (!orderInList.isPresent()) {
                ArrayList<OrderRow> orderRowList = new ArrayList<>();
                if (orderRow != null) orderRowList.add(orderRow);
                Orders order = new Orders(combined.getId(), combined.getOrderNumber(), orderRowList);
                orders.add(order);
            } else {
                Orders order = orderInList.get();
                if (orderRow != null) order.getOrderRows().add(orderRow);
            }
        }

        return orders;
    }

    @Override
    public Orders getOrderById(int parsedKey) {
        Optional<Orders> orderOptional = getAllOrders().stream()
                .filter(s -> s.getId().equals(String.valueOf(parsedKey))).findFirst();
        return orderOptional.orElse(null);
    }

    @Override
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

*/

//
//    @Override
//    public Orders saveOrderByPost(Orders order) {
//        String sql = "INSERT INTO orders (id, orderNumber, orderRows) " +
//                "VALUES (NEXT VALUE FOR seq1, ?, null);";
//
//        GeneratedKeyHolder holder = new GeneratedKeyHolder();
//
//        template.update(conn -> {
//            PreparedStatement preparedStatement = conn.prepareStatement(sql, new String[]{"id"});
//            preparedStatement.setString(1, order.getOrderNumber());
//            return preparedStatement;
//        }, holder);
//
//        order.setId(String.valueOf(holder.getKey().longValue()));
//
//        if (order != null && order.getOrderRows() != null) {
//            for (OrderRow orderRow : order.getOrderRows()) {
//                String sql2 = "INSERT INTO orderrow (orderId, itemName, quantity, price) " +
//                        "VALUES (?, ?, ?, ?);";
//                template.update(conn -> {
//                    PreparedStatement ps = conn.prepareStatement(sql2);
//                    ps.setString(1, order.getId());
//                    ps.setString(2, orderRow.getItemName());
//                    ps.setInt(3, orderRow.getQuantity());
//                    ps.setInt(4, orderRow.getPrice());
//                    return ps;
//                });
//            }
//        }
//
//        return order;
//    }

}
