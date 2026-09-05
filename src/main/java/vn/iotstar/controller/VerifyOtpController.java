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
 * Bài tập: Kích hoạt tài khoản bằng OTP gửi qua email khi đăng ký.
 * GET  /verify-otp          -> hiển thị form nhập OTP
 * POST /verify-otp          -> xác thực OTP, kích hoạt tài khoản
 * GET  /verify-otp?resend=1 -> gửi lại mã OTP mới
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/verify-otp")
public class VerifyOtpController extends HttpServlet {

    private final UserService service = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        String username = (String) session.getAttribute("otpUsername");
        if (username == null) {
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }

        if ("1".equals(req.getParameter("resend"))) {
            boolean sent = service.resendActivationOtp(username);
            req.setAttribute("flash", sent
                    ? "Đã gửi lại mã OTP mới, vui lòng kiểm tra email."
                    : "Không thể gửi lại mã OTP (tài khoản đã kích hoạt hoặc không tồn tại).");
        }

        if (session.getAttribute("flash") != null) {
            req.setAttribute("flash", session.getAttribute("flash"));
            session.removeAttribute("flash");
        }

        req.setAttribute("username", username);
        req.getRequestDispatcher(Constant.Path.VERIFY_OTP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession();
        String username = (String) session.getAttribute("otpUsername");
        if (username == null) {
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }

        String otp = req.getParameter("otp");
        boolean ok = otp != null && service.verifyActivationOtp(username, otp.trim());

        if (ok) {
            session.removeAttribute("otpUsername");
            session.setAttribute("flash", "Kích hoạt tài khoản thành công! Vui lòng đăng nhập.");
            resp.sendRedirect(req.getContextPath() + "/login");
        } else {
            req.setAttribute("username", username);
            req.setAttribute("alert", "Mã OTP không đúng hoặc đã hết hạn. Vui lòng thử lại hoặc bấm gửi lại mã.");
            req.getRequestDispatcher(Constant.Path.VERIFY_OTP).forward(req, resp);
        }
    }
}
