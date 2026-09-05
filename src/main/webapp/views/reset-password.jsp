<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Đặt lại mật khẩu</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
</head>
<body>
<div class="auth-wrapper">
    <div class="auth-card">
        <div class="auth-logo"><i class="bi bi-shield-lock-fill"></i></div>
        <h2>Đặt lại mật khẩu</h2>
        <p class="auth-sub">Nhập mã OTP đã gửi tới <strong>${email}</strong> và mật khẩu mới.</p>

        <c:if test="${flash != null}">
            <div class="alert alert-success d-flex align-items-center gap-2 py-2">
                <i class="bi bi-check-circle-fill"></i><div>${flash}</div>
            </div>
        </c:if>
        <c:if test="${alert != null}">
            <div class="alert alert-danger d-flex align-items-center gap-2 py-2">
                <i class="bi bi-exclamation-triangle-fill"></i><div>${alert}</div>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/reset-password" method="post" novalidate>
            <div class="mb-3">
                <label class="form-label">Mã OTP</label>
                <input type="text" name="otp" class="form-control" maxlength="6" pattern="\d{6}" placeholder="6 chữ số" required autofocus/>
            </div>
            <div class="mb-3">
                <label class="form-label">Mật khẩu mới</label>
                <input type="password" name="newPassword" class="form-control" placeholder="Tối thiểu 6 ký tự" minlength="6" required/>
            </div>
            <div class="mb-3">
                <label class="form-label">Nhập lại mật khẩu mới</label>
                <input type="password" name="confirmPassword" class="form-control" placeholder="Nhập lại mật khẩu mới" minlength="6" required/>
            </div>
            <button type="submit" class="btn btn-brand w-100 py-2">Đặt lại mật khẩu</button>
        </form>

        <p class="text-center mt-3 mb-0" style="font-size:.92rem;">
            <a href="${pageContext.request.contextPath}/forgot-password">Gửi lại mã khác</a> ·
            <a href="${pageContext.request.contextPath}/login">Quay lại đăng nhập</a>
        </p>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
