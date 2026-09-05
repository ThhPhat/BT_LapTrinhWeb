package vn.iotstar.service.impl;

import java.time.LocalDateTime;

import vn.iotstar.dao.UserDao;
import vn.iotstar.dao.impl.UserDaoImpl;
import vn.iotstar.model.User;
import vn.iotstar.service.UserService;
import vn.iotstar.util.Constant;
import vn.iotstar.util.EmailUtil;

public class UserServiceImpl implements UserService {

    UserDao userDao = new UserDaoImpl();

    @Override
    public User login(String username, String password) {
        User user = this.get(username);
        // Chỉ cho đăng nhập khi mật khẩu đúng VÀ tài khoản đã kích hoạt (active = true)
        if (user != null && password.equals(user.getPassWord()) && user.isActive()) {
            return user;
        }
        return null;
    }

    @Override
    public User get(String username) {
        return userDao.get(username);
    }

    @Override
    public User getByEmail(String email) {
        return userDao.getByEmail(email);
    }

    @Override
    public void insert(User user) {
        userDao.insert(user);
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public boolean checkExistEmail(String email) {
        return userDao.checkExistEmail(email);
    }

    @Override
    public boolean checkExistUsername(String username) {
        return userDao.checkExistUsername(username);
    }

    @Override
    public boolean checkExistPhone(String phone) {
        return userDao.checkExistPhone(phone);
    }

    @Override
    public boolean register(String username, String password, String email, String fullname, String phone) {
        if (userDao.checkExistUsername(username)) {
            return false;
        }
        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);

        User user = new User(email, username, fullname, password, null, 5, phone, date);
        user.setActive(false);
        user.setOtpCode(EmailUtil.generateOtp());
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(Constant.OTP_EXPIRE_MINUTES));

        userDao.insert(user);
        EmailUtil.sendOtpEmail(email, user.getOtpCode(), "kich hoat tai khoan");
        return true;
    }

    @Override
    public boolean resendActivationOtp(String username) {
        User user = userDao.get(username);
        if (user == null || user.isActive()) {
            return false;
        }
        user.setOtpCode(EmailUtil.generateOtp());
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(Constant.OTP_EXPIRE_MINUTES));
        userDao.update(user);
        EmailUtil.sendOtpEmail(user.getEmail(), user.getOtpCode(), "kich hoat tai khoan");
        return true;
    }

    @Override
    public boolean verifyActivationOtp(String username, String otp) {
        User user = userDao.get(username);
        if (user == null || user.isActive() || user.getOtpCode() == null) {
            return false;
        }
        if (user.getOtpExpiry() == null || LocalDateTime.now().isAfter(user.getOtpExpiry())) {
            return false; // OTP het han
        }
        if (!user.getOtpCode().equals(otp)) {
            return false; // OTP sai
        }
        user.setActive(true);
        user.setOtpCode(null);
        user.setOtpExpiry(null);
        userDao.update(user);
        return true;
    }

    @Override
    public boolean sendForgotPasswordOtp(String email) {
        User user = userDao.getByEmail(email);
        if (user == null) {
            return false; // khong tiet lo email khong ton tai o Controller de tranh do tham
        }
        user.setResetOtp(EmailUtil.generateOtp());
        user.setResetOtpExpiry(LocalDateTime.now().plusMinutes(Constant.OTP_EXPIRE_MINUTES));
        userDao.update(user);
        EmailUtil.sendOtpEmail(email, user.getResetOtp(), "dat lai mat khau");
        return true;
    }

    @Override
    public boolean resetPassword(String email, String otp, String newPassword) {
        User user = userDao.getByEmail(email);
        if (user == null || user.getResetOtp() == null) {
            return false;
        }
        if (user.getResetOtpExpiry() == null || LocalDateTime.now().isAfter(user.getResetOtpExpiry())) {
            return false; // OTP het han
        }
        if (!user.getResetOtp().equals(otp)) {
            return false; // OTP sai
        }
        user.setPassWord(newPassword);
        user.setResetOtp(null);
        user.setResetOtpExpiry(null);
        userDao.update(user);
        return true;
    }
}
