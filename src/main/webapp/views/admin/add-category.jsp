<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Thêm danh mục</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
</head>
<body>
<%@ include file="/views/common/navbar.jspf" %>

<div class="container" style="max-width:560px;margin-top:32px;margin-bottom:60px;">
    <div class="page-card">
        <h2 class="mb-3"><i class="bi bi-plus-circle-fill me-2 text-primary"></i>Thêm danh mục mới</h2>

        <c:if test="${error != null}">
            <div class="alert alert-danger d-flex align-items-center gap-2 py-2">
                <i class="bi bi-exclamation-triangle-fill"></i><div>${error}</div>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/admin/category/add" method="post"
              enctype="multipart/form-data" novalidate>
            <div class="mb-3">
                <label class="form-label">Tên danh mục</label>
                <input type="text" class="form-control" placeholder="Nhập tên danh mục"
                       name="name" value="${param.name}" required/>
            </div>

            <div class="mb-3">
                <label class="form-label">Ảnh đại diện</label>
                <div class="preview-box" id="previewBox">Chưa chọn ảnh</div>
                <input type="file" class="form-control" name="icon" accept="image/*" id="iconInput"/>
                <div class="form-text">Định dạng: JPG, PNG, GIF, WEBP.</div>
            </div>

            <div class="d-flex gap-2 mt-4">
                <button type="submit" class="btn btn-brand flex-fill">
                    <i class="bi bi-check-lg me-1"></i>Thêm
                </button>
                <a href="${pageContext.request.contextPath}/admin/category/list" class="btn btn-outline-secondary flex-fill">
                    Hủy
                </a>
            </div>
        </form>
    </div>
</div>

<script>
document.getElementById('iconInput').addEventListener('change', function (e) {
    var box = document.getElementById('previewBox');
    var file = e.target.files[0];
    if (file) {
        var reader = new FileReader();
        reader.onload = function (ev) {
            box.innerHTML = '<img src="' + ev.target.result + '" alt="preview"/>';
        };
        reader.readAsDataURL(file);
    } else {
        box.innerHTML = 'Chưa chọn ảnh';
    }
});
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
