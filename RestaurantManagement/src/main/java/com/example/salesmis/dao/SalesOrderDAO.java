package com.example.salesmis.dao;

import com.example.salesmis.model.entity.SalesOrder;
import com.example.salesmis.model.entity.SalesOrder.TrangThaiDon;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SalesOrderDAO {

    /** Lấy toàn bộ đơn hàng, sắp xếp mới nhất trước. */
    List<SalesOrder> findAll();

    /** Tìm theo ID, trả Optional để tránh null. */
    Optional<SalesOrder> findById(Long id);

    /** Tìm các đơn hàng của một khách hàng. */
    List<SalesOrder> findByKhachHangId(Long khachHangId);

    /** Tìm theo trạng thái (MOI / HOANTHANH / HUY). */
    List<SalesOrder> findByTrangThai(TrangThaiDon trangThai);

    /** Tìm đơn hàng trong khoảng ngày (inclusive). */
    List<SalesOrder> findByDateRange(LocalDate fromDate, LocalDate toDate);

    /** Lưu đơn hàng MỚI vào DB (persist). */
    SalesOrder save(SalesOrder salesOrder);

    /** Cập nhật đơn hàng ĐÃ TỒN TẠI (merge). */
    SalesOrder update(SalesOrder salesOrder);

    /** Xóa đơn hàng (cascade xóa luôn CTHD). */
    void deleteById(Long id);
}