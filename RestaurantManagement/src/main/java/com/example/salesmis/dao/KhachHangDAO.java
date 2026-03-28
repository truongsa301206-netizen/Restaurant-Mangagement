package com.example.salesmis.dao;

import com.example.salesmis.model.entity.KhachHang;
import java.util.List;
import java.util.Optional;

public interface KhachHangDAO {
    List<KhachHang> findAll();
    Optional<KhachHang> findById(Long id);
    List<KhachHang> searchByKeyword(String keyword);
    KhachHang save(KhachHang khachHang);
    KhachHang update(KhachHang khachHang);
    void deleteById(Long id);
    boolean existsBySdt(String sdt);
}