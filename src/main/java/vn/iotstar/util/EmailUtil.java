package vn.iotstar.util;

import java.security.SecureRandom;
import java.util.Properties;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

/**
 * Gửi email OTP (kích hoạt tài khoản / quên mật khẩu) qua SMTP Gmail.
 * Cấu hình tài khoản gửi mail trong Constant.MAIL_USERNAME / MAIL_PASSWORD.
 */
public class EmailUtil {

    private static final SecureRandom RANDOM = new SecureRandom();

    /** Sinh mã OTP gồm N chữ số (mặc định 6 số), ví dụ: 048213 */
    public static String generateOtp() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Constant.OTP_LENGTH; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }

    /** Gửi email dạng text đơn giản. Trả về true nếu gửi thành công. */
    public static boolean sendEmail(String toEmail, String subject, String content) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", Constant.MAIL_HOST);
        props.put("mail.smtp.port", Constant.MAIL_PORT);

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Constant.MAIL_USERNAME, Constant.MAIL_PASSWORD);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Constant.MAIL_USERNAME, Constant.MAIL_FROM_NAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject, "UTF-8");
            message.setText(content, "UTF-8");

            Transport.send(message);
            return true;
        } catch (Exception e) {
            // Nếu chưa cấu hình email thật (Constant.MAIL_USERNAME còn để mặc định)
            // thì việc gửi mail sẽ luôn thất bại -> in mã OTP ra console để vẫn test được.
            System.err.println("[EmailUtil] Khong gui duoc email that toi " + toEmail
                    + " (kiem tra lai Constant.MAIL_USERNAME/MAIL_PASSWORD). Loi: " + e.getMessage());
            System.out.println("[EmailUtil] Noi dung email (fallback hien thi tren console):\n" + content);
            return false;
        }
    }

    public static void sendOtpEmail(String toEmail, String otp, String purpose) {
        String subject = "Servlet CRUD MVC - Ma xac thuc OTP";
        String content = "Xin chao,\n\n"
                + "Ma OTP cua ban de " + purpose + " la: " + otp + "\n"
                + "Ma co hieu luc trong " + Constant.OTP_EXPIRE_MINUTES + " phut.\n\n"
                + "Neu ban khong yeu cau thao tac nay, vui long bo qua email nay.\n\n"
                + "Tran trong,\nServletCRUDMVC";
        sendEmail(toEmail, subject, content);
    }
}
