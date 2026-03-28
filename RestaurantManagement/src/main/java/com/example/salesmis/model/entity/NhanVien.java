package com.example.salesmis.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "NHANVIEN")
public class NhanVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //

    @Column(name = "ten_nhanvien", length = 150, nullable = false)
    private String tenNhanVien;

    @Column(name = "username", length = 50, unique = true, nullable = false)
    private String username;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    public NhanVien() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenNhanVien() { return tenNhanVien; }
    public void setTenNhanVien(String tenNhanVien) { this.tenNhanVien = tenNhanVien; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}