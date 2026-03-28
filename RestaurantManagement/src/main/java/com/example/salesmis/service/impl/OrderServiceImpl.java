package com.example.salesmis.service.impl;

import com.example.salesmis.dao.KhachHangDAO;
import com.example.salesmis.dao.MonAnDAO;
import com.example.salesmis.dao.SalesOrderDAO;
import com.example.salesmis.model.entity.*;
import com.example.salesmis.model.entity.SalesOrder.TrangThaiDon;
import com.example.salesmis.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class OrderServiceImpl implements OrderService {

    private final SalesOrderDAO salesOrderDAO;
    private final KhachHangDAO  khachHangDAO;
    private final MonAnDAO      monAnDAO;

    public OrderServiceImpl(SalesOrderDAO salesOrderDAO,
                            KhachHangDAO khachHangDAO,
                            MonAnDAO monAnDAO) {
        this.salesOrderDAO = salesOrderDAO;
        this.khachHangDAO  = khachHangDAO;
        this.monAnDAO      = monAnDAO;
    }

    @Override
    public List<SalesOrder> getAllOrders() {
        return salesOrderDAO.findAll();
    }

    @Override
    public List<SalesOrder> getOrdersByTrangThai(TrangThaiDon trangThai) {
        if (trangThai == null) {
            return salesOrderDAO.findAll();
        }
        return salesOrderDAO.findByTrangThai(trangThai);
    }

    @Override
    public SalesOrder getOrderById(Long id) {
        return salesOrderDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Không tìm thấy đơn hàng với id = " + id));
    }

    @Override
    public SalesOrder createOrder(Long khachHangId, Map<Long, Integer> items) {
        // 1. Validate: phải có ít nhất một món
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException(
                    "Đơn hàng phải có ít nhất một món ăn.");
        }

        // 2. Validate số lượng từng món
        for (Map.Entry<Long, Integer> entry : items.entrySet()) {
            if (entry.getValue() == null || entry.getValue() <= 0) {
                throw new IllegalArgumentException(
                        "Số lượng phải lớn hơn 0 cho mỗi món ăn.");
            }
        }

        // 3. Xây dựng SalesOrder header
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setNgay(LocalDateTime.now());
        salesOrder.setTrangThai(TrangThaiDon.MOI);

        // 4. Gán KhachHang nếu có
        if (khachHangId != null) {
            KhachHang khachHang = khachHangDAO.findById(khachHangId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Không tìm thấy khách hàng với id = " + khachHangId));
            salesOrder.setKhachHang(khachHang);
        }

        // 5. Thêm từng dòng chi tiết
        for (Map.Entry<Long, Integer> entry : items.entrySet()) {
            Long    monAnId  = entry.getKey();
            Integer soLuong  = entry.getValue();

            MonAn monAn = monAnDAO.findById(monAnId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Không tìm thấy món ăn với id = " + monAnId));

            if (monAn.getTrangThai() == null || !monAn.getTrangThai()) {
                throw new IllegalArgumentException(
                        "Món ăn '" + monAn.getTenMon() + "' hiện không còn phục vụ.");
            }

            // Snapshot giá tại thời điểm đặt hàng
            BigDecimal donGia = monAn.getGia();
            OrderDetail detail = new OrderDetail(monAn, soLuong, donGia);

            // addDetail() liên kết ngược detail.salesOrder = salesOrder
            salesOrder.addDetail(detail);
        }

        // 6. Lưu vào DB (cascade ALL sẽ persist toàn bộ detail)
        return salesOrderDAO.save(salesOrder);
    }

    @Override
    public SalesOrder updateTrangThai(Long orderId, TrangThaiDon trangThaiMoi) {
        if (trangThaiMoi == null) {
            throw new IllegalArgumentException("Trạng thái mới không được null.");
        }

        SalesOrder salesOrder = getOrderById(orderId);

        // Business rule: không cho phép chuyển ngược từ HOANTHANH / HUY
        if (salesOrder.getTrangThai() == TrangThaiDon.HOANTHANH
                || salesOrder.getTrangThai() == TrangThaiDon.HUY) {
            throw new IllegalArgumentException(
                    "Không thể thay đổi trạng thái của đơn hàng đã hoàn thành hoặc đã hủy.");
        }

        salesOrder.setTrangThai(trangThaiMoi);
        return salesOrderDAO.update(salesOrder);
    }

    @Override
    public SalesOrder addMonAnToOrder(Long orderId, Long monAnId, Integer soLuong) {
        validateSoLuong(soLuong);

        SalesOrder salesOrder = getOrderById(orderId);

        // Business rule: chỉ thêm món vào đơn đang MỞ
        if (salesOrder.getTrangThai() != TrangThaiDon.MOI) {
            throw new IllegalArgumentException(
                    "Chỉ có thể thêm món vào đơn hàng đang mở (trạng thái MOI).");
        }

        MonAn monAn = monAnDAO.findById(monAnId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Không tìm thấy món ăn với id = " + monAnId));

        if (monAn.getTrangThai() == null || !monAn.getTrangThai()) {
            throw new IllegalArgumentException(
                    "Món '" + monAn.getTenMon() + "' hiện không còn phục vụ.");
        }

        // Kiểm tra nếu đã có món này thì tăng số lượng
        boolean found = false;
        for (OrderDetail detail : salesOrder.getDetails()) {
            if (detail.getMonAn().getId().equals(monAnId)) {
                detail.setSoLuong(detail.getSoLuong() + soLuong);
                found = true;
                break;
            }
        }

        if (!found) {
            OrderDetail newDetail = new OrderDetail(monAn, soLuong, monAn.getGia());
            salesOrder.addDetail(newDetail);
        }

        salesOrder.recalculateTongTien();
        return salesOrderDAO.update(salesOrder);
    }

    @Override
    public SalesOrder removeDetailFromOrder(Long orderId, Long detailId) {
        SalesOrder salesOrder = getOrderById(orderId);

        if (salesOrder.getTrangThai() != TrangThaiDon.MOI) {
            throw new IllegalArgumentException(
                    "Chỉ có thể xóa món từ đơn hàng đang mở (trạng thái MOI).");
        }

        OrderDetail toRemove = salesOrder.getDetails().stream()
                .filter(d -> d.getId().equals(detailId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Không tìm thấy dòng chi tiết với id = " + detailId));

        salesOrder.removeDetail(toRemove);
        return salesOrderDAO.update(salesOrder);
    }

    @Override
    public void deleteOrder(Long id) {
        getOrderById(id); // kiểm tra tồn tại trước
        salesOrderDAO.deleteById(id);
    }

    // ── Private helpers ───────────────────────────────────────────────────
    private void validateSoLuong(Integer soLuong) {
        if (soLuong == null || soLuong <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0.");
        }
        if (soLuong > 999) {
            throw new IllegalArgumentException("Số lượng tối đa 999 cho một món.");
        }
    }
}