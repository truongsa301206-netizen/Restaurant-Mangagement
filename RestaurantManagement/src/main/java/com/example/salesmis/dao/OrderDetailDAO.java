package com.example.salesmis.dao;

import com.example.salesmis.model.entity.OrderDetail;
import java.util.List;
import java.util.Optional;

public interface OrderDetailDAO {
    List<OrderDetail> findBySalesOrderId(Long salesOrderId);
    Optional<OrderDetail> findById(Long id);
    OrderDetail save(OrderDetail detail);
    OrderDetail update(OrderDetail detail);
    void deleteById(Long id);
}