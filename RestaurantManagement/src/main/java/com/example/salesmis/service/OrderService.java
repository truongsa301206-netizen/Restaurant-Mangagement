package com.example.salesmis.service;

import com.example.salesmis.model.entity.SalesOrder;
import com.example.salesmis.model.entity.SalesOrder.TrangThaiDon;
import java.util.List;
import java.util.Map;

public interface OrderService {

    List<SalesOrder> getAllOrders();

    List<SalesOrder> getOrdersByTrangThai(TrangThaiDon trangThai);

    SalesOrder getOrderById(Long id);

    /**
     * Tạo đơn hàng mới.
     *
     * @param khachHangId  ID khách hàng (có thể null nếu khách vãng lai)
     * @param items        Map<monAnId, soLuong> — danh sách món và số lượng
     * @return SalesOrder đã được lưu vào DB
     */
    SalesOrder createOrder(Long khachHangId, Map<Long, Integer> items);

    /**
     * Cập nhật trạng thái đơn hàng.
     * Khi chuyển sang HOANTHANH: không trừ tồn kho (nhà hàng khác shop).
     * Khi chuyển sang HUY: không cần rollback tồn kho.
     */
    SalesOrder updateTrangThai(Long orderId, TrangThaiDon trangThaiMoi);

    /**
     * Thêm một món vào đơn hàng đang MỞ.
     */
    SalesOrder addMonAnToOrder(Long orderId, Long monAnId, Integer soLuong);

    /**
     * Xóa một dòng chi tiết khỏi đơn hàng đang MỞ.
     */
    SalesOrder removeDetailFromOrder(Long orderId, Long detailId);

    void deleteOrder(Long id);
}