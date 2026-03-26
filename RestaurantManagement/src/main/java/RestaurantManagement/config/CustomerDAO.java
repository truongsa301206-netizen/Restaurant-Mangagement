package com.example.salesmis.dao;

import com.example.salesmis.model.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDAO {
    List<Customer> findAll();
    Optional<Customer> findById(Long id);

    Optional<Customer> findByCode(String code);

    Customer save(Customer customer);
    Customer update(Customer customer);
    void deleteById(Long id);
}