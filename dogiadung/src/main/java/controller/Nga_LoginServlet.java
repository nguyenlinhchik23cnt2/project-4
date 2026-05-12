package controller;

import dao.Nga_UserDAO;
import model.Nga_User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "Nga_LoginServlet", urlPatterns = {"/login"})
public class Nga_LoginServlet extends HttpServlet {

    private final Nga_UserDAO dao = new Nga_UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Nếu đã login rồi thì redirect luôn, khỏi vào lại trang login
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("acc") != null) {
            response.sendRedirect("nga-user");
            return;
        }
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String u = request.getParameter("user");
        String p = request.getParameter("pass");

        if (u == null || p == null || u.trim().isEmpty() || p.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ tài khoản và mật khẩu!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        try {
            Nga_User a = dao.login(u.trim(), p.trim());

            if (a != null) {
                // Invalidate session cũ trước khi tạo mới — tránh session fixation
                HttpSession oldSession = request.getSession(false);
                if (oldSession != null) oldSession.invalidate();

                HttpSession session = request.getSession(true);
                session.setAttribute("acc", a);
                session.setMaxInactiveInterval(30 * 60); // Timeout 30 phút
                response.sendRedirect("nga-user");
            } else {
                request.setAttribute("error", "Tài khoản hoặc mật khẩu không đúng, hoặc đã bị khóa!");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            getServletContext().log("Lỗi LoginServlet: ", e); // dùng log thay e.printStackTrace()
            request.setAttribute("error", "Hệ thống đang gặp sự cố, vui lòng thử lại sau!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}