package vn.iotstar.controller;

import java.io.File;
import java.io.IOException;
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
import vn.iotstar.model.User;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.impl.CategoryServiceImpl;
import vn.iotstar.util.Constant;

/**
 * Servlet quan ly CRUD Category (danh muc), dung chung 1 servlet cho ca 4 chuc nang:
 *   GET   /admin/category/list    -> danh sach (co tim kiem + phan trang)
 *   GET   /admin/category/add     -> hien thi form them
 *   POST  /admin/category/add     -> xu ly them (upload anh)
 *   GET   /admin/category/edit    -> hien thi form sua
 *   POST  /admin/category/edit    -> xu ly sua (upload anh moi neu co)
 *   GET   /admin/category/delete  -> xoa danh muc
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = {
        "/admin/category/list",
        "/admin/category/add",
        "/admin/category/edit",
        "/admin/category/delete"
})
public class CategoryController extends HttpServlet {

    private final ICategoryService service = new CategoryServiceImpl();

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
            case "/admin/category/add":
                req.getRequestDispatcher(Constant.Path.CATEGORY_ADD).forward(req, resp);
                break;
            case "/admin/category/edit":
                showEditForm(req, resp);
                break;
            case "/admin/category/delete":
                handleDelete(req, resp);
                break;
            case "/admin/category/list":
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

        if ("/admin/category/edit".equals(uri)) {
            doUpdate(req, resp);
        } else {
            doInsert(req, resp);
        }
    }

    /** Chi cho phep tai khoan da dang nhap va co roleid == 1 (admin) truy cap CRUD danh muc */
    private boolean isAdmin(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) return false;
        Object account = session.getAttribute(Constant.SESSION_ACCOUNT);
        return account instanceof User && ((User) account).getRoleid() == 1;
    }

    // ================== LIST (search + pagination) ==================
    private void showList(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String keyword = req.getParameter("keyword");
        int currentPage = parseIntOrDefault(req.getParameter("page"), 1);
        if (currentPage < 1) currentPage = 1;

        int totalItems = service.countByKeyword(keyword);
        int totalPages = (int) Math.ceil((double) totalItems / Constant.PAGE_SIZE);
        if (totalPages < 1) totalPages = 1;
        if (currentPage > totalPages) currentPage = totalPages;

        List<Category> cateList = service.search(keyword, currentPage - 1, Constant.PAGE_SIZE);

        req.setAttribute("cateList", cateList);
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

        req.getRequestDispatcher(Constant.Path.CATEGORY_LIST).forward(req, resp);
    }

    // ================== ADD ==================
    private void doInsert(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String name = null;
        String iconFileName = null;

        if (JakartaServletFileUpload.isMultipartContent(req)) {
            try {
                DiskFileItemFactory factory = DiskFileItemFactory.builder().get();
                JakartaServletFileUpload<DiskFileItem, DiskFileItemFactory> upload =
                        new JakartaServletFileUpload<>(factory);

                for (DiskFileItem item : upload.parseRequest(req)) {
                    if (item.isFormField()) {
                        if ("name".equals(item.getFieldName())) {
                            name = item.getString(StandardCharsets.UTF_8);
                        }
                    } else if (!item.getName().isEmpty()) {
                        iconFileName = saveFile(item, "category");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("error", "Loi khi upload file: " + e.getMessage());
                req.getRequestDispatcher(Constant.Path.CATEGORY_ADD).forward(req, resp);
                return;
            }
        }

        if (name == null || name.trim().isEmpty()) {
            req.setAttribute("error", "Ten danh muc khong duoc de trong.");
            req.getRequestDispatcher(Constant.Path.CATEGORY_ADD).forward(req, resp);
            return;
        }

        Category existed = service.findByCategoryname(name.trim());
        if (existed != null) {
            req.setAttribute("error", "Danh muc \"" + name.trim() + "\" da ton tai.");
            req.getRequestDispatcher(Constant.Path.CATEGORY_ADD).forward(req, resp);
            return;
        }

        Category category = new Category();
        category.setName(name.trim());
        category.setIcon(iconFileName);
        service.insert(category);

        req.getSession().setAttribute("flash", "Them danh muc thanh cong!");
        resp.sendRedirect(req.getContextPath() + "/admin/category/list");
    }

    // ================== EDIT ==================
    private void showEditForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int id = parseIntOrDefault(req.getParameter("id"), -1);
        Category category = service.findById(id);
        if (category == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/category/list");
            return;
        }
        req.setAttribute("category", category);
        req.getRequestDispatcher(Constant.Path.CATEGORY_EDIT).forward(req, resp);
    }

    private void doUpdate(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idParam = null;
        String name = null;
        String newIconFileName = null;

        if (JakartaServletFileUpload.isMultipartContent(req)) {
            try {
                DiskFileItemFactory factory = DiskFileItemFactory.builder().get();
                JakartaServletFileUpload<DiskFileItem, DiskFileItemFactory> upload =
                        new JakartaServletFileUpload<>(factory);

                for (DiskFileItem item : upload.parseRequest(req)) {
                    if (item.isFormField()) {
                        if ("id".equals(item.getFieldName())) {
                            idParam = item.getString(StandardCharsets.UTF_8);
                        } else if ("name".equals(item.getFieldName())) {
                            name = item.getString(StandardCharsets.UTF_8);
                        }
                    } else if (!item.getName().isEmpty()) {
                        newIconFileName = saveFile(item, "category");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("error", "Loi khi upload file: " + e.getMessage());
                req.getRequestDispatcher(Constant.Path.CATEGORY_EDIT).forward(req, resp);
                return;
            }
        }

        int id = parseIntOrDefault(idParam, -1);
        Category category = service.findById(id);
        if (category == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/category/list");
            return;
        }

        if (name == null || name.trim().isEmpty()) {
            req.setAttribute("error", "Ten danh muc khong duoc de trong.");
            req.setAttribute("category", category);
            req.getRequestDispatcher(Constant.Path.CATEGORY_EDIT).forward(req, resp);
            return;
        }

        category.setName(name.trim());
        if (newIconFileName != null) {
            category.setIcon(newIconFileName);
        }
        service.update(category);

        req.getSession().setAttribute("flash", "Cap nhat danh muc thanh cong!");
        resp.sendRedirect(req.getContextPath() + "/admin/category/list");
    }

    // ================== DELETE ==================
    private void handleDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = parseIntOrDefault(req.getParameter("id"), -1);
        try {
            service.delete(id);
            req.getSession().setAttribute("flash", "Xoa danh muc thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        resp.sendRedirect(req.getContextPath() + "/admin/category/list");
    }

    // ================== helpers ==================
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
