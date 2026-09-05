package vn.iotstar.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import vn.iotstar.model.Product;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.IProductService;
import vn.iotstar.service.impl.CategoryServiceImpl;
import vn.iotstar.service.impl.ProductServiceImpl;
import vn.iotstar.util.Constant;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.impl.CategoryServiceImpl;

/**
 * Bài tập: Hiển thị tất cả sản phẩm, phân trang 6 sản phẩm/trang, tại URL /product
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/product")
public class ProductListController extends HttpServlet {
    
    private final IProductService productService = new ProductServiceImpl();
    private final ICategoryService categoryService = new CategoryServiceImpl(); // thêm dòng này
    

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int currentPage = parseIntOrDefault(req.getParameter("page"), 1);
        if (currentPage < 1) currentPage = 1;

        int categoryId = parseIntOrDefault(req.getParameter("categoryId"), -1);

        int totalItems = categoryId > 0
                ? productService.countByCategory(categoryId)
                : productService.count();

        int totalPages = (int) Math.ceil((double) totalItems / Constant.PRODUCT_PAGE_SIZE);
        if (totalPages < 1) totalPages = 1;
        if (currentPage > totalPages) currentPage = totalPages;

        List<Product> productList = categoryId > 0
                ? productService.findByCategory(categoryId, currentPage - 1, Constant.PRODUCT_PAGE_SIZE)
                : productService.findAll(currentPage - 1, Constant.PRODUCT_PAGE_SIZE);

        req.setAttribute("productList", productList);
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalItems", totalItems);
        req.setAttribute("categoryId", categoryId);
        req.setAttribute("categories", categoryService.findAll());

        req.getRequestDispatcher(Constant.Path.PRODUCT_LIST).forward(req, resp);
    }

    private int parseIntOrDefault(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
