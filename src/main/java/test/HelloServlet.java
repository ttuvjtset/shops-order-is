package test;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@WebServlet("/api/orders")
public class HelloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    AtomicInteger atomicInteger = new AtomicInteger(1);

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.getWriter().print("Hello!");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String request = Util.asString(req.getInputStream());

        String parsedOrderNumber = Util.parseOrderNumber(request);

        Order order = new Order();
        order.setId(String.valueOf(atomicInteger.getAndIncrement()));
        order.setOrderNumber(parsedOrderNumber);

        resp.setHeader("Content-Type", "application/json");
        resp.getWriter().print("{ \"id\": \"" + order.getId() + "\", \"orderNumber\": \"" + order.getOrderNumber() + "\" }");
    }
}
