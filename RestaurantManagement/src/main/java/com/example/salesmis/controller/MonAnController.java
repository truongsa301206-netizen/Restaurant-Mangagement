package com.example.salesmis.controller;

import com.example.salesmis.model.entity.MonAn;
import com.example.salesmis.service.MonAnService;
import java.math.BigDecimal;
import java.util.List;

public class MonAnController {

    private final MonAnService monAnService;

    public MonAnController(MonAnService monAnService) {
        this.monAnService = monAnService;
    }

    public List<MonAn> getAllMonAn() {
        return monAnService.getAllMonAn();
    }

    public List<MonAn> searchMonAn(String keyword) {
        return monAnService.searchMonAn(keyword);
    }

    public MonAn createMonAn(String tenMon, String giaStr, boolean trangThai) {
        validateInput(tenMon, giaStr);
        MonAn monAn = new MonAn();
        monAn.setTenMon(tenMon.trim());
        monAn.setGia(new BigDecimal(giaStr.trim()));
        monAn.setTrangThai(trangThai);
        return monAnService.createMonAn(monAn);
    }

    public MonAn updateMonAn(Long id, String tenMon, String giaStr, boolean trangThai) {
        validateInput(tenMon, giaStr);
        MonAn monAn = new MonAn();
        monAn.setTenMon(tenMon.trim());
        monAn.setGia(new BigDecimal(giaStr.trim()));
        monAn.setTrangThai(trangThai);
        return monAnService.updateMonAn(id, monAn);
    }

    public void deleteMonAn(Long id) {
        monAnService.deleteMonAn(id);
    }

    private void validateInput(String tenMon, String giaStr) {
        if (tenMon == null || tenMon.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên món ăn không được để trống.");
        }
        try {
            BigDecimal gia = new BigDecimal(giaStr.trim());
            if (gia.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Giá món không thể nhỏ hơn 0.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Giá món phải là số hợp lệ.");
        }
    }
}