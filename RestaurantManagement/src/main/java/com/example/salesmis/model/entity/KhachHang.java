package com.example.salesmis.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "KHACHHANG")
public class KhachHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ten", length = 150)
    private String ten;

    @Column(name = "sdt", length = 20, unique = true)
    private String sdt;

    @Column(name = "tong_lan")
    private Integer tongLan = 0;

    @Column(name = "tong_tien", precision = 14, scale = 2)
    private BigDecimal tongTien = BigDecimal.ZERO;

    /**
     * ENUM ánh xạ sang cột ENUM MySQL.
     * EnumType.STRING đảm bảo lưu "NORMAL" / "VIP" vào DB.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "loai", length = 10)
    private LoaiKhachHang loai = LoaiKhachHang.NORMAL;

    public enum LoaiKhachHang {
        NORMAL, VIP
    }

    // ── Constructors ─────────────────────────────────────────────────────
    public KhachHang() {}

    // ── Getters & Setters ────────────────────────────────────────────────
    public Long getId()                          { return id; }
    public void setId(Long id)                   { this.id = id; }
    public String getTen()                       { return ten; }
    public void setTen(String ten)               { this.ten = ten; }
    public String getSdt()                       { return sdt; }
    public void setSdt(String sdt)               { this.sdt = sdt; }
    public Integer getTongLan()                  { return tongLan; }
    public void setTongLan(Integer tongLan)      { this.tongLan = tongLan; }
    public BigDecimal getTongTien()              { return tongTien; }
    public void setTongTien(BigDecimal tongTien) { this.tongTien = tongTien; }
    public LoaiKhachHang getLoai()               { return loai; }
    public void setLoai(LoaiKhachHang loai)      { this.loai = loai; }

    @Override
    public String toString() {
        return "[" + id + "] " + ten + " - " + sdt;
    }
}