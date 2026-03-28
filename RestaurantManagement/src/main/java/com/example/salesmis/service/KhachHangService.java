package com.example.salesmis.service;

import com.example.salesmis.model.entity.KhachHang;
import java.util.List;

public interface KhachHangService {
    List<KhachHang> getAllKhachHang();
    KhachHang getKhachHangById(Long id);
    List<KhachHang> searchKhachHang(String keyword);
    KhachHang createKhachHang(KhachHang khachHang);
    KhachHang updateKhachHang(Long id, KhachHang khachHangDetails);
    void deleteKhachHang(Long id);
}