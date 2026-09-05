<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Sản phẩm</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
</head>
<body>
<%@ include file="/views/common/navbar.jspf" %>

<div class="container" style="max-width:1100px;margin-top:32px;margin-bottom:60px;">
    <div class="page-card">
        <div class="page-header">
            <h2><i class="bi bi-grid-3x3-gap-fill me-2 text-primary"></i>Tất cả sản phẩm
                <span class="badge rounded-pill badge-count ms-1">${totalItems}</span>
            </h2>
        </div>

        <c:choose>
            <c:when test="${empty productList}">
                <div class="empty-state">
                    <i class="bi bi-box-seam"></i>
                    Chưa có sản phẩm nào.
                </div>
            </c:when>
            <c:otherwise>
                <div class="product-grid">
                    <c:forEach items="${productList}" var="p">
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

                <c:if test="${totalPages > 1}">
                    <nav class="d-flex justify-content-center mt-4">
                        <ul class="pagination">
                            <c:forEach begin="1" end="${totalPages}" var="p">
                                <c:url value="/product" var="pageUrl">
                                    <c:param name="page" value="${p}"/>
                                </c:url>
                                <li class="page-item ${p == currentPage ? 'active' : ''}">
                                    <a class="page-link" href="${pageUrl}">${p}</a>
                                </li>
                            </c:forEach>
                        </ul>
                    </nav>
                </c:if>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
