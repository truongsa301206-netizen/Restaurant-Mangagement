package com.example.salesmis.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "BAN")
public class Ban {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //

    @Column(name = "ten_ban", length = 50, nullable = false)
    private String tenBan;

    @Column(name = "trang_thai")
    private Boolean trangThai = Boolean.TRUE; // True = Trống, False = Có khách

    public Ban() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenBan() { return tenBan; }
    public void setTenBan(String tenBan) { this.tenBan = tenBan; }
    public Boolean getTrangThai() { return trangThai; }
    public void setTrangThai(Boolean trangThai) { this.trangThai = trangThai; }

    @Override
    public String toString() {
        return tenBan;
    }
}