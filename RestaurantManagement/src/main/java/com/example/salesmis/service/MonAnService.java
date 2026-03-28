package com.example.salesmis.service;

import com.example.salesmis.model.entity.MonAn;
import java.util.List;

public interface MonAnService {
    List<MonAn> getAllMonAn();
    List<MonAn> getActiveMonAn();
    MonAn getMonAnById(Long id);
    List<MonAn> searchMonAn(String keyword);
    MonAn createMonAn(MonAn monAn);
    MonAn updateMonAn(Long id, MonAn monAnDetails);
    void deleteMonAn(Long id);
}