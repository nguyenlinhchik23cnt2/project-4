<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Quan ly Khach hang - Nga</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container mt-4">
    <nav class="navbar navbar-dark bg-dark mb-4 p-2 rounded">
        <div class="navbar-nav d-flex flex-row">
            <a class="nav-link px-3" href="nga-user">Nhan vien</a>
            <a class="nav-link px-3 active" href="nga-customer">Khach hang</a>
            <a class="nav-link px-3" href="nga-category">Danh muc</a>
        </div>
    </nav>

    <h3>Quan ly Khach hang</h3>

    <%-- Tim kiem (F12) --%>
    <form action="nga-customer" method="get" class="row g-3 mb-3">
        <div class="col-4">
            <input type="text" name="txtSearch" class="form-control" placeholder="Tim ten hoac so dien thoai...">
        </div>
        <div class="col-auto">
            <button type="submit" class="btn btn-primary">Tim kiem</button>
            <button type="button" class="btn btn-success"
                    data-bs-toggle="modal" data-bs-target="#addCusModal">Them moi</button>
        </div>
    </form>

    <%-- Bang danh sach (F10) --%>
    <table class="table table-hover table-bordered align-middle">
        <thead class="table-info">
            <tr>
                <th>ID</th>
                <th>Ho ten</th>
                <th>Dien thoai</th>
                <th>Email</th>
                <th>Dia chi</th>
                <th>Thao tac</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${customerList}" var="cus">
                <tr>
                    <td>${cus.customerId}</td>
                    <td>${cus.fullName}</td>
                    <td>${cus.phone}</td>
                    <td>${cus.email}</td>
                    <td>${cus.address}</td>
                    <td>
                        <%-- Nut sua (F11) mo modal va dien san du lieu --%>
                        <button class="btn btn-sm btn-warning"
                                data-bs-toggle="modal"
                                data-bs-target="#editCusModal"
                                data-id="${cus.customerId}"
                                data-name="${cus.fullName}"
                                data-phone="${cus.phone}"
                                data-email="${cus.email}"
                                data-address="${cus.address}">Sua</button>
                        <a href="order-history?id=${cus.customerId}"
                           class="btn btn-sm btn-info">Lich su (F13)</a>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty customerList}">
                <tr><td colspan="6" class="text-center text-muted">Khong co du lieu</td></tr>
            </c:if>
        </tbody>
    </table>

    <%-- Modal them moi (F09) --%>
    <div class="modal fade" id="addCusModal" tabindex="-1">
        <div class="modal-dialog">
            <form action="nga-customer" method="post" class="modal-content">
                <input type="hidden" name="action" value="add">
                <div class="modal-header">
                    <h5 class="modal-title">Them Khach Hang</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <input type="text"     name="name"    class="form-control mb-2" placeholder="Ho ten" required>
                    <input type="text"     name="phone"   class="form-control mb-2" placeholder="So dien thoai" required>
                    <input type="email"    name="email"   class="form-control mb-2" placeholder="Email">
                    <input type="text"     name="address" class="form-control mb-2" placeholder="Dia chi">
                    <input type="password" name="pass"    class="form-control mb-2" placeholder="Mat khau" required>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Huy</button>
                    <button type="submit" class="btn btn-primary">Dang ky</button>
                </div>
            </form>
        </div>
    </div>

    <%-- Modal sua (F11) --%>
    <div class="modal fade" id="editCusModal" tabindex="-1">
        <div class="modal-dialog">
            <form action="nga-customer" method="post" class="modal-content">
                <input type="hidden" name="action" value="edit">
                <input type="hidden" name="id"     id="editId">
                <div class="modal-header">
                    <h5 class="modal-title">Sua Khach Hang</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <input type="text"  name="name"    id="editName"    class="form-control mb-2" placeholder="Ho ten" required>
                    <input type="text"  name="phone"   id="editPhone"   class="form-control mb-2" placeholder="So dien thoai" required>
                    <input type="email" name="email"   id="editEmail"   class="form-control mb-2" placeholder="Email">
                    <input type="text"  name="address" id="editAddress" class="form-control mb-2" placeholder="Dia chi">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Huy</button>
                    <button type="submit" class="btn btn-warning">Luu</button>
                </div>
            </form>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Dien du lieu vao modal sua khi click nut Sua
        document.getElementById('editCusModal').addEventListener('show.bs.modal', function (e) {
            var btn = e.relatedTarget;
            document.getElementById('editId').value      = btn.getAttribute('data-id');
            document.getElementById('editName').value    = btn.getAttribute('data-name');
            document.getElementById('editPhone').value   = btn.getAttribute('data-phone');
            document.getElementById('editEmail').value   = btn.getAttribute('data-email');
            document.getElementById('editAddress').value = btn.getAttribute('data-address');
        });
    </script>
</body>
</html>