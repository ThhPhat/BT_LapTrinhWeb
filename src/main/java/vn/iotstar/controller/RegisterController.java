package vn.iotstar.controller;

import java.io.IOException;
import java.util.regex.Pattern;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Cookie;

import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.Constant;

/**
 * Bài tập: Chức năng đăng ký tài khoản
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/register")
public class RegisterController extends HttpServlet {

    UserService service = new UserServiceImpl();

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[0-9]{9,11}$");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute(Constant.SESSION_ACCOUNT) != null) {
            resp.sendRedirect(req.getContextPath() + "/waiting");
            return;
        }

        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(Constant.COOKIE_REMEMBER)) {
                    resp.sendRedirect(req.getContextPath() + "/waiting");
                    return;
                }
            }
        }
        req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");

        String username = trim(req.getParameter("username"));
        String password = req.getParameter("password");
        String email = trim(req.getParameter("email"));
        String fullname = trim(req.getParameter("fullname"));
        String phone = trim(req.getParameter("phone"));

        // 1. Validate dữ liệu bắt buộc
        if (username.isEmpty() || password == null || password.isEmpty()
                || email.isEmpty() || fullname.isEmpty() || phone.isEmpty()) {
            showError(req, resp, "Vui lòng điền đầy đủ thông tin.");
            return;
        }
        if (password.length() < 6) {
            showError(req, resp, "Mật khẩu phải có ít nhất 6 ký tự.");
            return;
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            showError(req, resp, "Email không đúng định dạng.");
            return;
        }
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            showError(req, resp, "Số điện thoại không hợp lệ (9-11 chữ số).");
            return;
        }

        // 2. Kiểm tra trùng lặp
        if (service.checkExistEmail(email)) {
            showError(req, resp, "Email đã tồn tại!");
            return;
        }
        if (service.checkExistUsername(username)) {
            showError(req, resp, "Tài khoản đã tồn tại!");
            return;
        }
        if (service.checkExistPhone(phone)) {
            showError(req, resp, "Số điện thoại đã được sử dụng!");
            return;
        }

        // 3. Đăng ký (tài khoản sẽ ở trạng thái CHƯA kích hoạt, hệ thống gửi OTP qua email)
        boolean isSuccess = service.register(username, password, email, fullname, phone);
        if (isSuccess) {
            HttpSession session = req.getSession();
            session.setAttribute("otpUsername", username);
            session.setAttribute("flash",
                    "Đăng ký thành công! Vui lòng kiểm tra email " + email + " để lấy mã OTP kích hoạt tài khoản.");
            resp.sendRedirect(req.getContextPath() + "/verify-otp");
        } else {
            showError(req, resp, "System error!");
        }
    }

    private String trim(String s) {
        return s == null ? "" : s.trim();
    }

    private void showError(HttpServletRequest req, HttpServletResponse resp, String message)
            throws ServletException, IOException {
        req.setAttribute("alert", message);
        req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
    }
}
