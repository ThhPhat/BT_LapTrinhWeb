<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Đăng nhập</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
</head>
<body>
<div class="auth-wrapper">
    <div class="auth-card">
        <div class="auth-logo"><i class="bi bi-shield-lock-fill"></i></div>
        <h2>Đăng nhập</h2>
        <p class="auth-sub">Chào mừng quay lại! Vui lòng nhập thông tin tài khoản.</p>

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

        <form action="${pageContext.request.contextPath}/login" method="post" novalidate>
            <div class="mb-3">
                <label class="form-label">Tài khoản</label>
                <div class="input-group">
                    <span class="input-group-text bg-white"><i class="bi bi-person"></i></span>
                    <input type="text" name="username" class="form-control" placeholder="Nhập tài khoản" required autofocus/>
                </div>
            </div>
            <div class="mb-3">
                <label class="form-label">Mật khẩu</label>
                <div class="input-group">
                    <span class="input-group-text bg-white"><i class="bi bi-key"></i></span>
                    <input type="password" name="password" class="form-control" placeholder="Nhập mật khẩu" required/>
                </div>
            </div>
            <div class="mb-3 d-flex justify-content-between align-items-center">
                <div class="form-check">
                    <input type="checkbox" class="form-check-input" name="remember" id="remember"/>
                    <label class="form-check-label" for="remember">Nhớ tôi</label>
                </div>
                <a href="${pageContext.request.contextPath}/forgot-password" style="font-size:.9rem;">Quên mật khẩu?</a>
            </div>
            <button type="submit" class="btn btn-brand w-100 py-2">Đăng nhập</button>
        </form>

        <p class="text-center mt-3 mb-0" style="font-size:.92rem;">
            Chưa có tài khoản?
            <a href="${pageContext.request.contextPath}/register" class="fw-semibold">Đăng ký ngay</a>
        </p>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
