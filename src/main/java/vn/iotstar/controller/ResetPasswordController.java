package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.Constant;

/**
 * Bài tập: Quên mật khẩu - Bước 2: xác thực OTP + đặt mật khẩu mới.
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/reset-password")
public class ResetPasswordController extends HttpServlet {

    private final UserService service = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        String email = (String) session.getAttribute("resetEmail");
        if (email == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }
        req.setAttribute("email", email);
        if (session.getAttribute("flash") != null) {
            req.setAttribute("flash", session.getAttribute("flash"));
            session.removeAttribute("flash");
        }
        req.getRequestDispatcher(Constant.Path.RESET_PASSWORD).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        String email = (String) session.getAttribute("resetEmail");
        if (email == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }

        String otp = req.getParameter("otp");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        if (otp == null || otp.trim().isEmpty() || newPassword == null || newPassword.isEmpty()) {
            showError(req, resp, email, "Vui lòng nhập đầy đủ mã OTP và mật khẩu mới.");
            return;
        }
        if (newPassword.length() < 6) {
            showError(req, resp, email, "Mật khẩu mới phải có ít nhất 6 ký tự.");
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            showError(req, resp, email, "Mật khẩu nhập lại không khớp.");
            return;
        }

        boolean ok = service.resetPassword(email, otp.trim(), newPassword);
        if (ok) {
            session.removeAttribute("resetEmail");
            session.setAttribute("flash", "Đặt lại mật khẩu thành công! Vui lòng đăng nhập bằng mật khẩu mới.");
            resp.sendRedirect(req.getContextPath() + "/login");
        } else {
            showError(req, resp, email, "Mã OTP không đúng hoặc đã hết hạn.");
        }
    }

    private void showError(HttpServletRequest req, HttpServletResponse resp, String email, String message)
            throws ServletException, IOException {
        req.setAttribute("email", email);
        req.setAttribute("alert", message);
        req.getRequestDispatcher(Constant.Path.RESET_PASSWORD).forward(req, resp);
    }
}
