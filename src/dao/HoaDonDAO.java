package dao;

import entity.HoaDon;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {

    public int themHoaDonVaLayMa(HoaDon hd) {
        
        String sql = "INSERT INTO HoaDon (NgayLap, TenNhanVien, TongTien) OUTPUT INSERTED.MaHD VALUES (?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, new java.sql.Timestamp(hd.getNgayLap().getTime()));
            stmt.setString(2, hd.getTenNhanVien());
            stmt.setDouble(3, hd.getTongTien());
    
            System.out.println("Executing query: " + sql);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                int maHD = rs.getInt(1);
                System.out.println("HoaDon added with MaHD: " + maHD);
                return maHD;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT MaHD, NgayLap, TenNhanVien, TongTien FROM HoaDon ORDER BY NgayLap DESC";
    
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
    
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
        }
    
        return list;
    }

    public List<HoaDon> getHoaDonByTimeRange(Timestamp thoiGianBatDau, Timestamp thoiGianKetThuc) {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT MaHD, NgayLap, TenNhanVien, TongTien FROM HoaDon WHERE NgayLap BETWEEN ? AND ? ORDER BY NgayLap DESC";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, new Timestamp(thoiGianBatDau.getTime()));
            stmt.setTimestamp(2, new Timestamp(thoiGianKetThuc.getTime()));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    HoaDon hd = new HoaDon();
                    hd.setMaHD(rs.getInt("MaHD"));
                    hd.setNgayLap(rs.getTimestamp("NgayLap"));
                    hd.setTenNhanVien(rs.getString("TenNhanVien"));
                    hd.setTongTien(rs.getDouble("TongTien"));
                    list.add(hd);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn dữ liệu từ bảng HoaDon: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    public double tinhTongDoanhThu(String thoiGianBatDau, String thoiGianKetThuc) {
        double tongDoanhThu = 0;
        String query = "SELECT SUM(TongTien) FROM HoaDon WHERE NgayLap BETWEEN ? AND ?";
        try (Connection connection = DBConnect.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, thoiGianBatDau);
            stmt.setString(2, thoiGianKetThuc);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                tongDoanhThu = rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tongDoanhThu;
    }

    public int demSoHoaDon(String thoiGianBatDau, String thoiGianKetThuc) {
        int soHoaDon = 0;
        String query = "SELECT COUNT(*) FROM HoaDon WHERE NgayLap BETWEEN ? AND ?";
        try (Connection connection = DBConnect.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, thoiGianBatDau);
            stmt.setString(2, thoiGianKetThuc);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                soHoaDon = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return soHoaDon;
    }
}
