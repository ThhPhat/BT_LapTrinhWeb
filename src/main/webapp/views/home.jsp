<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Trang chủ</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
</head>
<body>
<%@ include file="/views/common/navbar.jspf" %>

<div class="container" style="max-width:1100px;margin-top:32px;margin-bottom:50px;">
    <div class="page-card mb-4">
        <div class="section-title">
            <h2 class="h5 mb-0"><i class="bi bi-stars me-2 text-primary"></i>Sản phẩm mới nhất</h2>
            <a href="${pageContext.request.contextPath}/product" class="btn btn-sm btn-outline-secondary">Xem tất cả</a>
        </div>

        <c:choose>
            <c:when test="${empty latestProducts}">
                <div class="empty-state">
                    <i class="bi bi-box-seam"></i>
                    Chưa có sản phẩm nào. Hãy vào trang quản trị để thêm sản phẩm!
                </div>
            </c:when>
            <c:otherwise>
                <div class="product-grid">
                    <c:forEach items="${latestProducts}" var="p">
                        <c:url value="/product/detail" var="detailUrl"><c:param name="id" value="${p.id}"/></c:url>
                        <c:url value="/image?fname=${p.image}" var="imgUrl"/>
                        <a href="${detailUrl}" class="product-card">
                            <img class="product-thumb" src="${imgUrl}" alt="${p.name}"/>
                            <div class="product-body">
                                <span class="product-cate-badge">${p.category.name}</span>
                                <div class="product-name">${p.name}</div>
                                <div class="product-price"><fmt:formatNumber value="${p.price}" type="number"/> đ</div>
                            </div>
                        </a>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<div class="container" style="max-width:640px;margin-top:0;">
    <div class="page-card text-center">
        <div class="avatar-circle-lg mx-auto mb-3">
            ${fn:substring(sessionScope.account.fullName,0,1)}
        </div>
        <h2 class="mb-1">Xin chào, ${sessionScope.account.fullName}!</h2>
        <p class="text-muted mb-4">Bạn đã đăng nhập thành công vào hệ thống.</p>

        <div class="row text-start g-3 justify-content-center">
            <div class="col-sm-8">
                <div class="border rounded-3 p-3">
                    <div class="d-flex justify-content-between py-1">
                        <span class="text-muted"><i class="bi bi-person me-2"></i>Tài khoản</span>
                        <span class="fw-semibold">${sessionScope.account.userName}</span>
                    </div>
                    <hr class="my-1"/>
                    <div class="d-flex justify-content-between py-1">
                        <span class="text-muted"><i class="bi bi-envelope me-2"></i>Email</span>
                        <span class="fw-semibold">${sessionScope.account.email}</span>
                    </div>
                    <hr class="my-1"/>
                    <div class="d-flex justify-content-between py-1">
                        <span class="text-muted"><i class="bi bi-telephone me-2"></i>Điện thoại</span>
                        <span class="fw-semibold">${sessionScope.account.phone}</span>
                    </div>
                    <hr class="my-1"/>
                    <div class="d-flex justify-content-between py-1">
                        <span class="text-muted"><i class="bi bi-shield-check me-2"></i>Vai trò</span>
                        <span class="fw-semibold">
                            <c:choose>
                                <c:when test="${sessionScope.account.roleid == 1}">Quản trị viên</c:when>
                                <c:when test="${sessionScope.account.roleid == 2}">Quản lý</c:when>
                                <c:otherwise>Thành viên</c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                </div>
            </div>
        </div>

        <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger mt-4 px-4">
            <i class="bi bi-box-arrow-right me-1"></i>Đăng xuất
        </a>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
