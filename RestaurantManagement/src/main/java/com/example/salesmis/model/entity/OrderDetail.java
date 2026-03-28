package com.example.salesmis.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "CTHD")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * FK tới bảng HOADON.
     * mappedBy ở SalesOrder chỉ ra field này là "owning side".
     * insertable/updatable = false nếu bạn manage qua SalesOrder.addDetail().
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hoadon_id", nullable = false)
    private SalesOrder salesOrder;

    /**
     * FK tới bảng MONAN.
     * FetchType.EAGER để load thông tin món ăn ngay cùng chi tiết đơn.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "monan_id", nullable = false)
    private MonAn monAn;

    @Column(name = "so_luong", nullable = false)
    private Integer soLuong;

    /**
     * don_gia: lưu giá tại thời điểm đặt hàng (không phụ thuộc MonAn.gia sau này).
     */
    @Column(name = "don_gia", precision = 12, scale = 2, nullable = false)
    private BigDecimal donGia;

    @Column(name = "thanh_tien", precision = 14, scale = 2, nullable = false)
    private BigDecimal thanhTien;

    // ── Constructors ─────────────────────────────────────────────────────
    public OrderDetail() {}

    /**
     * Constructor tiện lợi: tự tính thanhTien = soLuong * donGia.
     */
    public OrderDetail(MonAn monAn, Integer soLuong, BigDecimal donGia) {
        this.monAn   = monAn;
        this.soLuong = soLuong;
        this.donGia  = donGia;
        this.thanhTien = donGia.multiply(BigDecimal.valueOf(soLuong));
    }

    /**
     * Tính lại thanhTien khi soLuong hoặc donGia thay đổi.
     */
    public void recalculate() {
        if (this.donGia != null && this.soLuong != null) {
            this.thanhTien = this.donGia.multiply(BigDecimal.valueOf(this.soLuong));
        }
    }

    // ── Getters & Setters ────────────────────────────────────────────────
    public Long getId()                              { return id; }
    public void setId(Long id)                       { this.id = id; }
    public SalesOrder getSalesOrder()                { return salesOrder; }
    public void setSalesOrder(SalesOrder so)         { this.salesOrder = so; }
    public MonAn getMonAn()                          { return monAn; }
    public void setMonAn(MonAn monAn)                { this.monAn = monAn; }
    public Integer getSoLuong()                      { return soLuong; }
    public void setSoLuong(Integer soLuong)          { this.soLuong = soLuong; recalculate(); }
    public BigDecimal getDonGia()                    { return donGia; }
    public void setDonGia(BigDecimal donGia)         { this.donGia = donGia; recalculate(); }
    public BigDecimal getThanhTien()                 { return thanhTien; }
    public void setThanhTien(BigDecimal thanhTien)   { this.thanhTien = thanhTien; }
}