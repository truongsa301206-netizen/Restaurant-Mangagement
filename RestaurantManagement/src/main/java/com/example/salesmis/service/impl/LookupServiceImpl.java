package com.example.salesmis.service.impl;

import com.example.salesmis.dao.KhachHangDAO;
import com.example.salesmis.dao.MonAnDAO;
import com.example.salesmis.model.entity.KhachHang;
import com.example.salesmis.model.entity.MonAn;
import com.example.salesmis.service.LookupService;

import java.util.List;

public class LookupServiceImpl implements LookupService {

    private final KhachHangDAO khachHangDAO;
    private final MonAnDAO monAnDAO;

    public LookupServiceImpl(KhachHangDAO khachHangDAO, MonAnDAO monAnDAO) {
        this.khachHangDAO = khachHangDAO;
        this.monAnDAO     = monAnDAO; //
    }

    @Override
    public List<KhachHang> getAllKhachHang() {
        return khachHangDAO.findAll(); //
    }

    @Override
    public List<MonAn> getAllActiveMonAn() {
        return monAnDAO.findAllActive(); //
    }
}