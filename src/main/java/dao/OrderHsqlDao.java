package dao;

import model.Order;
import model.OrderAndRowCombined;
import model.OrderRow;
import model.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Primary
@Repository
public class OrderHsqlDao implements OrderDao {

    @Autowired
    private JdbcTemplate template;

    @Override
    public List<Order> getAllOrders() {
        String sql = "SELECT id, orderNumber, itemName, quantity, price " +
                "FROM orders LEFT JOIN orderrow ON orderrow.orderId = orders.id;";

        List<OrderAndRowCombined> orderAndRowCombined = template.query(sql, (rs, rowNum) -> new OrderAndRowCombined(
                rs.getString("id"),
                rs.getString("orderNumber"),
                rs.getString("itemName"),
                rs.getInt("quantity"),
                rs.getInt("price")
        ));

        List<Order> orders = new ArrayList<>();

        for (OrderAndRowCombined combined : orderAndRowCombined) {
            Optional<Order> orderInList = orders.stream().filter(s -> s.getId().equals(combined.getId())).findFirst();

            OrderRow orderRow = null;

            if (!(combined.getItemName() == null && combined.getQuantity() == 0 && combined.getPrice() == 0)) {
                orderRow = new OrderRow(combined.getItemName(), combined.getQuantity(), combined.getPrice());
            }

            if (!orderInList.isPresent()) {
                ArrayList<OrderRow> orderRowList = new ArrayList<>();
                if (orderRow != null) orderRowList.add(orderRow);
                Order order = new Order(combined.getId(), combined.getOrderNumber(), orderRowList);
                orders.add(order);
            } else {
                Order order = orderInList.get();
                if (orderRow != null) order.getOrderRows().add(orderRow);
            }
        }

        return orders;
    }

    @Override
    public Order getOrderById(int parsedKey) {
        Optional<Order> orderOptional = getAllOrders().stream()
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

        List<Report> reports = template.query(sql, (rs, rowNum) -> new Report(
                rs.getInt("arv"),
                rs.getInt("averageOrderAmount"),
                rs.getInt("turnoverWithoutVAT"),
                rs.getInt("turnoverVAT"),
                rs.getInt("turnoverWithVAT")
        ));

        return reports.get(0);
    }

    @Override
    public void deleteOrderById(String id) {
        String sqlOrders = "DELETE FROM orders WHERE id=?";
        String sqlOrderRow = "DELETE FROM orderrow WHERE orderId=?";

        template.update(sqlOrders, id);
        template.update(sqlOrderRow, id);
    }

    @Override
    public void deleteAllOrders() {
        String sqlOrders = "DELETE FROM orders";
        String sqlOrderRow = "DELETE FROM orderrow";

        template.update(sqlOrders);
        template.update(sqlOrderRow);
    }

    @Override
    public Order saveOrderByPost(Order order) {
        String sql = "INSERT INTO orders (id, orderNumber, orderRows) " +
                "VALUES (NEXT VALUE FOR seq1, ?, null);";

        GeneratedKeyHolder holder = new GeneratedKeyHolder();

        template.update(conn -> {
            PreparedStatement preparedStatement = conn.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, order.getOrderNumber());
            return preparedStatement;
        }, holder);

        order.setId(String.valueOf(holder.getKey().longValue()));

        if (order != null && order.getOrderRows() != null) {
            for (OrderRow orderRow : order.getOrderRows()) {
                String sql2 = "INSERT INTO orderrow (orderId, itemName, quantity, price) " +
                        "VALUES (?, ?, ?, ?);";
                template.update(conn -> {
                    PreparedStatement ps = conn.prepareStatement(sql2);
                    ps.setString(1, order.getId());
                    ps.setString(2, orderRow.getItemName());
                    ps.setInt(3, orderRow.getQuantity());
                    ps.setInt(4, orderRow.getPrice());
                    return ps;
                });
            }
        }

        return order;
    }
}
