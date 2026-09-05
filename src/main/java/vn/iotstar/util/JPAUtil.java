package vn.iotstar.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Quản lý EntityManagerFactory dùng chung cho toàn bộ ứng dụng (JPA/Hibernate).
 * Tên persistence-unit phải khớp với "ShoppingPU" khai báo trong persistence.xml.
 */
public class JPAUtil {

    private static final String PERSISTENCE_UNIT_NAME = "ShoppingPU";
    private static volatile EntityManagerFactory factory;

    private JPAUtil() {
    }

    public static EntityManagerFactory getFactory() {
        if (factory == null) {
            synchronized (JPAUtil.class) {
                if (factory == null) {
                    factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
                }
            }
        }
        return factory;
    }

    public static EntityManager getEntityManager() {
        return getFactory().createEntityManager();
    }

    public static void close() {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
    }
}
