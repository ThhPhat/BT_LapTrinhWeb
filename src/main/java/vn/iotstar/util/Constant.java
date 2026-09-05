package vn.iotstar.util;

import java.io.File;

public class Constant {
    // Thư mục vật lý dùng để lưu file upload (ảnh danh mục, ảnh sản phẩm,...)
    // Tự động trỏ tới thư mục "ServletCRUDMVC/Category" trong home directory của máy đang chạy,
    // nên không cần chỉnh sửa lại khi đem project sang máy khác.
    public static final String DIR = System.getProperty("user.home")
            + File.separator + "ServletCRUDMVC" + File.separator + "Category";

    public static final String SESSION_ACCOUNT = "account";
    public static final String COOKIE_REMEMBER = "username";

    public static final int PAGE_SIZE = 5;          // phân trang cho Category (admin)
    public static final int PRODUCT_PAGE_SIZE = 8;  // phân trang cho trang /product (6 sp/trang)
    public static final int LATEST_PRODUCT_COUNT = 10; // số sản phẩm mới nhất hiển thị ở trang chủ

    // OTP
    public static final int OTP_LENGTH = 6;
    public static final int OTP_EXPIRE_MINUTES = 5;

    // ================== Cấu hình gửi mail (SMTP Gmail) ==================
    // Cách lấy MAIL_PASSWORD: bật xác minh 2 bước cho tài khoản Gmail, sau đó tạo
    // "Mật khẩu ứng dụng" (App Password) tại https://myaccount.google.com/apppasswords
    // rồi dán vào đây (KHÔNG dùng mật khẩu Gmail thường).
    public static final String MAIL_HOST = "smtp.gmail.com";
    public static final String MAIL_PORT = "587";
    public static final String MAIL_USERNAME = "thanhphat.wqe@gmail.com";
    public static final String MAIL_PASSWORD = "rndymeinvvpyrhbk";
    public static final String MAIL_FROM_NAME = "ServletCRUDMVC";

    public static class Path {
        public static final String LOGIN = "/views/login.jsp";
        public static final String REGISTER = "/views/register.jsp";
        public static final String VERIFY_OTP = "/views/verify-otp.jsp";
        public static final String FORGOT_PASSWORD = "/views/forgot-password.jsp";
        public static final String RESET_PASSWORD = "/views/reset-password.jsp";

        public static final String CATEGORY_LIST = "/views/admin/list-category.jsp";
        public static final String CATEGORY_ADD = "/views/admin/add-category.jsp";
        public static final String CATEGORY_EDIT = "/views/admin/edit-category.jsp";

        public static final String PRODUCT_ADMIN_LIST = "/views/admin/list-product.jsp";
        public static final String PRODUCT_ADMIN_ADD = "/views/admin/add-product.jsp";
        public static final String PRODUCT_ADMIN_EDIT = "/views/admin/edit-product.jsp";

        public static final String PRODUCT_LIST = "/views/product-list.jsp";
        public static final String PRODUCT_DETAIL = "/views/product-detail.jsp";
        public static final String HOME = "/views/home.jsp";
    }
}
