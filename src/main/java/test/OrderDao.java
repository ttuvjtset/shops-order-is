package test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class OrderDao {
    private DataSourceBasic dataSourceBasic;

    OrderDao() {
        dataSourceBasic = new DataSourceBasic();
    }

    ArrayList<Order> getAllOrders() {
        ArrayList<Order> orders = new ArrayList<>();

        String sql = "SELECT id, orderNumber, itemName, quantity, price " +
                "FROM orders LEFT JOIN orderrow ON orderrow.orderId = orders.id;";

        try (Connection conn = dataSourceBasic.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString("id");
                String orderNumber = rs.getString("orderNumber");
                String itemName = rs.getString("itemName");
                int quantity = rs.getInt("quantity");
                int price = rs.getInt("price");

                Optional<Order> orderInList = orders.stream().filter(s -> s.getId().equals(id)).findFirst();

                OrderRow orderRow = null;

                if (!(itemName == null && quantity == 0 && price == 0)) {
                    orderRow = new OrderRow(itemName, quantity, price);
                }

                if (!orderInList.isPresent()) {
                    ArrayList<OrderRow> orderRowList = new ArrayList<>();
                    if (orderRow != null) orderRowList.add(orderRow);
                    Order order = new Order(id, orderNumber, orderRowList);
                    orders.add(order);
                } else {
                    Order order = orderInList.get();
                    if (orderRow != null) order.getOrderRows().add(orderRow);
                }
            }

            return orders;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    Order getOrderById(int parsedKey) {
        Optional<Order> orderOptional = getAllOrders().stream()
                .filter(s -> s.getId().equals(String.valueOf(parsedKey))).findFirst();
        return orderOptional.orElse(null);
    }

    Report getReport() {

        String sql = "SELECT " +
                "COUNT(a.total) AS arv, " +
                "AVG(a.total) AS averageOrderAmount, " +
                "SUM(a.total) AS turnoverWithoutVAT, " +
                "SUM(a.total)*.2 AS turnoverVAT, " +
                "SUM(a.total)*1.2 AS turnoverWithVAT\n" +
                "FROM (SELECT orderId, SUM(quantity*price) AS total FROM orderrow GROUP BY orderId) AS a;";

        Report report = null;

        try (Connection conn = dataSourceBasic.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int arv = rs.getInt("arv");
                int averageOrderAmount = rs.getInt("averageOrderAmount");
                int turnoverWithoutVAT = rs.getInt("turnoverWithoutVAT");
                int turnoverVAT = rs.getInt("turnoverVAT");
                int turnoverWithVAT = rs.getInt("turnoverWithVAT");


                System.out.println(arv + " " + averageOrderAmount + " " + turnoverWithoutVAT + " "
                        + turnoverVAT + " " + turnoverWithVAT);

                report = new Report(arv, averageOrderAmount, turnoverWithoutVAT, turnoverVAT, turnoverWithVAT);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return report;
    }

    void deleteOrderById(String id) {
        String sqlOrders = "delete from orders where id=?";
        String sqlOrderRow = "delete from orderrow where orderId=?";

        List<String> sqls = Arrays.asList(sqlOrders, sqlOrderRow);

        for (String sql : sqls) {
            try (Connection conn = dataSourceBasic.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, Long.valueOf(id));
                ps.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    void deleteAllOrders() {
        String sqlOrders = "delete from orders";
        String sqlOrderRow = "delete from orderrow";

        List<String> sqls = Arrays.asList(sqlOrders, sqlOrderRow);

        for (String sql : sqls) {
            try (Connection conn = dataSourceBasic.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    Order getOrderRows(Order order) {
        if (order != null) {
            String sql = "select itemName, quantity, price from orderrow where orderId = ?";

            try (Connection conn = dataSourceBasic.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, Long.valueOf(order.getId()));
                ResultSet rs = ps.executeQuery();

                ArrayList<OrderRow> orderRows = new ArrayList<>();

                while (rs.next()) {
                    String itemName = rs.getString("itemName");
                    int quantity = rs.getInt("quantity");
                    int price = rs.getInt("price");

                    System.out.println(itemName + " " + quantity + " " + price);

                    OrderRow orderRow = new OrderRow(itemName, quantity, price);
                    orderRows.add(orderRow);
                }

                order.setOrderRows(orderRows);

                return order;

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    long saveOrderByPost(Order order) {
        String sql = "insert into orders (id, orderNumber, orderRows) " +
                "values (next value for seq1, ?, null);";

        try (Connection conn = dataSourceBasic.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"})) {

            ps.setString(1, order.getOrderNumber());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();

            return rs.getLong("id");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void saveOrderRows(Order order) {
        if (order != null && order.getOrderRows() != null) {
            for (OrderRow orderRow : order.getOrderRows()) {
                String sql = "insert into orderrow (orderId, itemName, quantity, price) " +
                        "values (?, ?, ?, ?);";

                try (Connection conn = dataSourceBasic.getConnection();
                     PreparedStatement ps = conn.prepareStatement(sql)) {

                    ps.setString(1, order.getId());
                    ps.setString(2, orderRow.getItemName());
                    ps.setInt(3, orderRow.getQuantity());
                    ps.setInt(4, orderRow.getPrice());

                    ps.executeUpdate();

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }

    }
}
