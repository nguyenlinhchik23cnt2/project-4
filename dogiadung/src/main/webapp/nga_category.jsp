<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Quan ly Danh muc - Nga</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container mt-4">
    <h2 class="text-center mb-4 text-success">DANH MUC SAN PHAM</h2>

    <%-- Form them danh muc (F16) --%>
    <div class="card mb-4">
        <div class="card-header fw-bold">Them danh muc moi</div>
        <div class="card-body">
            <form action="nga-category" method="post">
                <input type="hidden" name="action" value="add">
                <div class="row g-2">
                    <div class="col-md-3">
                        <input type="text" name="name" class="form-control" placeholder="Ten danh muc" required>
                    </div>
                    <div class="col-md-3">
                        <input type="text" name="desc" class="form-control" placeholder="Mo ta">
                    </div>
                    <div class="col-md-2">
                        <input type="number" name="parentId" class="form-control" placeholder="ID Cha (0 = goc)" value="0" min="0">
                    </div>
                    <div class="col-md-2">
                        <input type="number" name="sort" class="form-control" placeholder="Thu tu" value="0" min="0">
                    </div>
                    <div class="col-md-2">
                        <button type="submit" class="btn btn-success w-100">Them</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <%-- Bang danh sach danh muc (F19, F20) --%>
    <table class="table table-striped border align-middle">
        <thead class="table-success">
            <tr>
                <th>ID</th>
                <th>Ten danh muc</th>
                <th>Mo ta</th>
                <th>ID Cha</th>
                <th>Thu tu</th>
                <th>Trang thai</th>
                <th>Hanh dong</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${cateList}" var="cat">
                <tr>
                    <td>${cat.categoryId}</td>
                    <td>
                        <%-- Hien thi thu bac cha-con (F19) --%>
                        <c:if test="${cat.parentId != 0}">
                            <span class="text-muted ms-3">↳</span>
                        </c:if>
                        ${cat.name}
                    </td>
                    <td>${cat.description}</td>
                    <td>${cat.parentId == 0 ? 'Goc' : cat.parentId}</td>
                    <td>${cat.sortOrder}</td>
                    <td>
                        <c:choose>
                            <c:when test="${cat.visible}">
                                <span class="badge bg-success">Hien</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-secondary">An</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <%-- Nut an/hien (F18) --%>
                        <a href="nga-category?action=toggle&id=${cat.categoryId}&st=${cat.visible}"
                           class="btn btn-sm ${cat.visible ? 'btn-warning' : 'btn-success'}">
                            ${cat.visible ? 'An' : 'Hien'}
                        </a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>