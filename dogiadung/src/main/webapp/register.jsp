<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Dang ky thanh vien - Nga</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container" style="max-width: 500px; margin-top: 50px;">
        <div class="card shadow">
            <div class="card-header bg-success text-white text-center">
                <h3>DANG KY TAI KHOAN</h3>
            </div>
            <div class="card-body">

                <%-- Hien thi loi neu co --%>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>

                <form action="register" method="post">
                    <div class="mb-3">
                        <label class="form-label">Tai khoan</label>
                        <%-- Giu lai gia tri cu neu co loi (oldUser) --%>
                        <input type="text" name="user" class="form-control" required
                               placeholder="Ten dang nhap..."
                               value="${not empty oldUser ? oldUser : ''}">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Ho va ten</label>
                        <input type="text" name="name" class="form-control" required
                               placeholder="Nguyen Van A..."
                               value="${not empty oldName ? oldName : ''}">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Email</label>
                        <input type="email" name="email" class="form-control"
                               placeholder="example@email.com">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Mat khau</label>
                        <input type="password" name="pass" class="form-control" required
                               minlength="6">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Xac nhan mat khau</label>
                        <input type="password" name="repass" class="form-control" required
                               minlength="6">
                    </div>
                    <button type="submit" class="btn btn-success w-100">Dang ky ngay</button>
                </form>
            </div>
            <div class="card-footer text-center">
                <%-- Dung /login thay vi login.jsp --%>
                <a href="login" class="text-decoration-none">Da co tai khoan? Dang nhap</a>
            </div>
        </div>
    </div>
</body>
</html>