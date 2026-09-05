package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Cookie;

import vn.iotstar.model.User;
import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.Constant;

/**
 * Bài tập: Chức năng đăng nhập tài khoản (Login với Session + Cookie "remember me")
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/login")
public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Nếu đã đăng nhập (còn session) -> vào thẳng /waiting
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute(Constant.SESSION_ACCOUNT) != null) {
            resp.sendRedirect(req.getContextPath() + "/waiting");
            return;
        }

        // Kiểm tra cookie "remember me"
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(Constant.COOKIE_REMEMBER)) {
                    String username = cookie.getValue();
                    UserService service = new UserServiceImpl();
                    User user = service.get(username);
                    if (user != null) {
                        session = req.getSession(true);
                        session.setAttribute(Constant.SESSION_ACCOUNT, user);
                        resp.sendRedirect(req.getContextPath() + "/waiting");
                        return;
                    }
                }
            }
        }

        // Thông báo flash (vd: sau khi đăng ký thành công)
        if (session != null && session.getAttribute("flash") != null) {
            req.setAttribute("flash", session.getAttribute("flash"));
            session.removeAttribute("flash");
        }

        req.getRequestDispatcher(Constant.Path.LOGIN).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        if (username != null) username = username.trim();
        String password = req.getParameter("password");
        String remember = req.getParameter("remember");
        boolean isRememberMe = "on".equals(remember);

        String alertMsg = "";

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            alertMsg = "Tài khoản hoặc mật khẩu không được rỗng";
            req.setAttribute("alert", alertMsg);
            req.getRequestDispatcher(Constant.Path.LOGIN).forward(req, resp);
            return;
        }

        UserService service = new UserServiceImpl();
        User user = service.login(username, password);

        if (user != null) {
            HttpSession session = req.getSession(true);
            session.setAttribute(Constant.SESSION_ACCOUNT, user);
            if (isRememberMe) {
                saveRememberMe(resp, username);
            }
            resp.sendRedirect(req.getContextPath() + "/waiting");
            return;
        }

        // Đăng nhập thất bại: kiểm tra xem có phải do tài khoản chưa kích hoạt OTP hay không
        User existed = service.get(username);
        if (existed != null && password.equals(existed.getPassWord()) && !existed.isActive()) {
            req.getSession().setAttribute("otpUsername", username);
            req.getSession().setAttribute("flash", "Tài khoản chưa kích hoạt. Vui lòng nhập mã OTP đã gửi qua email.");
            resp.sendRedirect(req.getContextPath() + "/verify-otp");
            return;
        }

        alertMsg = "Tài khoản hoặc mật khẩu không đúng";
        req.setAttribute("alert", alertMsg);
        req.getRequestDispatcher(Constant.Path.LOGIN).forward(req, resp);
    }

    private void saveRememberMe(HttpServletResponse response, String username) {
        Cookie cookie = new Cookie(Constant.COOKIE_REMEMBER, username);
        cookie.setMaxAge(30 * 60); // 30 phút
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
