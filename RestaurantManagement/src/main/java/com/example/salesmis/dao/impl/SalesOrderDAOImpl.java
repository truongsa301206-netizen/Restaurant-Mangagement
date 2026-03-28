package com.example.salesmis.dao.impl;

import com.example.salesmis.config.JpaUtil;
import com.example.salesmis.dao.SalesOrderDAO;
import com.example.salesmis.model.entity.SalesOrder;
import com.example.salesmis.model.entity.SalesOrder.TrangThaiDon;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class SalesOrderDAOImpl implements SalesOrderDAO {

    @Override
    public List<SalesOrder> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            /*
             * JOIN FETCH details: load chi tiết hóa đơn trong cùng 1 query
             * để tránh N+1 problem khi lặp qua danh sách.
             * JOIN FETCH kh: load luôn KhachHang cho hiển thị.
             */
            return em.createQuery("""
                SELECT DISTINCT so
                FROM SalesOrder so
                LEFT JOIN FETCH so.khachHang kh
                LEFT JOIN FETCH so.details d
                LEFT JOIN FETCH d.monAn
                ORDER BY so.ngay DESC
                """, SalesOrder.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<SalesOrder> findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            /*
             * Dùng JPQL thay vì em.find() để có thể JOIN FETCH luôn details.
             * em.find() chỉ load entity, không load association LAZY.
             */
            List<SalesOrder> result = em.createQuery("""
                SELECT so
                FROM SalesOrder so
                LEFT JOIN FETCH so.khachHang
                LEFT JOIN FETCH so.nhanVien
                LEFT JOIN FETCH so.ban
                LEFT JOIN FETCH so.details d
                LEFT JOIN FETCH d.monAn
                WHERE so.id = :id
                """, SalesOrder.class)
                    .setParameter("id", id)
                    .getResultList();
            return result.stream().findFirst();
        } finally {
            em.close();
        }
    }

    @Override
    public List<SalesOrder> findByKhachHangId(Long khachHangId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                SELECT so
                FROM SalesOrder so
                LEFT JOIN FETCH so.khachHang kh
                WHERE kh.id = :khachHangId
                ORDER BY so.ngay DESC
                """, SalesOrder.class)
                    .setParameter("khachHangId", khachHangId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<SalesOrder> findByTrangThai(TrangThaiDon trangThai) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                SELECT so
                FROM SalesOrder so
                LEFT JOIN FETCH so.khachHang
                WHERE so.trangThai = :trangThai
                ORDER BY so.ngay DESC
                """, SalesOrder.class)
                    .setParameter("trangThai", trangThai)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<SalesOrder> findByDateRange(LocalDate fromDate, LocalDate toDate) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            LocalDateTime from = fromDate.atStartOfDay();
            LocalDateTime to   = toDate.atTime(23, 59, 59);
            return em.createQuery("""
                SELECT so
                FROM SalesOrder so
                LEFT JOIN FETCH so.khachHang
                WHERE so.ngay BETWEEN :from AND :to
                ORDER BY so.ngay DESC
                """, SalesOrder.class)
                    .setParameter("from", from)
                    .setParameter("to", to)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public SalesOrder save(SalesOrder salesOrder) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            /*
             * persist() lưu entity mới.
             * cascade = ALL trên @OneToMany sẽ tự persist toàn bộ details.
             */
            em.persist(salesOrder);
            tx.commit();
            return salesOrder;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public SalesOrder update(SalesOrder salesOrder) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            /*
             * merge() cập nhật entity đã có id.
             * orphanRemoval = true trên @OneToMany:
             *   → dòng chi tiết bị xóa khỏi list sẽ bị DELETE trong DB.
             */
            SalesOrder merged = em.merge(salesOrder);
            tx.commit();
            return merged;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            SalesOrder salesOrder = em.find(SalesOrder.class, id);
            if (salesOrder != null) {
                /*
                 * cascade = ALL + orphanRemoval = true:
                 * xóa SalesOrder sẽ tự động xóa toàn bộ OrderDetail.
                 */
                em.remove(salesOrder);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}