package dao;

import entity.ChiTietHoaDon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDonDAO {
    public boolean themChiTietHoaDon(ChiTietHoaDon ct) {
        String sql = "INSERT INTO ChiTietHoaDon (MaHD, MaSP, TenSP, SoLuong, DonGia, ThanhTien) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnect.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, ct.getMaHD());
            stmt.setInt(2, ct.getMaSP());
            stmt.setString(3, ct.getTenSP());
            stmt.setInt(4, ct.getSoLuong());
            stmt.setDouble(5, ct.getDonGia());
            stmt.setDouble(6, ct.getThanhTien());
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
     public List<ChiTietHoaDon> getByMaHD(int maHD) {
        List<ChiTietHoaDon> list = new ArrayList<>();
        String sql = "SELECT MaSP, TenSP, SoLuong, DonGia, ThanhTien FROM ChiTietHoaDon WHERE MaHD = ?";

        try (Connection connection = DBConnect.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, maHD);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ChiTietHoaDon ct = new ChiTietHoaDon(
                    maHD,
                    rs.getInt("MaSP"),
                    rs.getString("TenSP"),
                    rs.getInt("SoLuong"),
                    rs.getDouble("DonGia"),
                    rs.getDouble("ThanhTien")
                );
                list.add(ct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
