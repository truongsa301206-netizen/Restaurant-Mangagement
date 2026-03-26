package vn.edu.ute.quanlybanhang.dao.impl;

import vn.edu.ute.quanlybanhang.config.DBConnection;
import vn.edu.ute.quanlybanhang.dao.HoaDonDAO;
import vn.edu.ute.quanlybanhang.model.HoaDon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAOImpl implements HoaDonDAO {

    private static final String SQL_FIND_ALL =
            "SELECT hd.MaHD, hd.MaKH, kh.TenCty AS TenKH, hd.MaNV, hd.NgayLapHD, hd.NgayNhanHang " +
                    "FROM HoaDon hd JOIN KhachHang kh ON hd.MaKH = kh.MaKH ORDER BY hd.MaHD DESC";

    private static final String SQL_INSERT =
            "INSERT INTO HoaDon(MaKH, MaNV, NgayLapHD, NgayNhanHang) VALUES(?,?,?,?)";

    private static final String SQL_DELETE =
            "DELETE FROM HoaDon WHERE MaHD=?";

    @Override

    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHD(rs.getInt("MaHD"));
                hd.setMaKH(rs.getString("MaKH"));
                hd.setTenKhachHang(rs.getString("TenKH"));
                hd.setMaNV(rs.getString("MaNV"));

                Date ngayLap = rs.getDate("NgayLapHD");
                if (ngayLap != null) hd.setNgayLapHD(ngayLap.toLocalDate());

                Date ngayNhan = rs.getDate("NgayNhanHang");
                if (ngayNhan != null) hd.setNgayNhanHang(ngayNhan.toLocalDate());

                list.add(hd);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi findAll HoaDon: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public boolean insert(HoaDon hd) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT)) {

            ps.setString(1, hd.getMaKH());
            ps.setString(2, hd.getMaNV());
            ps.setDate(3, hd.getNgayLapHD() != null ? Date.valueOf(hd.getNgayLapHD()) : null);
            ps.setDate(4, hd.getNgayNhanHang() != null ? Date.valueOf(hd.getNgayNhanHang()) : null);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi insert HoaDon: " + e.getMessage(), e);
        }
    }

    private static final String SQL_UPDATE =
            "UPDATE HoaDon SET MaKH=?, MaNV=?, NgayLapHD=?, NgayNhanHang=? WHERE MaHD=?";
    @Override
    public boolean update(HoaDon hd) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, hd.getMaKH());
            ps.setString(2, hd.getMaNV());
            ps.setDate(3, Date.valueOf(hd.getNgayLapHD()));
            ps.setDate(4, Date.valueOf(hd.getNgayNhanHang()));
            ps.setInt(5, hd.getMaHD());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi update HoaDon: " + e.getMessage(), e);
        }
    }



    @Override
    public boolean delete(int maHD) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, maHD);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi delete HoaDon: " + e.getMessage(), e);
        }
    }
}

