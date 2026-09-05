-- ============================================
-- Database: ServletCRUDMVC (SQL Server)
-- ============================================
-- QUAN TRỌNG: Từ khi project chuyển sang dùng JPA/Hibernate
-- (xem persistence.xml, hibernate.hbm2ddl.auto = update), bạn KHÔNG cần tự
-- tạo bảng "users", "category", "products" nữa - Hibernate sẽ tự tạo/cập
-- nhật các bảng này (theo entity User/Category/Product) trong lần chạy đầu
-- tiên của ứng dụng. Bạn chỉ cần chạy đúng 1 lệnh dưới đây để tạo database
-- rỗng, rồi build & deploy ứng dụng lên Tomcat (bảng sẽ tự sinh ra):

CREATE DATABASE ServletCRUDMVC;
GO

-- Sau khi deploy ứng dụng lần đầu (Hibernate đã tự tạo xong các bảng),
-- chạy tiếp đoạn script bên dưới để tạo 1 tài khoản admin mẫu
-- (vì luồng /register chỉ tạo tài khoản role=5 - user thường):
--
-- USE ServletCRUDMVC;
-- GO
-- INSERT INTO users(email, username, fullname, password, avatar, roleid, phone, createddate, active)
-- VALUES (N'admin@hcmute.edu.vn', N'admin', N'Quan tri vien', N'123456', NULL, 1, N'0900000000', GETDATE(), 1);
-- GO

-- ============================================
-- (Tham khảo) Cấu trúc bảng do Hibernate tự sinh:
-- ============================================
-- users(id, email, username, fullname, password, avatar, roleid, phone,
--       createddate, active, otp_code, otp_expiry, reset_otp, reset_otp_expiry)
--
-- category(cate_id, cate_name, icons)
--
-- products(product_id, product_name, price, quantity, image, description,
--          created_date, cate_id [FK -> category.cate_id])
