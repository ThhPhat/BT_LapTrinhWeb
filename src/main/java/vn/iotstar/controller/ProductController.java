package vn.iotstar.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.fileupload2.core.DiskFileItem;
import org.apache.commons.fileupload2.core.DiskFileItemFactory;
import org.apache.commons.fileupload2.jakarta.servlet6.JakartaServletFileUpload;

import vn.iotstar.model.Category;
import vn.iotstar.model.Product;
import vn.iotstar.model.User;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.IProductService;
import vn.iotstar.service.impl.CategoryServiceImpl;
import vn.iotstar.service.impl.ProductServiceImpl;
import vn.iotstar.util.Constant;

/**
 * Servlet quản lý CRUD Products (admin), gồm bảng products có quan hệ n-1 với Category.
 *   GET   /admin/product/list    -> danh sách (tìm kiếm + phân trang)
 *   GET   /admin/product/add     -> hiển thị form thêm
 *   POST  /admin/product/add     -> xử lý thêm (upload ảnh)
 *   GET   /admin/product/edit    -> hiển thị form sửa
 *   POST  /admin/product/edit    -> xử lý sửa
 *   GET   /admin/product/delete  -> xóa sản phẩm
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = {
        "/admin/product/list",
        "/admin/product/add",
        "/admin/product/edit",
        "/admin/product/delete"
})
public class ProductController extends HttpServlet {

    private final IProductService productService = new ProductServiceImpl();
    private final ICategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        if (!isAdmin(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        String uri = req.getServletPath();

        switch (uri) {
            case "/admin/product/add":
                req.setAttribute("categories", categoryService.findAll());
                req.getRequestDispatcher(Constant.Path.PRODUCT_ADMIN_ADD).forward(req, resp);
                break;
            case "/admin/product/edit":
                showEditForm(req, resp);
                break;
            case "/admin/product/delete":
                handleDelete(req, resp);
                break;
            case "/admin/product/list":
            default:
                showList(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        if (!isAdmin(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        String uri = req.getServletPath();
        if ("/admin/product/edit".equals(uri)) {
            doUpdate(req, resp);
        } else {
            doInsert(req, resp);
        }
    }

    private boolean isAdmin(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) return false;
        Object account = session.getAttribute(Constant.SESSION_ACCOUNT);
        return account instanceof User && ((User) account).getRoleid() == 1;
    }

    // ================== LIST ==================
    private void showList(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String keyword = req.getParameter("keyword");
        int currentPage = parseIntOrDefault(req.getParameter("page"), 1);
        if (currentPage < 1) currentPage = 1;

        int totalItems = productService.countByKeyword(keyword);
        int totalPages = (int) Math.ceil((double) totalItems / Constant.PAGE_SIZE);
        if (totalPages < 1) totalPages = 1;
        if (currentPage > totalPages) currentPage = totalPages;

        List<Product> productList = productService.search(keyword, currentPage - 1, Constant.PAGE_SIZE);

        req.setAttribute("productList", productList);
        req.setAttribute("keyword", keyword);
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalItems", totalItems);
        req.setAttribute("pageSize", Constant.PAGE_SIZE);

        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("flash") != null) {
            req.setAttribute("flash", session.getAttribute("flash"));
            session.removeAttribute("flash");
        }
        req.getRequestDispatcher(Constant.Path.PRODUCT_ADMIN_LIST).forward(req, resp);
    }

    // ================== ADD ==================
    private void doInsert(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String name = null, priceStr = null, quantityStr = null, description = null, categoryIdStr = null;
        String imageFileName = null;

        if (JakartaServletFileUpload.isMultipartContent(req)) {
            try {
                DiskFileItemFactory factory = DiskFileItemFactory.builder().get();
                JakartaServletFileUpload<DiskFileItem, DiskFileItemFactory> upload =
                        new JakartaServletFileUpload<>(factory);

                for (DiskFileItem item : upload.parseRequest(req)) {
                    if (item.isFormField()) {
                        String field = item.getFieldName();
                        String value = item.getString(StandardCharsets.UTF_8);
                        switch (field) {
                            case "name": name = value; break;
                            case "price": priceStr = value; break;
                            case "quantity": quantityStr = value; break;
                            case "description": description = value; break;
                            case "categoryId": categoryIdStr = value; break;
                        }
                    } else if (!item.getName().isEmpty()) {
                        imageFileName = saveFile(item, "product");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("error", "Lỗi khi upload file: " + e.getMessage());
                req.setAttribute("categories", categoryService.findAll());
                req.getRequestDispatcher(Constant.Path.PRODUCT_ADMIN_ADD).forward(req, resp);
                return;
            }
        }

        String error = validate(name, priceStr, quantityStr, categoryIdStr);
        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("categories", categoryService.findAll());
            req.getRequestDispatcher(Constant.Path.PRODUCT_ADMIN_ADD).forward(req, resp);
            return;
        }

        Category category = categoryService.findById(Integer.parseInt(categoryIdStr));
        Product product = new Product();
        product.setName(name.trim());
        product.setPrice(new BigDecimal(priceStr.trim()));
        product.setQuantity(parseIntOrDefault(quantityStr, 0));
        product.setDescription(description);
        product.setImage(imageFileName);
        product.setCategory(category);
        product.setCreatedDate(new java.util.Date());

        productService.insert(product);

        req.getSession().setAttribute("flash", "Thêm sản phẩm thành công!");
        resp.sendRedirect(req.getContextPath() + "/admin/product/list");
    }

    // ================== EDIT ==================
    private void showEditForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = parseIntOrDefault(req.getParameter("id"), -1);
        Product product = productService.findById(id);
        if (product == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/product/list");
            return;
        }
        req.setAttribute("product", product);
        req.setAttribute("categories", categoryService.findAll());
        req.getRequestDispatcher(Constant.Path.PRODUCT_ADMIN_EDIT).forward(req, resp);
    }

    private void doUpdate(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idParam = null, name = null, priceStr = null, quantityStr = null,
                description = null, categoryIdStr = null;
        String newImageFileName = null;

        if (JakartaServletFileUpload.isMultipartContent(req)) {
            try {
                DiskFileItemFactory factory = DiskFileItemFactory.builder().get();
                JakartaServletFileUpload<DiskFileItem, DiskFileItemFactory> upload =
                        new JakartaServletFileUpload<>(factory);

                for (DiskFileItem item : upload.parseRequest(req)) {
                    if (item.isFormField()) {
                        String field = item.getFieldName();
                        String value = item.getString(StandardCharsets.UTF_8);
                        switch (field) {
                            case "id": idParam = value; break;
                            case "name": name = value; break;
                            case "price": priceStr = value; break;
                            case "quantity": quantityStr = value; break;
                            case "description": description = value; break;
                            case "categoryId": categoryIdStr = value; break;
                        }
                    } else if (!item.getName().isEmpty()) {
                        newImageFileName = saveFile(item, "product");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("error", "Lỗi khi upload file: " + e.getMessage());
                req.setAttribute("categories", categoryService.findAll());
                req.getRequestDispatcher(Constant.Path.PRODUCT_ADMIN_EDIT).forward(req, resp);
                return;
            }
        }

        int id = parseIntOrDefault(idParam, -1);
        Product product = productService.findById(id);
        if (product == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/product/list");
            return;
        }

        String error = validate(name, priceStr, quantityStr, categoryIdStr);
        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("product", product);
            req.setAttribute("categories", categoryService.findAll());
            req.getRequestDispatcher(Constant.Path.PRODUCT_ADMIN_EDIT).forward(req, resp);
            return;
        }

        Category category = categoryService.findById(Integer.parseInt(categoryIdStr));
        product.setName(name.trim());
        product.setPrice(new BigDecimal(priceStr.trim()));
        product.setQuantity(parseIntOrDefault(quantityStr, 0));
        product.setDescription(description);
        product.setCategory(category);
        if (newImageFileName != null) {
            product.setImage(newImageFileName);
        }
        productService.update(product);

        req.getSession().setAttribute("flash", "Cập nhật sản phẩm thành công!");
        resp.sendRedirect(req.getContextPath() + "/admin/product/list");
    }

    // ================== DELETE ==================
    private void handleDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = parseIntOrDefault(req.getParameter("id"), -1);
        try {
            productService.delete(id);
            req.getSession().setAttribute("flash", "Xóa sản phẩm thành công!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        resp.sendRedirect(req.getContextPath() + "/admin/product/list");
    }

    // ================== helpers ==================
    private String validate(String name, String priceStr, String quantityStr, String categoryIdStr) {
        if (name == null || name.trim().isEmpty()) {
            return "Tên sản phẩm không được để trống.";
        }
        try {
            BigDecimal price = new BigDecimal(priceStr.trim());
            if (price.signum() < 0) return "Giá sản phẩm không được âm.";
        } catch (Exception e) {
            return "Giá sản phẩm không hợp lệ.";
        }
        try {
            int qty = Integer.parseInt(quantityStr.trim());
            if (qty < 0) return "Số lượng không được âm.";
        } catch (Exception e) {
            return "Số lượng không hợp lệ.";
        }
        if (categoryIdStr == null || categoryIdStr.trim().isEmpty()) {
            return "Vui lòng chọn danh mục.";
        }
        return null;
    }

    private int parseIntOrDefault(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private String saveFile(DiskFileItem item, String subFolder) throws Exception {
        File dir = new File(Constant.DIR + File.separator + subFolder);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String originalName = item.getName();
        String ext = originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf('.'))
                : "";
        String newFileName = System.currentTimeMillis() + ext;

        File savedFile = new File(dir, newFileName);
        item.write(savedFile.toPath());

        return subFolder + "/" + newFileName;
    }
}
