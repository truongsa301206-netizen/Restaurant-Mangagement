package com.example.salesmis.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "MONAN")
public class MonAn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ten_mon", length = 150, nullable = false)
    private String tenMon;

    @Column(name = "gia", precision = 12, scale = 2, nullable = false)
    private BigDecimal gia;

    /**
     * trang_thai là BOOLEAN trong MySQL (0/1).
     * JPA ánh xạ tự động sang boolean Java.
     */
    @Column(name = "trang_thai")
    private Boolean trangThai = Boolean.TRUE;

    // ── Constructors ─────────────────────────────────────────────────────
    public MonAn() {}

    // ── Getters & Setters ────────────────────────────────────────────────
    public Long getId()                      { return id; }
    public void setId(Long id)               { this.id = id; }
    public String getTenMon()                { return tenMon; }
    public void setTenMon(String tenMon)     { this.tenMon = tenMon; }
    public BigDecimal getGia()               { return gia; }
    public void setGia(BigDecimal gia)       { this.gia = gia; }
    public Boolean getTrangThai()            { return trangThai; }
    public void setTrangThai(Boolean tt)     { this.trangThai = tt; }

    @Override
    public String toString() {
        return "[" + id + "] " + tenMon + " - " + gia + " VND";
    }
}