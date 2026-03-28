package com.example.salesmis.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Singleton quản lý EntityManagerFactory cho toàn bộ ứng dụng.
 */
public class JpaUtil {

    private static final String PERSISTENCE_UNIT_NAME = "salesmis-pu";
    private static final EntityManagerFactory emf;

    static {
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(
                    "Không thể khởi tạo JPA EntityManagerFactory: " + e.getMessage()); //
        }
    }

    private JpaUtil() {}

    public static EntityManager getEntityManager() {
        return emf.createEntityManager(); //
    }

    public static void closeFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close(); //
        }
    }
}