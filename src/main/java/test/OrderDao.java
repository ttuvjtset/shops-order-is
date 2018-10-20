package test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

class OrderDao {
    private DataSourceBasic dataSourceBasic;

    OrderDao() {
        dataSourceBasic = new DataSourceBasic();
    }

    ArrayList<Order> getAllOrders() {
        ArrayList<Order> orders = new ArrayList<>();

        String sql = "select id, orderNumber, orderRows from orders";

        try (Connection conn = dataSourceBasic.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString("id");
                String orderNumber = rs.getString("orderNumber");
                String orderRows = rs.getString("orderRows");

                Order order = new Order();
                order.setId(id);
                order.setOrderNumber(orderNumber);

                orders.add(order);
            }

            return orders;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    Order getOrderById(int parsedKey) {
        Order order = null;

        String sql = "select id, orderNumber, orderRows from orders where id = ?";

        try (Connection conn = dataSourceBasic.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, parsedKey);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String id = rs.getString("id");
                String orderNumber = rs.getString("orderNumber");
                String orderRows = rs.getString("orderRows");
                System.out.println(id + " " + orderNumber + " " + orderRows);

                order = new Order();
                order.setId(id);
                order.setOrderNumber(orderNumber);
                return order;
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void deleteOrderById(String id) {
        String sql = "delete from orders where id=?";

        try (Connection conn = dataSourceBasic.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, Long.valueOf(id));
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
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
