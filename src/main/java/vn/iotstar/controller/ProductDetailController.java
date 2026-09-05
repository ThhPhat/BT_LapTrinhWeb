package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import vn.iotstar.model.Product;
import vn.iotstar.service.IProductService;
import vn.iotstar.service.impl.ProductServiceImpl;
import vn.iotstar.util.Constant;

/**
 * Bài tập: Hiển thị chi tiết 1 sản phẩm khi bấm chuột vào sản phẩm đó
 * (từ trang chủ hoặc từ trang /product).
 * URL: /product/detail?id=...
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/product/detail")
public class ProductDetailController extends HttpServlet {

    private final IProductService productService = new ProductServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int id = parseIntOrDefault(req.getParameter("id"), -1);
        Product product = productService.findById(id);

        if (product == null) {
            resp.sendRedirect(req.getContextPath() + "/product");
            return;
        }

        req.setAttribute("product", product);
        req.getRequestDispatcher(Constant.Path.PRODUCT_DETAIL).forward(req, resp);
    }

    private int parseIntOrDefault(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
