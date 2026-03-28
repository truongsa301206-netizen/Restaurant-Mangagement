package com.example.salesmis.dao.impl;

import com.example.salesmis.config.JpaUtil;
import com.example.salesmis.dao.OrderDetailDAO;
import com.example.salesmis.model.entity.OrderDetail;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class OrderDetailDAOImpl implements OrderDetailDAO {

    @Override
    public List<OrderDetail> findBySalesOrderId(Long salesOrderId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT d FROM OrderDetail d WHERE d.salesOrder.id = :orderId", OrderDetail.class)
                    .setParameter("orderId", salesOrderId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<OrderDetail> findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(OrderDetail.class, id));
        } finally {
            em.close();
        }
    }

    @Override
    public OrderDetail save(OrderDetail detail) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(detail);
            tx.commit();
            return detail;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public OrderDetail update(OrderDetail detail) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            OrderDetail merged = em.merge(detail);
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
            OrderDetail d = em.find(OrderDetail.class, id);
            if (d != null) {
                em.remove(d);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}