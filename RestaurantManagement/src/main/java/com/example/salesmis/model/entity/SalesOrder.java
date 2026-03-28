package com.example.salesmis.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "HOADON")
public class SalesOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Ngày tạo đơn hàng.
     * LocalDateTime ánh xạ sang DATETIME trong MySQL.
     */
    @Column(name = "ngay")
    private LocalDateTime ngay;

    /**
     * Quan hệ Many-to-One với KhachHang.
     * FetchType.LAZY = chỉ load khi cần thiết (tối ưu hiệu năng).
     * @JoinColumn(name = "khachhang_id") → FK trong bảng HOADON.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "khachhang_id")
    private KhachHang khachHang;

    /**
     * Quan hệ Many-to-One với NhanVien.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nhanvien_id")
    private NhanVien nhanVien;

    /**
     * Quan hệ Many-to-One với Ban.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ban_id")
    private Ban ban;

    @Column(name = "tong_tien", precision = 14, scale = 2)
    private BigDecimal tongTien = BigDecimal.ZERO;

    /**
     * ENUM trạng thái đơn hàng.
     * MOI → đang mở | HOANTHANH → đã thanh toán | HUY → đã huỷ.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", length = 20)
    private TrangThaiDon trangThai = TrangThaiDon.MOI;

    public enum TrangThaiDon {
        MOI, HOANTHANH, HUY
    }

    /**
     * Quan hệ One-to-Many với OrderDetail.
     * cascade = ALL → persist / remove OrderDetail cùng SalesOrder.
     * orphanRemoval = true → xóa dòng chi tiết khi bị remove khỏi list.
     * mappedBy = "salesOrder" → bên SalesOrder là "inverse side".
     */
    @OneToMany(
            mappedBy = "salesOrder",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<OrderDetail> details = new ArrayList<>();

    // ── Constructors ─────────────────────────────────────────────────────
    public SalesOrder() {}

    /**
     * Helper: tính lại tổng tiền từ danh sách chi tiết.
     * Gọi mỗi khi thêm / sửa / xóa OrderDetail.
     */
    public void recalculateTongTien() {
        this.tongTien = details.stream()
                .map(OrderDetail::getThanhTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Helper: thêm một dòng chi tiết và liên kết ngược lại.
     */
    public void addDetail(OrderDetail detail) {
        detail.setSalesOrder(this);
        this.details.add(detail);
        recalculateTongTien();
    }

    /**
     * Helper: xóa một dòng chi tiết.
     */
    public void removeDetail(OrderDetail detail) {
        detail.setSalesOrder(null);
        this.details.remove(detail);
        recalculateTongTien();
    }

    // ── Getters & Setters ────────────────────────────────────────────────
    public Long getId()                            { return id; }
    public void setId(Long id)                     { this.id = id; }
    public LocalDateTime getNgay()                 { return ngay; }
    public void setNgay(LocalDateTime ngay)        { this.ngay = ngay; }
    public KhachHang getKhachHang()                { return khachHang; }
    public void setKhachHang(KhachHang kh)         { this.khachHang = kh; }
    public NhanVien getNhanVien()                  { return nhanVien; }
    public void setNhanVien(NhanVien nv)           { this.nhanVien = nv; }
    public Ban getBan()                            { return ban; }
    public void setBan(Ban ban)                    { this.ban = ban; }
    public BigDecimal getTongTien()                { return tongTien; }
    public void setTongTien(BigDecimal tongTien)   { this.tongTien = tongTien; }
    public TrangThaiDon getTrangThai()             { return trangThai; }
    public void setTrangThai(TrangThaiDon tt)      { this.trangThai = tt; }
    public List<OrderDetail> getDetails()          { return details; }
    public void setDetails(List<OrderDetail> d)    { this.details = d; }
}