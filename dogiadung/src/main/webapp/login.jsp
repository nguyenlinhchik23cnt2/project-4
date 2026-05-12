<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Dang nhap - Nga Store</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f8f9fa; display: flex; align-items: center; justify-content: center; height: 100vh; }
        .login-card { width: 400px; padding: 30px; border-radius: 15px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); background: white; }
        .btn-login { background-color: #343a40; color: white; width: 100%; }
        .btn-login:hover { background-color: #23272b; color: white; }
    </style>
</head>
<body>
    <div class="login-card">
        <h3 class="text-center mb-4">Nga Store Login</h3>

        <%-- Hien thi loi neu co --%>
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-danger text-center py-2">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <%-- Hien thi thong bao dang ky thanh cong --%>
        <% if ("success".equals(request.getParameter("mess"))) { %>
            <div class="alert alert-success text-center py-2">
                Dang ky thanh cong! Vui long dang nhap.
            </div>
        <% } %>

        <form action="login" method="post">
            <div class="mb-3">
                <label class="form-label">Ten dang nhap</label>
                <input type="text" name="user" class="form-control"
                       placeholder="Nhap username..." required
                       autocomplete="username">
            </div>
            <div class="mb-3">
                <label class="form-label">Mat khau</label>
                <input type="password" name="pass" class="form-control"
                       placeholder="Nhap password..." required
                       autocomplete="current-password">
            </div>
            <button type="submit" class="btn btn-login">Dang nhap</button>
            <div class="text-center mt-3">
                <a href="register" class="text-secondary">Chua co tai khoan? Dang ky ngay</a>
            </div>
        </form>
    </div>
</body>
</html>