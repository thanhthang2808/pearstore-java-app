package dao;

import entity.HoaDon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import connect.DBConnect;

public class HoaDonDAO {

    public int themHoaDonVaLayMa(HoaDon hd) {
        String sql = "INSERT INTO HoaDon (NgayLap, MaNV, TenNhanVien, TongTien) OUTPUT INSERTED.MaHD VALUES (?, ?, ?, ?)";

        Connection conn = DBConnect.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setTimestamp(1, new java.sql.Timestamp(hd.getNgayLap().getTime()));
            stmt.setString(2, hd.getMaNV());
            stmt.setString(3, hd.getTenNhanVien());
            stmt.setDouble(4, hd.getTongTien());

            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT MaHD, NgayLap, TenNhanVien, TongTien FROM HoaDon ORDER BY NgayLap DESC";

        Connection conn = DBConnect.getConnection(); // Không đóng connection
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHD(rs.getInt("MaHD"));
                hd.setNgayLap(rs.getTimestamp("NgayLap"));
                hd.setTenNhanVien(rs.getString("TenNhanVien"));
                hd.setTongTien(rs.getDouble("TongTien"));
                list.add(hd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    public List<HoaDon> getHoaDonByTimeRange(Timestamp thoiGianBatDau, Timestamp thoiGianKetThuc) {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT MaHD, NgayLap, TenNhanVien, TongTien FROM HoaDon WHERE NgayLap BETWEEN ? AND ? ORDER BY NgayLap DESC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnect.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setTimestamp(1, thoiGianBatDau);
            stmt.setTimestamp(2, thoiGianKetThuc);
            rs = stmt.executeQuery();
            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHD(rs.getInt("MaHD"));
                hd.setNgayLap(rs.getTimestamp("NgayLap"));
                hd.setTenNhanVien(rs.getString("TenNhanVien"));
                hd.setTongTien(rs.getDouble("TongTien"));
                list.add(hd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    public double tinhTongDoanhThu(String thoiGianBatDau, String thoiGianKetThuc) {
        double tongDoanhThu = 0;
        String query = "SELECT SUM(TongTien) FROM HoaDon WHERE NgayLap BETWEEN ? AND ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnect.getConnection();
            stmt = conn.prepareStatement(query);
            stmt.setString(1, thoiGianBatDau);
            stmt.setString(2, thoiGianKetThuc);
            rs = stmt.executeQuery();
            if (rs.next()) {
                tongDoanhThu = rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return tongDoanhThu;
    }

    public int demSoHoaDon(String thoiGianBatDau, String thoiGianKetThuc) {
        int soHoaDon = 0;
        String query = "SELECT COUNT(*) FROM HoaDon WHERE NgayLap BETWEEN ? AND ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnect.getConnection();
            stmt = conn.prepareStatement(query);
            stmt.setString(1, thoiGianBatDau);
            stmt.setString(2, thoiGianKetThuc);
            rs = stmt.executeQuery();
            if (rs.next()) {
                soHoaDon = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return soHoaDon;
    }

}
