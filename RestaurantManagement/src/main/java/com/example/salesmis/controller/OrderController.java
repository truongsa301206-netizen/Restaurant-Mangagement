package com.example.salesmis.controller;

import com.example.salesmis.model.entity.KhachHang;
import com.example.salesmis.model.entity.MonAn;
import com.example.salesmis.model.entity.SalesOrder;
import com.example.salesmis.model.entity.SalesOrder.TrangThaiDon;
import com.example.salesmis.service.LookupService;
import com.example.salesmis.service.OrderService;

import java.util.List;
import java.util.Map;

public class OrderController {

    private final OrderService  orderService;
    private final LookupService lookupService;

    public OrderController(OrderService orderService, LookupService lookupService) {
        this.orderService  = orderService;
        this.lookupService = lookupService;
    }

    public List<KhachHang> getAllKhachHang() {
        return lookupService.getAllKhachHang();
    }

    public List<MonAn> getAllActiveMonAn() {
        return lookupService.getAllActiveMonAn();
    }

    public List<SalesOrder> getAllOrders() {
        return orderService.getAllOrders();
    }

    public List<SalesOrder> getOrdersByTrangThai(String trangThaiStr) {
        if (trangThaiStr == null || trangThaiStr.trim().isEmpty() || trangThaiStr.equals("TẤT CẢ")) {
            return orderService.getAllOrders();
        }
        TrangThaiDon trangThai = parseTrangThai(trangThaiStr);
        return orderService.getOrdersByTrangThai(trangThai);
    }

    public SalesOrder getOrderById(Long id) {
        return orderService.getOrderById(id);
    }

    public SalesOrder createOrder(String khachHangIdStr, Map<Long, Integer> items) {
        Long khachHangId = parseOptionalLong(khachHangIdStr, "ID khách hàng");
        return orderService.createOrder(khachHangId, items);
    }

    public SalesOrder updateTrangThai(Long orderId, String trangThaiStr) {
        TrangThaiDon trangThai = parseTrangThai(trangThaiStr);
        return orderService.updateTrangThai(orderId, trangThai);
    }

    public SalesOrder addMonAnToOrder(Long orderId, Long monAnId, String soLuongStr) {
        Integer soLuong = parseInteger(soLuongStr, "Số lượng");
        return orderService.addMonAnToOrder(orderId, monAnId, soLuong);
    }

    public SalesOrder removeDetailFromOrder(Long orderId, Long detailId) {
        return orderService.removeDetailFromOrder(orderId, detailId);
    }

    public void deleteOrder(Long id) {
        orderService.deleteOrder(id);
    }

    private Integer parseInteger(String value, String fieldName) {
        try {
            return Integer.valueOf(value.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException(fieldName + " phải là số nguyên hợp lệ.");
        }
    }

    private Long parseOptionalLong(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Long.valueOf(value.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException(fieldName + " phải là số nguyên hợp lệ.");
        }
    }

    private TrangThaiDon parseTrangThai(String value) {
        try {
            return TrangThaiDon.valueOf(value.trim().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Trạng thái không hợp lệ: " + value + ". Giá trị hợp lệ: MOI, HOANTHANH, HUY");
        }
    }
}