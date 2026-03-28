package com.example.salesmis.service.impl;

import com.example.salesmis.dao.KhachHangDAO;
import com.example.salesmis.model.entity.KhachHang;
import com.example.salesmis.service.KhachHangService;

import java.util.List;

public class KhachHangServiceImpl implements KhachHangService {

    private final KhachHangDAO khachHangDAO;

    public KhachHangServiceImpl(KhachHangDAO khachHangDAO) {
        this.khachHangDAO = khachHangDAO;
    }

    @Override
    public List<KhachHang> getAllKhachHang() {
        return khachHangDAO.findAll(); //
    }

    @Override
    public KhachHang getKhachHangById(Long id) {
        return khachHangDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng với ID = " + id)); //
    }

    @Override
    public List<KhachHang> searchKhachHang(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return khachHangDAO.findAll();
        }
        return khachHangDAO.searchByKeyword(keyword.trim()); //
    }

    @Override
    public KhachHang createKhachHang(KhachHang khachHang) {
        if (khachHangDAO.existsBySdt(khachHang.getSdt())) {
            throw new IllegalArgumentException("Số điện thoại '" + khachHang.getSdt() + "' đã tồn tại trên hệ thống."); //
        }
        return khachHangDAO.save(khachHang); //
    }

    @Override
    public KhachHang updateKhachHang(Long id, KhachHang khachHangDetails) {
        KhachHang existingKh = getKhachHangById(id);

        // Kiểm tra trùng SĐT với người khác
        if (!existingKh.getSdt().equals(khachHangDetails.getSdt())
                && khachHangDAO.existsBySdt(khachHangDetails.getSdt())) {
            throw new IllegalArgumentException("Số điện thoại '" + khachHangDetails.getSdt() + "' đã được sử dụng.");
        }

        existingKh.setTen(khachHangDetails.getTen());
        existingKh.setSdt(khachHangDetails.getSdt());
        existingKh.setLoai(khachHangDetails.getLoai());

        return khachHangDAO.update(existingKh); //
    }

    @Override
    public void deleteKhachHang(Long id) {
        getKhachHangById(id); // Kiểm tra tồn tại trước khi xóa
        khachHangDAO.deleteById(id); //
    }
}