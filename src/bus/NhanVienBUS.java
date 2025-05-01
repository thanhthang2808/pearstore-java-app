package bus;

import dao.DBConnect;
import entity.ChucVu;
import entity.NhanVien;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhanVienBUS {

    // Lấy toàn bộ danh sách nhân viên từ DB
    public List<NhanVien> layDanhSachNhanVien() {
        List<NhanVien> ds = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien";

        try (Connection conn = DBConnect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String maNV = rs.getString("MaNV");
                String tenNV = rs.getString("TenNV");
                Date ngaySinh = rs.getDate("NgaySinh");
                boolean gioiTinh = rs.getBoolean("GioiTinh");
                String email = rs.getString("Email");
                String soDT = rs.getString("SoDienThoai");
                ChucVu chucVu = ChucVu.valueOf(rs.getString("ChucVu"));

                NhanVien nv = new NhanVien(maNV, tenNV, ngaySinh, gioiTinh, email, soDT, chucVu);
                ds.add(nv);
            }

        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi lấy danh sách nhân viên: " + e.getMessage());
        }

        return ds;
    }

    // Thêm nhân viên
    public boolean themNhanVien(NhanVien nv) {
        String sql = "INSERT INTO NhanVien (MaNV, TenNV, NgaySinh, GioiTinh, Email, SoDienThoai, ChucVu) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getMaNV());
            ps.setString(2, nv.getTenNV());
            ps.setDate(3, nv.getNgaySinh());
            ps.setBoolean(4, nv.isGioiTinh());
            ps.setString(5, nv.getEmail());
            ps.setString(6, nv.getSoDienThoai());
            ps.setString(7, nv.getChucVu().name());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Lỗi thêm nhân viên: " + e.getMessage());
            return false;
        }
    }

    // Cập nhật nhân viên
    public boolean suaNhanVien(NhanVien nv) {
        String sql = "UPDATE NhanVien SET TenNV = ?, NgaySinh = ?, GioiTinh = ?, Email = ?, SoDienThoai = ?, ChucVu = ? WHERE MaNV = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getTenNV());
            ps.setDate(2, nv.getNgaySinh());
            ps.setBoolean(3, nv.isGioiTinh());
            ps.setString(4, nv.getEmail());
            ps.setString(5, nv.getSoDienThoai());
            ps.setString(6, nv.getChucVu().name());
            ps.setString(7, nv.getMaNV());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Lỗi cập nhật nhân viên: " + e.getMessage());
            return false;
        }
    }

    // Xoá nhân viên
    public boolean xoaNhanVien(String maNV) {
        String sql = "DELETE FROM NhanVien WHERE MaNV = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNV);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Lỗi xóa nhân viên: " + e.getMessage());
            return false;
        }
    }
}
