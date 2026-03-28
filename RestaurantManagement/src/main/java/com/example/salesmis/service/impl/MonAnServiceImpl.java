package com.example.salesmis.service.impl;

import com.example.salesmis.dao.MonAnDAO;
import com.example.salesmis.model.entity.MonAn;
import com.example.salesmis.service.MonAnService;

import java.util.List;

public class MonAnServiceImpl implements MonAnService {

    private final MonAnDAO monAnDAO;

    public MonAnServiceImpl(MonAnDAO monAnDAO) {
        this.monAnDAO = monAnDAO;
    }

    @Override
    public List<MonAn> getAllMonAn() {
        return monAnDAO.findAll(); //
    }

    @Override
    public List<MonAn> getActiveMonAn() {
        return monAnDAO.findAllActive(); //
    }

    @Override
    public MonAn getMonAnById(Long id) {
        return monAnDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy món ăn với ID = " + id)); //
    }

    @Override
    public List<MonAn> searchMonAn(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return monAnDAO.findAll();
        }
        return monAnDAO.searchByKeyword(keyword.trim()); //
    }

    @Override
    public MonAn createMonAn(MonAn monAn) {
        if (monAnDAO.existsByTenMon(monAn.getTenMon())) {
            throw new IllegalArgumentException("Món ăn '" + monAn.getTenMon() + "' đã tồn tại."); //
        }
        return monAnDAO.save(monAn); //
    }

    @Override
    public MonAn updateMonAn(Long id, MonAn monAnDetails) {
        MonAn existingMon = getMonAnById(id);

        if (!existingMon.getTenMon().equalsIgnoreCase(monAnDetails.getTenMon())
                && monAnDAO.existsByTenMon(monAnDetails.getTenMon())) {
            throw new IllegalArgumentException("Tên món ăn '" + monAnDetails.getTenMon() + "' đã tồn tại.");
        }

        existingMon.setTenMon(monAnDetails.getTenMon());
        existingMon.setGia(monAnDetails.getGia());
        existingMon.setTrangThai(monAnDetails.getTrangThai());

        return monAnDAO.update(existingMon); //
    }

    @Override
    public void deleteMonAn(Long id) {
        getMonAnById(id); // Kiểm tra tồn tại trước khi xóa
        monAnDAO.deleteById(id); //
    }
}