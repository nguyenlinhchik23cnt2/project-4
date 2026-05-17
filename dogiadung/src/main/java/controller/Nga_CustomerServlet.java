package controller;

import dao.Nga_CustomerDAO;
import model.Nga_Customer;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "Nga_CustomerServlet", urlPatterns = {"/nga-customer"})
public class Nga_CustomerServlet extends HttpServlet {

    private final Nga_CustomerDAO dao = new Nga_CustomerDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        try {
            String txt = request.getParameter("txtSearch");
            List<Nga_Customer> list;

            if (txt != null && !txt.trim().isEmpty()) {
                list = dao.search(txt.trim());
            } else {
                list = dao.getAll();
            }

            request.setAttribute("customerList", list);
            request.getRequestDispatcher("nga_customer.jsp").forward(request, response);
        } catch (Exception e) {
            getServletContext().log("Lỗi CustomerServlet (GET): ", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        try {
            if ("add".equals(action)) {
                String name = request.getParameter("name");
                String phone = request.getParameter("phone");
                String email = request.getParameter("email");
                String address = request.getParameter("address");

                if (name != null && !name.trim().isEmpty()
                        && phone != null && !phone.trim().isEmpty()) {
                    Nga_Customer c = new Nga_Customer(0, name.trim(), phone.trim(),
                            email != null ? email.trim() : "",
                            address != null ? address.trim() : "");
                    dao.register(c);
                }
            }
            response.sendRedirect("nga-customer");
        } catch (Exception e) {
            getServletContext().log("Lỗi CustomerServlet (POST): ", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}