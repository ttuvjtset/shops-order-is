package test;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(value = {"/api/orders", "/orders/form"})
public class Servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrderDao orderDao;

    public Servlet() {
        orderDao = new OrderDao();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getRequestURI().equals("/api/orders")) {
            if (request.getParameterMap().containsKey("id")) {
                int parsedKey = Integer.parseInt(request.getParameter("id"));
                Order orderById = orderDao.getOrderById(parsedKey);
                response.setHeader("Content-Type", "application/json");
                response.getWriter().print(new ObjectMapper().writeValueAsString(orderById));
            }

            if (request.getParameterMap().isEmpty()) {
                ArrayList<Order> allOrders = orderDao.getAllOrders();
                response.setHeader("Content-Type", "application/json");
                response.getWriter().print(new ObjectMapper().writeValueAsString(allOrders));
            }
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getRequestURI().equals("/api/orders")) {

            ObjectMapper mapper = new ObjectMapper();
            Order order = mapper.readValue(request.getInputStream(), Order.class);
            order.setId(String.valueOf(orderDao.saveOrderByPost(order)));

            response.setHeader("Content-Type", "application/json");
            response.getWriter().print(new ObjectMapper().writeValueAsString(order));
        }

        if (request.getRequestURI().equals("/orders/form")) {

            Order order = new Order();
            order.setOrderNumber(request.getParameter("orderNumber"));
            order.setId(String.valueOf(orderDao.saveOrderByPost(order)));

            response.setHeader("Content-Type", "application/json");
            response.getWriter().print(order.getId());
        }
    }
}
