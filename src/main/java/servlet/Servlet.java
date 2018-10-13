package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

@WebServlet(value = {"/api/orders", "/orders/form"})
public class Servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AtomicInteger atomicInteger = new AtomicInteger(1);
    private HashMap<Integer, Order> orders = new HashMap<>();
    public static String URL = "jdbc:hsqldb:mem:orders";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getRequestURI().equals("/api/orders")) {
            if (request.getParameterMap().containsKey("id")) {
                int parsedKey = Integer.parseInt(request.getParameter("id"));

                String sql = "select id, orderNumber, orderRows from orders where id = ?";

                try (Connection conn = DriverManager.getConnection(URL);
                     PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setLong(1, parsedKey);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        String id = rs.getString("id");
                        String orderNumber = rs.getString("orderNumber");
                        String orderRows = rs.getString("orderRows");
                        System.out.println(id + " " + orderNumber + " " + orderRows);

                        response.setHeader("Content-Type", "application/json");
                        response.getWriter().print(new ObjectMapper().writeValueAsString(new Order(id, orderNumber, null)));

                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }

            if (request.getParameterMap().isEmpty()) {
                String sql = "select id, orderNumber, orderRows from orders";

                try (Connection conn = DriverManager.getConnection(URL);
                     PreparedStatement ps = conn.prepareStatement(sql)) {
                    ResultSet rs = ps.executeQuery();

                    ArrayList<Order> orders = new ArrayList<>();
                    while (rs.next()) {
                        String id = rs.getString("id");
                        String orderNumber = rs.getString("orderNumber");
                        String orderRows = rs.getString("orderRows");

                        orders.add(new Order(id, orderNumber, null));

                    }

                    response.setHeader("Content-Type", "application/json");
                    response.getWriter().print(new ObjectMapper().writeValueAsString(orders));

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getRequestURI().equals("/api/orders")) {

            ObjectMapper mapper = new ObjectMapper();
            Order order = mapper.readValue(request.getInputStream(), Order.class);

            String sql = "insert into orders (id, orderNumber, orderRows) " +
                    "values (next value for seq1, ?, null);";

            try (Connection conn = DriverManager.getConnection(URL);
                 PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"})) {

                ps.setString(1, order.getOrderNumber());
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                long generatedID = rs.getLong("id");

                order.setId(String.valueOf(generatedID));

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            response.setHeader("Content-Type", "application/json");
            response.getWriter().print(new ObjectMapper().writeValueAsString(order));
        }

        if (request.getRequestURI().equals("/orders/form")) {
            // Integer newID = atomicInteger.getAndIncrement();

            Order order = new Order();
            order.setOrderNumber(request.getParameter("orderNumber"));

            // order.setId(String.valueOf(newID));

            String sql = "insert into orders (id, orderNumber, orderRows) " +
                    "values (next value for seq1, ?, null);";

            try (Connection conn = DriverManager.getConnection(URL);
                 PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"})) {

                ps.setString(1, order.getOrderNumber());
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                long generatedID = rs.getLong("id");

                order.setId(String.valueOf(generatedID));

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


            // orders.put(newID, order);

            response.setHeader("Content-Type", "application/json");
            response.getWriter().print(order.getId());
        }
    }

}
