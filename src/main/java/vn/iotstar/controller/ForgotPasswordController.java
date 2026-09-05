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
 * Bài tập: Quên mật khẩu - Bước 1: nhập email, hệ thống gửi OTP xác nhận qua email.
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/forgot-password")
public class ForgotPasswordController extends HttpServlet {

    private final UserService service = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(Constant.Path.FORGOT_PASSWORD).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String email = req.getParameter("email");
        email = email == null ? "" : email.trim();

        if (email.isEmpty()) {
            req.setAttribute("alert", "Vui lòng nhập email.");
            req.getRequestDispatcher(Constant.Path.FORGOT_PASSWORD).forward(req, resp);
            return;
        }

        // Luôn báo thành công cho người dùng (tránh dò email tồn tại hay không),
        // nhưng chỉ thật sự gửi mail nếu email có trong hệ thống.
        service.sendForgotPasswordOtp(email);

        HttpSession session = req.getSession();
        session.setAttribute("resetEmail", email);
        session.setAttribute("flash", "Nếu email tồn tại trong hệ thống, mã OTP đặt lại mật khẩu đã được gửi tới " + email);
        resp.sendRedirect(req.getContextPath() + "/reset-password");
    }
}
