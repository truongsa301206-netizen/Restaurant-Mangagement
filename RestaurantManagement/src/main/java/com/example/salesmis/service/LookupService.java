package com.example.salesmis.service;

import com.example.salesmis.model.entity.KhachHang;
import com.example.salesmis.model.entity.MonAn;
import java.util.List;

public interface LookupService {
    List<KhachHang> getAllKhachHang();
    List<MonAn> getAllActiveMonAn();
}