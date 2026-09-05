<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Quên mật khẩu</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
</head>
<body>
<div class="auth-wrapper">
    <div class="auth-card">
        <div class="auth-logo"><i class="bi bi-key-fill"></i></div>
        <h2>Quên mật khẩu</h2>
        <p class="auth-sub">Nhập email đã đăng ký, hệ thống sẽ gửi mã OTP để đặt lại mật khẩu.</p>

        <c:if test="${alert != null}">
            <div class="alert alert-danger d-flex align-items-center gap-2 py-2">
                <i class="bi bi-exclamation-triangle-fill"></i><div>${alert}</div>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/forgot-password" method="post" novalidate>
            <div class="mb-3">
                <label class="form-label">Email</label>
                <div class="input-group">
                    <span class="input-group-text bg-white"><i class="bi bi-envelope"></i></span>
                    <input type="email" name="email" class="form-control" placeholder="you@example.com" required autofocus/>
                </div>
            </div>
            <button type="submit" class="btn btn-brand w-100 py-2">Gửi mã OTP</button>
        </form>

        <p class="text-center mt-3 mb-0" style="font-size:.92rem;">
            <a href="${pageContext.request.contextPath}/login">Quay lại đăng nhập</a>
        </p>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
