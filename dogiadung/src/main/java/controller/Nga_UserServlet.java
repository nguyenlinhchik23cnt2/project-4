package controller;

import dao.Nga_UserDAO;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "Nga_UserServlet", urlPatterns = {"/nga-user"})
public class Nga_UserServlet extends HttpServlet {
    private final Nga_UserDAO dao = new Nga_UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("acc") == null) {
            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");
        try {
            if ("lock".equals(action)) {
                String idParam = request.getParameter("id");
                String currentStatus = request.getParameter("st");
                if (idParam != null && currentStatus != null) {
                    int id = Integer.parseInt(idParam);
                    String nextStatus = "active".equalsIgnoreCase(currentStatus) ? "inactive" : "active";
                    dao.updateStatus(id, nextStatus);
                }
                response.sendRedirect("nga-user");
                return;
            } else if ("delete".equals(action)) {
                String idParam = request.getParameter("id");
                if (idParam != null) {
                    dao.delete(Integer.parseInt(idParam));
                }
                response.sendRedirect("nga-user");
                return;
            }

            request.setAttribute("userList", dao.getAll(""));
            request.getRequestDispatcher("nga_user.jsp").forward(request, response);
        } catch (Exception e) {
            getServletContext().log("Error UserServlet (GET): ", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("acc") == null) {
            response.sendRedirect("login");
            return;
        }

        try {
            String txt = request.getParameter("txtSearch");
            request.setAttribute("userList", dao.getAll(txt != null ? txt.trim() : ""));
            request.getRequestDispatcher("nga_user.jsp").forward(request, response);
        } catch (Exception e) {
            getServletContext().log("Error UserServlet (POST): ", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}