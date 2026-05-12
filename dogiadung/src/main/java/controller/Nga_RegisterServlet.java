package controller;

import dao.Nga_UserDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "Nga_RegisterServlet", urlPatterns = {"/register"})
public class Nga_RegisterServlet extends HttpServlet {

    private final Nga_UserDAO dao = new Nga_UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("acc") != null) {
            response.sendRedirect("nga-user");
            return;
        }
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String user   = request.getParameter("user");
        String name   = request.getParameter("name");
        String pass   = request.getParameter("pass");
        String repass = request.getParameter("repass");
        String email  = request.getParameter("email");

        // 1. Kiem tra null / rong
        if (user == null || name == null || pass == null || repass == null
                || user.trim().isEmpty() || name.trim().isEmpty()
                || pass.trim().isEmpty() || repass.trim().isEmpty()) {
            request.setAttribute("error", "Vui long nhap day du thong tin!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        user  = user.trim();
        name  = name.trim();
        email = (email != null) ? email.trim() : "";

        // 2. Kiem tra mat khau khop nhau
        if (!pass.equals(repass)) {
            request.setAttribute("error", "Mat khau xac nhan khong khop!");
            request.setAttribute("oldUser", user);
            request.setAttribute("oldName", name);
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        try {
            // 3. Kiem tra tai khoan da ton tai chua
            if (dao.findByUsername(user) != null) {
                request.setAttribute("error", "Tai khoan nay da co nguoi su dung!");
                request.setAttribute("oldUser", user);
                request.setAttribute("oldName", name);
                request.getRequestDispatcher("register.jsp").forward(request, response);
            } else {
                // 4. Dang ky thanh cong
                dao.register(user, pass, name, email);
                response.sendRedirect("login?mess=success");
            }
        } catch (Exception e) {
            getServletContext().log("Loi RegisterServlet: ", e);
            request.setAttribute("error", "He thong dang gap su co, vui long thu lai sau!");
            request.setAttribute("oldUser", user);
            request.setAttribute("oldName", name);
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}