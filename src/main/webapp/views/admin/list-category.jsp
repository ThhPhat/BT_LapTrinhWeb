<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Quản lý danh mục</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
</head>
<body>
<%@ include file="/views/common/navbar.jspf" %>

<div class="container" style="max-width:1000px;margin-top:32px;margin-bottom:60px;">

    <c:if test="${flash != null}">
        <div class="alert alert-success alert-dismissible fade show d-flex align-items-center gap-2" role="alert">
            <i class="bi bi-check-circle-fill"></i><div>${flash}</div>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <div class="page-card">
        <div class="page-header">
            <h2><i class="bi bi-tags-fill me-2 text-primary"></i>Quản lý danh mục
                <span class="badge rounded-pill badge-count ms-1">${totalItems}</span>
            </h2>
            <a href="${pageContext.request.contextPath}/admin/category/add" class="btn btn-brand">
                <i class="bi bi-plus-lg me-1"></i>Thêm danh mục
            </a>
        </div>

        <form action="${pageContext.request.contextPath}/admin/category/list" method="get" class="row g-2 mb-3">
            <div class="col-sm-6 col-md-4">
                <div class="input-group">
                    <span class="input-group-text bg-white"><i class="bi bi-search"></i></span>
                    <input type="text" name="keyword" class="form-control" placeholder="Tìm theo tên danh mục..." value="${keyword}"/>
                </div>
            </div>
            <div class="col-auto">
                <button class="btn btn-outline-secondary" type="submit">Tìm kiếm</button>
                <c:if test="${not empty keyword}">
                    <a href="${pageContext.request.contextPath}/admin/category/list" class="btn btn-link">Xóa lọc</a>
                </c:if>
            </div>
        </form>

        <c:choose>
        <c:when test="${empty cateList}">
            <div class="empty-state">
                <i class="bi bi-inbox"></i>
                <c:choose>
                    <c:when test="${not empty keyword}">Không tìm thấy danh mục nào khớp với "<strong>${keyword}</strong>".</c:when>
                    <c:otherwise>Chưa có danh mục nào. Hãy thêm danh mục đầu tiên!</c:otherwise>
                </c:choose>
            </div>
        </c:when>
        <c:otherwise>
            <div class="table-responsive">
                <table class="table align-middle">
                    <thead>
                    <tr>
                        <th style="width:60px;">STT</th>
                        <th style="width:100px;">Hình ảnh</th>
                        <th>Tên danh mục</th>
                        <th style="width:160px;" class="text-end">Hành động</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${cateList}" var="cate" varStatus="STT">
                        <tr>
                            <td>${(currentPage - 1) * pageSize + STT.index + 1}</td>
                            <c:url value="/image?fname=${cate.icon}" var="imgUrl"></c:url>
                            <td><img class="cate-thumb" src="${imgUrl}" alt="${cate.name}"/></td>
                            <td class="fw-semibold">${cate.name}</td>
                            <td class="text-end">
                                <a href="<c:url value='/admin/category/edit?id=${cate.id}'/>" class="btn btn-sm btn-outline-primary">
                                    <i class="bi bi-pencil-square"></i> Sửa
                                </a>
                                <a href="<c:url value='/admin/category/delete?id=${cate.id}'/>"
                                   class="btn btn-sm btn-outline-danger"
                                   onclick="return confirm('Bạn có chắc muốn xóa danh mục \'${cate.name}\'?');">
                                    <i class="bi bi-trash3"></i> Xóa
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

            <c:if test="${totalPages > 1}">
                <nav class="d-flex justify-content-center mt-3">
                    <ul class="pagination">
                        <c:forEach begin="1" end="${totalPages}" var="p">
                            <c:url value="/admin/category/list" var="pageUrl">
                                <c:param name="page" value="${p}"/>
                                <c:if test="${not empty keyword}">
                                    <c:param name="keyword" value="${keyword}"/>
                                </c:if>
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
