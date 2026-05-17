<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Quan ly Nhan vien - Nga</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container mt-4">
    <div class="d-flex justify-content-between bg-dark p-3 mb-4 rounded text-white">
        <div>
            <a href="nga-user"     class="text-white me-3 text-decoration-none fw-bold">Nhan vien</a>
            <a href="nga-customer" class="text-white me-3 text-decoration-none">Khach hang</a>
            <a href="nga-category" class="text-white text-decoration-none">Danh muc</a>
        </div>
        <%-- Dung full_name khop voi getter getFull_name() trong model --%>
        <span>Chao, ${sessionScope.acc.full_name} |
            <a href="logout" class="text-warning">Thoat</a>
        </span>
    </div>

    <%-- Tim kiem --%>
    <form action="nga-user" method="post" class="row g-3 mb-3">
        <div class="col-4">
            <input type="text" name="txtSearch" class="form-control" placeholder="Tim ten/username...">
        </div>
        <div class="col-2">
            <button type="submit" class="btn btn-primary">Tim kiem</button>
        </div>
    </form>

    <%-- Bang danh sach --%>
    <table class="table table-hover border align-middle">
        <thead class="table-light">
            <tr>
                <th>ID</th>
                <th>Username</th>
                <th>Ho ten</th>
                <th>Quyen</th>
                <th>Trang thai</th>
                <th>Thao tac</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${userList}" var="u">
                <tr>
                    <td>${u.user_id}</td>
                    <td>${u.username}</td>
                    <td>${u.full_name}</td>
                    <td><span class="badge bg-info text-dark">${u.role}</span></td>
                    <td>
                        <%-- Status trong DB la 'active'/'inactive' (chu thuong) --%>
                        <span class="badge ${u.status == 'active' ? 'bg-success' : 'bg-danger'}">
                            ${u.status == 'active' ? 'Hoat dong' : 'Bi khoa'}
                        </span>
                    </td>
                    <td>
                        <a href="nga-user?action=lock&id=${u.user_id}&st=${u.status}"
                           class="btn btn-sm ${u.status == 'active' ? 'btn-warning' : 'btn-success'}">
                            ${u.status == 'active' ? 'Khoa' : 'Mo khoa'}
                        </a>
                        <a href="nga-user?action=delete&id=${u.user_id}"
                           class="btn btn-sm btn-danger"
                           onclick="return confirm('Xac nhan xoa nguoi dung nay?')">Xoa</a>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty userList}">
                <tr><td colspan="6" class="text-center text-muted">Khong co du lieu</td></tr>
            </c:if>
        </tbody>
    </table>
</body>
</html>