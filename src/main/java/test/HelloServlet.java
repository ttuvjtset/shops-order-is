package test;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

@WebServlet(value = {"/api/orders", "/orders/form"})
public class HelloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AtomicInteger atomicInteger = new AtomicInteger(1);
    private HashMap<Integer, Order> orders = new HashMap<>();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getRequestURI().equals("/api/orders")) {
            if (request.getParameterMap().containsKey("id")) {
                int parsedKey = Integer.parseInt(request.getParameter("id"));

                if (orders.containsKey(parsedKey)) {
                    Order orderByID = orders.get(parsedKey);
                    response.setHeader("Content-Type", "application/json");
                    response.getWriter().print(new ObjectMapper().writeValueAsString(orderByID));
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getRequestURI().equals("/api/orders")) {
            Integer newID = atomicInteger.getAndIncrement();

            ObjectMapper mapper = new ObjectMapper();
            Order order = mapper.readValue(request.getInputStream(), Order.class);
            order.setId(String.valueOf(newID));
            orders.put(newID, order);

            response.setHeader("Content-Type", "application/json");
            response.getWriter().print(new ObjectMapper().writeValueAsString(order));
        }

        if (request.getRequestURI().equals("/orders/form")) {
            Integer newID = atomicInteger.getAndIncrement();

            Order order = new Order();
            order.setId(String.valueOf(newID));
            order.setOrderNumber(request.getParameter("orderNumber"));
            orders.put(newID, order);

            response.setHeader("Content-Type", "application/json");
            response.getWriter().print(order.getId());
        }
    }

}
