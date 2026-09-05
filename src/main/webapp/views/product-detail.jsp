<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${product.name}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
</head>
<body>
<%@ include file="/views/common/navbar.jspf" %>

<div class="container" style="max-width:960px;margin-top:32px;margin-bottom:60px;">
    <nav aria-label="breadcrumb" class="mb-3">
        <ol class="breadcrumb" style="font-size:.9rem;">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">Trang chủ</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/product">Sản phẩm</a></li>
            <li class="breadcrumb-item active" aria-current="page">${product.name}</li>
        </ol>
    </nav>

    <div class="page-card">
        <div class="row g-4">
            <div class="col-md-5">
                <c:url value="/image?fname=${product.image}" var="imgUrl"/>
                <img class="detail-image" src="${imgUrl}" alt="${product.name}"/>
            </div>
            <div class="col-md-7">
                <span class="product-cate-badge">${product.category.name}</span>
                <h2 class="mt-1 mb-3">${product.name}</h2>
                <div class="detail-price mb-3"><fmt:formatNumber value="${product.price}" type="number"/> đ</div>

                <div class="d-flex align-items-center gap-2 mb-3">
                    <span class="text-muted">Số lượng còn lại:</span>
                    <span class="fw-semibold">${product.quantity}</span>
                </div>

                <hr/>
                <h5 class="mb-2">Mô tả sản phẩm</h5>
                <p style="white-space:pre-line;">
                    <c:choose>
                        <c:when test="${not empty product.description}">${product.description}</c:when>
                        <c:otherwise><span class="text-muted">Chưa có mô tả cho sản phẩm này.</span></c:otherwise>
                    </c:choose>
                </p>

                <a href="${pageContext.request.contextPath}/product" class="btn btn-outline-secondary mt-3">
                    <i class="bi bi-arrow-left me-1"></i>Quay lại danh sách sản phẩm
                </a>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
