package com.example.salesmis.controller;

import com.example.salesmis.model.entity.KhachHang;
import com.example.salesmis.service.KhachHangService;
import java.util.List;

public class KhachHangController {

    private final KhachHangService khachHangService;

    public KhachHangController(KhachHangService khachHangService) {
        this.khachHangService = khachHangService;
    }

    public List<KhachHang> getAllKhachHang() {
        return khachHangService.getAllKhachHang();
    }

    public List<KhachHang> searchKhachHang(String keyword) {
        return khachHangService.searchKhachHang(keyword);
    }

    public KhachHang createKhachHang(String ten, String sdt, String loaiStr) {
        validateInput(ten, sdt);
        KhachHang kh = new KhachHang();
        kh.setTen(ten.trim());
        kh.setSdt(sdt.trim());
        kh.setLoai(KhachHang.LoaiKhachHang.valueOf(loaiStr.toUpperCase()));
        return khachHangService.createKhachHang(kh);
    }

    public KhachHang updateKhachHang(Long id, String ten, String sdt, String loaiStr) {
        validateInput(ten, sdt);
        KhachHang kh = new KhachHang();
        kh.setTen(ten.trim());
        kh.setSdt(sdt.trim());
        kh.setLoai(KhachHang.LoaiKhachHang.valueOf(loaiStr.toUpperCase()));
        return khachHangService.updateKhachHang(id, kh);
    }

    public void deleteKhachHang(Long id) {
        khachHangService.deleteKhachHang(id);
    }

    private void validateInput(String ten, String sdt) {
        if (ten == null || ten.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên khách hàng không được để trống.");
        }
        if (sdt == null || sdt.trim().isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không được để trống.");
        }
    }
}