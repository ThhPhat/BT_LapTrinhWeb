# ServletCRUDMVC – Bài tập Servlet/JSP (Cookie, Session, JPA, OTP Email, Product CRUD)

Sinh viên: **24110300 - Võ Thành Phát**

## 1. Nội dung đã thực hiện

1. Login với Cookie ("Nhớ tôi") và Login với Session.
2. CRUD Category (tìm kiếm + phân trang, upload ảnh).
3. Bài tập 01 được viết lại bằng **JPA (Hibernate)** thay cho JDBC thuần
   (xem `persistence.xml`, `util/JPAUtil.java`, các entity trong `model/`,
   các DAO trong `dao/impl/`).
4. Đăng ký tài khoản có **kích hoạt bằng mã OTP gửi qua email**
   (`RegisterController`, `VerifyOtpController`, `EmailUtil`).
5. Đăng nhập (chỉ cho phép tài khoản đã kích hoạt).
6. **Quên mật khẩu** – gửi mã OTP xác nhận qua email để đặt lại mật khẩu
   (`ForgotPasswordController`, `ResetPasswordController`).
7. Bảng **Products** quan hệ 1-n với Category:
   - CRUD Products (trang quản trị `/admin/product/list`).
   - Trang chủ `/home` hiển thị **10 sản phẩm mới nhất**.
   - Trang `/product` hiển thị tất cả sản phẩm, **phân trang 6 sản phẩm/trang**.
   - Trang `/product/detail?id=...` hiển thị chi tiết 1 sản phẩm (bấm vào sản
     phẩm ở trang chủ hoặc trang `/product` sẽ mở trang này).

## 2. Yêu cầu môi trường

- JDK 17+
- Apache Maven 3.8+
- Apache Tomcat 10+ (dùng namespace `jakarta.*`)
- SQL Server (đã cài `sa` hoặc user có quyền tạo DB)

## 3. Cấu hình Database

Project dùng JPA/Hibernate với `hibernate.hbm2ddl.auto=update`, nghĩa là
**không cần tự tạo bảng bằng tay** – Hibernate sẽ tự tạo/cập nhật bảng theo
entity khi ứng dụng khởi động lần đầu.

Bước làm:

1. Mở SQL Server Management Studio (hoặc sqlcmd), chạy:
   ```sql
   CREATE DATABASE ServletCRUDMVC;
   ```
2. Mở file `src/main/resources/META-INF/persistence.xml`, sửa lại đúng
   `jakarta.persistence.jdbc.url`, `jdbc.user`, `jdbc.password` cho khớp với
   SQL Server trên máy bạn (mặc định: `localhost:1433`, user `sa`, password `1452`).
3. Build & deploy ứng dụng (xem mục 5) – Hibernate sẽ tự tạo các bảng
   `users`, `category`, `products`.
4. Tạo 1 tài khoản **admin** mẫu (vì trang `/register` chỉ tạo tài khoản
   thường – roleid = 5) bằng script:
   ```sql
   USE ServletCRUDMVC;
   INSERT INTO users(email, username, fullname, password, avatar, roleid, phone, createddate, active)
   VALUES (N'admin@hcmute.edu.vn', N'admin', N'Quan tri vien', N'123456', NULL, 1, N'0900000000', GETDATE(), 1);
   ```
   (roleid = 1: admin, 5: user thường; `active = 1` để khỏi cần xác thực OTP)

## 4. Cấu hình gửi email OTP (SMTP Gmail)

Mở `src/main/java/vn/iotstar/util/Constant.java`, sửa:
```java
public static final String MAIL_USERNAME = "your-email@gmail.com";
public static final String MAIL_PASSWORD = "your-app-password";
```
- `MAIL_USERNAME`: địa chỉ Gmail dùng để gửi mail.
- `MAIL_PASSWORD`: **KHÔNG dùng mật khẩu Gmail thường.** Vào
  https://myaccount.google.com/apppasswords (yêu cầu tài khoản đã bật xác
  minh 2 bước), tạo 1 "Mật khẩu ứng dụng" (App Password) rồi dán vào đây.

Nếu chưa cấu hình email thật, ứng dụng vẫn chạy được: khi gửi OTP thất bại,
mã OTP sẽ được **in ra console/log của Tomcat** để vẫn có thể test được luồng
kích hoạt tài khoản / quên mật khẩu.

## 5. Build & chạy ứng dụng

```bash
mvn clean package
```

File `target/ServletCRUDMVC.war` sinh ra, copy vào thư mục
`webapps` của Tomcat 10+ (hoặc deploy bằng Eclipse/IntelliJ có tích hợp Tomcat).

Truy cập: `http://localhost:8080/ServletCRUDMVC/`

## 6. Một số luồng để test nhanh

- **Đăng ký + kích hoạt OTP**: vào `/register` → tạo tài khoản → hệ thống
  chuyển tới `/verify-otp` → xem mã OTP trong email (hoặc console log nếu
  chưa cấu hình mail thật) → nhập mã để kích hoạt → đăng nhập.
- **Quên mật khẩu**: ở trang `/login` bấm "Quên mật khẩu?" → nhập email →
  nhận OTP → nhập OTP + mật khẩu mới tại `/reset-password`.
- **Quản lý Category/Product**: đăng nhập bằng tài khoản admin (roleid = 1)
  → menu "Quản lý danh mục" / "Quản lý sản phẩm".
- **Trang chủ / danh sách / chi tiết sản phẩm**: `/home`, `/product`,
  `/product/detail?id=1`.

## 7. Đưa source code lên GitHub

```bash
git init
git add .
git commit -m "ServletCRUDMVC - JPA, OTP email, Product CRUD"
git branch -M main
git remote add origin https://github.com/<username>/<ten-repo>.git
git push -u origin main
```

Sau đó nộp đường link repo GitHub tại UTEXLMS.
