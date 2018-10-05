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

    AtomicInteger atomicInteger = new AtomicInteger(1);
    HashMap<Integer, Order> orders = new HashMap<>();

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getParameterMap().containsKey("id")) {
            String orderID = request.getParameter("id");
            int parsedKey = Integer.parseInt(orderID);

            if (orders.containsKey(parsedKey)) {
                Order orderByID = orders.get(parsedKey);
                response.setHeader("Content-Type", "application/json");
                response.getWriter().print(new ObjectMapper().writeValueAsString(orderByID));
            }
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getRequestURI().equals("/api/orders")) {

            ObjectMapper mapper = new ObjectMapper();
            Order order = mapper.readValue(req.getInputStream(), Order.class);
            Integer newID = atomicInteger.getAndIncrement();
            order.setId(String.valueOf(newID));
            orders.put(newID, order);
            resp.setHeader("Content-Type", "application/json");
            resp.getWriter().print(new ObjectMapper().writeValueAsString(order));
        }

        if (req.getRequestURI().equals("/orders/form")) {
            String orderNumber = req.getParameter("orderNumber");
            Order order = new Order();
            Integer newID = atomicInteger.getAndIncrement();
            order.setId(String.valueOf(newID));
            order.setOrderNumber(orderNumber);
            orders.put(newID, order);
            resp.setHeader("Content-Type", "application/json");
            resp.getWriter().print(order.getId());
        }
    }

}
