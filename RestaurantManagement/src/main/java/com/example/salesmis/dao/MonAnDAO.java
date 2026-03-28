package com.example.salesmis.dao;

import com.example.salesmis.model.entity.MonAn;
import java.util.List;
import java.util.Optional;

public interface MonAnDAO {
    List<MonAn> findAll();
    List<MonAn> findAllActive();          // chỉ món còn hoạt động
    Optional<MonAn> findById(Long id);
    List<MonAn> searchByKeyword(String keyword);
    MonAn save(MonAn monAn);
    MonAn update(MonAn monAn);
    void deleteById(Long id);
    boolean existsByTenMon(String tenMon);
}