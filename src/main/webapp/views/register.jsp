<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Đăng ký</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
</head>
<body>
<div class="auth-wrapper">
    <div class="auth-card" style="max-width:480px;">
        <div class="auth-logo"><i class="bi bi-person-plus-fill"></i></div>
        <h2>Tạo tài khoản mới</h2>
        <p class="auth-sub">Điền thông tin bên dưới để bắt đầu.</p>

        <c:if test="${alert != null}">
            <div class="alert alert-danger d-flex align-items-center gap-2 py-2">
                <i class="bi bi-exclamation-triangle-fill"></i><div>${alert}</div>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/register" method="post" novalidate>
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label">Tài khoản</label>
                    <input type="text" name="username" class="form-control" placeholder="Tài khoản" value="${param.username}" required/>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label">Họ tên</label>
                    <input type="text" name="fullname" class="form-control" placeholder="Họ tên" value="${param.fullname}" required/>
                </div>
            </div>
            <div class="mb-3">
                <label class="form-label">Email</label>
                <input type="email" name="email" class="form-control" placeholder="you@example.com" value="${param.email}" required/>
            </div>
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label">Điện thoại</label>
                    <input type="text" name="phone" class="form-control" placeholder="09xxxxxxxx" value="${param.phone}" required/>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label">Mật khẩu</label>
                    <input type="password" name="password" class="form-control" placeholder="Tối thiểu 6 ký tự" minlength="6" required/>
                </div>
            </div>
            <button type="submit" class="btn btn-brand w-100 py-2 mt-1">Tạo tài khoản</button>
        </form>

        <p class="text-center mt-3 mb-0" style="font-size:.92rem;">
            Đã có tài khoản?
            <a href="${pageContext.request.contextPath}/login" class="fw-semibold">Đăng nhập</a>
        </p>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
