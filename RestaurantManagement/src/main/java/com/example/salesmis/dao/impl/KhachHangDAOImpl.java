package com.example.salesmis.dao.impl;

import com.example.salesmis.config.JpaUtil;
import com.example.salesmis.dao.KhachHangDAO;
import com.example.salesmis.model.entity.KhachHang;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class KhachHangDAOImpl implements KhachHangDAO {

    @Override
    public List<KhachHang> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT kh FROM KhachHang kh ORDER BY kh.ten", KhachHang.class)
                    .getResultList();
        } finally {
            em.close(); // [cite: 39]
        }
    }

    @Override
    public Optional<KhachHang> findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(KhachHang.class, id));
        } finally {
            em.close();
        }
    }

    @Override
    public List<KhachHang> searchByKeyword(String keyword) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT kh FROM KhachHang kh WHERE LOWER(kh.ten) LIKE LOWER(:kw) OR kh.sdt LIKE :kw ORDER BY kh.ten",
                            KhachHang.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public KhachHang save(KhachHang khachHang) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(khachHang);
            tx.commit();
            return khachHang;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback(); // [cite: 39]
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public KhachHang update(KhachHang khachHang) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            KhachHang merged = em.merge(khachHang);
            tx.commit();
            return merged;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
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
            KhachHang kh = em.find(KhachHang.class, id);
            if (kh != null) {
                em.remove(kh);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean existsBySdt(String sdt) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(kh) FROM KhachHang kh WHERE kh.sdt = :sdt", Long.class)
                    .setParameter("sdt", sdt)
                    .getSingleResult();
            return count != null && count > 0;
        } finally {
            em.close();
        }
    }
}