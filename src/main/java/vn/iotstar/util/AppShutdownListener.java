package vn.iotstar.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Đảm bảo EntityManagerFactory (Hibernate) được đóng đúng cách khi ứng dụng
 * dừng/undeploy. Nếu không có listener này, các kết nối/luồng nền của
 * Hibernate có thể khiến Tomcat không dừng được trong thời gian timeout
 * (cảnh báo "Server Tomcat did not stop within timeout" trên VS Code/Eclipse).
 */
@WebListener
public class AppShutdownListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Không cần làm gì đặc biệt lúc khởi động; EntityManagerFactory được
        // khởi tạo lười (lazy) khi có request JPA đầu tiên, xem JPAUtil.
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JPAUtil.close();
    }
}
