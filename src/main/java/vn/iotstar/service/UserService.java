package vn.iotstar.service;

import vn.iotstar.model.User;

public interface UserService {
    User login(String username, String password);
    User get(String username);
    User getByEmail(String email);
    void insert(User user);
    void update(User user);

    boolean checkExistEmail(String email);
    boolean checkExistUsername(String username);
    boolean checkExistPhone(String phone);

    /** Đăng ký tài khoản mới ở trạng thái CHƯA kích hoạt + gửi OTP kích hoạt qua email. */
    boolean register(String username, String password, String email, String fullname, String phone);

    /** Gửi lại mã OTP kích hoạt cho 1 username (dùng khi mã cũ hết hạn / chưa nhận được mail). */
    boolean resendActivationOtp(String username);

    /** Xác thực OTP kích hoạt tài khoản. Trả về true nếu kích hoạt thành công. */
    boolean verifyActivationOtp(String username, String otp);

    /** Bước 1 quên mật khẩu: sinh OTP và gửi qua email nếu email tồn tại. */
    boolean sendForgotPasswordOtp(String email);

    /** Bước 2 quên mật khẩu: xác thực OTP + đổi mật khẩu mới. */
    boolean resetPassword(String email, String otp, String newPassword);
}
