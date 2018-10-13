package servlet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

class OrderDao {
    private BasicDataSource basicDataSource;

    OrderDao() {
        basicDataSource = new BasicDataSource();
    }

    ArrayList<Order> getAllOrders() {
        ArrayList<Order> orders = new ArrayList<>();

        String sql = "select id, orderNumber, orderRows from orders";

        try (Connection conn = basicDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString("id");
                String orderNumber = rs.getString("orderNumber");
                String orderRows = rs.getString("orderRows");

                orders.add(new Order(id, orderNumber, null));
            }

            return orders;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    Order getOrderById(int parsedKey) {
        Order order = null;

        String sql = "select id, orderNumber, orderRows from orders where id = ?";

        try (Connection conn = basicDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, parsedKey);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String id = rs.getString("id");
                String orderNumber = rs.getString("orderNumber");
                String orderRows = rs.getString("orderRows");
                System.out.println(id + " " + orderNumber + " " + orderRows);

                return new Order(id, orderNumber, null);
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    long saveOrderByPost(Order order) {
        String sql = "insert into orders (id, orderNumber, orderRows) " +
                "values (next value for seq1, ?, null);";

        try (Connection conn = basicDataSource.getConnection();
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
}
