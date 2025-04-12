package dao;

import entity.HoaDon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
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

}
