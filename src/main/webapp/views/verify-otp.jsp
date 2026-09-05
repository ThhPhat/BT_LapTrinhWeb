<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Xác thực OTP</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
</head>
<body>
<div class="auth-wrapper">
    <div class="auth-card">
        <div class="auth-logo"><i class="bi bi-envelope-check-fill"></i></div>
        <h2>Xác thực OTP</h2>
        <p class="auth-sub">Nhập mã OTP gồm 6 chữ số đã được gửi tới email của tài khoản <strong>${username}</strong>.</p>

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

        <form action="${pageContext.request.contextPath}/verify-otp" method="post" novalidate>
            <div class="mb-3">
                <label class="form-label">Mã OTP</label>
                <input type="text" name="otp" class="form-control text-center fs-4" style="letter-spacing:6px;"
                       maxlength="6" pattern="\d{6}" placeholder="------" required autofocus/>
            </div>
            <button type="submit" class="btn btn-brand w-100 py-2">Xác thực</button>
        </form>

        <p class="text-center mt-3 mb-0" style="font-size:.92rem;">
            Không nhận được mã?
            <a href="${pageContext.request.contextPath}/verify-otp?resend=1" class="fw-semibold">Gửi lại OTP</a>
        </p>
        <p class="text-center mt-2 mb-0" style="font-size:.92rem;">
            <a href="${pageContext.request.contextPath}/login">Quay lại đăng nhập</a>
        </p>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
