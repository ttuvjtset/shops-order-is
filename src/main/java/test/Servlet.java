package test;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.OrderHsqlDao;
import model.Order;
import model.Report;
import validation.ValidationError;
import validation.ValidationErrors;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@WebServlet(value = {"/api/orders", "/orders/form", "/api/orders/report"})
public class Servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrderHsqlDao orderHsqlDao;

    public Servlet() {
        orderHsqlDao = new OrderHsqlDao();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        if (request.getRequestURI().equals("/api/orders/report")) {
            Report report = orderHsqlDao.getReport();
            response.setHeader("Content-Type", "application/json");
            response.getWriter().print(new ObjectMapper().writeValueAsString(report));
        }

        if (request.getRequestURI().equals("/api/orders")) {
            if (request.getParameterMap().containsKey("id")) {
                int parsedKey = Integer.parseInt(request.getParameter("id"));
                Order orderById = orderHsqlDao.getOrderById(parsedKey);

                response.setHeader("Content-Type", "application/json");
                response.getWriter().print(new ObjectMapper().writeValueAsString(orderById));
            }

            if (request.getParameterMap().isEmpty()) {
                ArrayList<Order> allOrders = orderHsqlDao.getAllOrders();
                response.setHeader("Content-Type", "application/json");
                response.getWriter().print(new ObjectMapper().writeValueAsString(allOrders));
            }
        }


    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        if (request.getRequestURI().equals("/api/orders")) {
            if (request.getParameterMap().containsKey("id")) {
                orderHsqlDao.deleteOrderById(request.getParameter("id"));
            } else {
                orderHsqlDao.deleteAllOrders();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (request.getRequestURI().equals("/api/orders")) {
            ObjectMapper mapper = new ObjectMapper();
            Order order = mapper.readValue(request.getInputStream(), Order.class);

            if (order.getOrderNumber().length() < 2) {
                generateValidationErrorTooShortNumber(response);
            } else {
                order.setId(String.valueOf(orderHsqlDao.saveOrderByPost(order)));
                orderHsqlDao.saveOrderRows(order);
                response.setHeader("Content-Type", "application/json");
                response.getWriter().print(new ObjectMapper().writeValueAsString(order));
            }
        }

        if (request.getRequestURI().equals("/orders/form")) {
            if (request.getParameter("orderNumber") != null && request.getParameter("orderNumber").length() < 2) {
                generateValidationErrorTooShortNumber(response);
            } else {
                Order order = new Order();
                order.setOrderNumber(request.getParameter("orderNumber"));
                order.setId(String.valueOf(orderHsqlDao.saveOrderByPost(order)));

                response.setHeader("Content-Type", "application/json");
                response.getWriter().print(order.getId());
            }
        }
    }

    private void generateValidationErrorTooShortNumber(HttpServletResponse response) throws IOException {
        ValidationError validationError = new ValidationError("too_short_number");
        List<ValidationError> validationErrorList = Collections.singletonList(validationError);
        ValidationErrors validationErrors = new ValidationErrors(validationErrorList);
        response.setHeader("Content-Type", "application/json");
        response.setStatus(400);
        response.getWriter().print(new ObjectMapper().writeValueAsString(validationErrors));
    }
}
