package controller;

import dao.Nga_CategoryDAO;
import model.Nga_Category;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "Nga_CategoryServlet", urlPatterns = {"/nga-category"})
public class Nga_CategoryServlet extends HttpServlet {

    private final Nga_CategoryDAO dao = new Nga_CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        try {
            if ("toggle".equals(action)) {
                String idParam = request.getParameter("id");
                String stParam = request.getParameter("st");
                if (idParam != null && stParam != null) {
                    int id = Integer.parseInt(idParam);
                    boolean currentSt = Boolean.parseBoolean(stParam);
                    dao.toggleVisible(id, !currentSt);
                }
                response.sendRedirect("nga-category");
                return;
            }

            List<Nga_Category> list = dao.getAll();
            request.setAttribute("cateList", list);
            request.getRequestDispatcher("nga_category.jsp").forward(request, response);
        } catch (Exception e) {
            getServletContext().log("Lỗi CategoryServlet (GET): ", e);
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
                String desc = request.getParameter("desc");

                String parentIdStr = request.getParameter("parentId");
                String sortStr = request.getParameter("sort");
                int parentId = (parentIdStr != null && !parentIdStr.isEmpty()) ? Integer.parseInt(parentIdStr) : 0;
                int sort = (sortStr != null && !sortStr.isEmpty()) ? Integer.parseInt(sortStr) : 0;

                if (name != null && !name.trim().isEmpty()) {
                    Nga_Category c = new Nga_Category(0, name.trim(), desc, parentId, sort, true);
                    dao.add(c);
                }
            }
            response.sendRedirect("nga-category");
        } catch (Exception e) {
            getServletContext().log("Lỗi CategoryServlet (POST): ", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}