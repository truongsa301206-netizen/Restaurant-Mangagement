package com.example.salesmis.dao.impl;

import com.example.salesmis.config.JpaUtil;
import com.example.salesmis.dao.MonAnDAO;
import com.example.salesmis.model.entity.MonAn;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class MonAnDAOImpl implements MonAnDAO {

    @Override
    public List<MonAn> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                SELECT m FROM MonAn m ORDER BY m.tenMon
                """, MonAn.class).getResultList();
        } finally { em.close(); }
    }

    @Override
    public List<MonAn> findAllActive() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                SELECT m FROM MonAn m
                WHERE m.trangThai = TRUE
                ORDER BY m.tenMon
                """, MonAn.class).getResultList();
        } finally { em.close(); }
    }

    @Override
    public Optional<MonAn> findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(MonAn.class, id));
        } finally { em.close(); }
    }

    @Override
    public List<MonAn> searchByKeyword(String keyword) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                SELECT m FROM MonAn m
                WHERE LOWER(m.tenMon) LIKE LOWER(:kw)
                ORDER BY m.tenMon
                """, MonAn.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .getResultList();
        } finally { em.close(); }
    }

    @Override
    public MonAn save(MonAn monAn) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(monAn);
            tx.commit();
            return monAn;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally { em.close(); }
    }

    @Override
    public MonAn update(MonAn monAn) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            MonAn merged = em.merge(monAn);
            tx.commit();
            return merged;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally { em.close(); }
    }

    @Override
    public void deleteById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            MonAn m = em.find(MonAn.class, id);
            if (m != null) em.remove(m);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally { em.close(); }
    }

    @Override
    public boolean existsByTenMon(String tenMon) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Long count = em.createQuery("""
                SELECT COUNT(m) FROM MonAn m
                WHERE LOWER(m.tenMon) = LOWER(:tenMon)
                """, Long.class)
                    .setParameter("tenMon", tenMon)
                    .getSingleResult();
            return count != null && count > 0;
        } finally { em.close(); }
    }
}