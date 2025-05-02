package dao;

import entity.ChucVu;
import entity.NhanVien;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import connect.DBConnect;

public class NhanVienDAO {

    public List<NhanVien> layDanhSachNhanVien() {
        List<NhanVien> ds = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnect.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

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
        } finally {
            // Đảm bảo đóng các tài nguyên sau khi sử dụng
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                System.err.println("❌ Lỗi khi đóng kết nối: " + e.getMessage());
            }
        }

        return ds;
    }

    // Thêm nhân viên
    public boolean themNhanVien(NhanVien nv) {
        String sql = "INSERT INTO NhanVien (MaNV, TenNV, NgaySinh, GioiTinh, Email, SoDienThoai, ChucVu) VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBConnect.getConnection();
            ps = conn.prepareStatement(sql);

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
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                System.err.println("❌ Lỗi khi đóng kết nối: " + e.getMessage());
            }
        }
    }

    // Cập nhật nhân viên
    public boolean suaNhanVien(NhanVien nv) {
        String sql = "UPDATE NhanVien SET TenNV = ?, NgaySinh = ?, GioiTinh = ?, Email = ?, SoDienThoai = ?, ChucVu = ? WHERE MaNV = ?";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBConnect.getConnection();
            ps = conn.prepareStatement(sql);

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
        } finally {
            // Đảm bảo đóng các tài nguyên sau khi sử dụng
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                System.err.println("❌ Lỗi khi đóng kết nối: " + e.getMessage());
            }
        }
    }

    // Xoá nhân viên
    public boolean xoaNhanVien(String maNV) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBConnect.getConnection();
            conn.setAutoCommit(false);

            String deleteTaiKhoanSQL = "DELETE FROM TaiKhoan WHERE MaNV = ?";
            ps = conn.prepareStatement(deleteTaiKhoanSQL);
            ps.setString(1, maNV);
            ps.executeUpdate();

            String deleteNhanVienSQL = "DELETE FROM NhanVien WHERE MaNV = ?";
            ps = conn.prepareStatement(deleteNhanVienSQL);
            ps.setString(1, maNV);
            int nhanVienDeleted = ps.executeUpdate();

            if (nhanVienDeleted > 0) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi xóa nhân viên: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("❌ Lỗi rollback: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.err.println("❌ Lỗi khi đóng kết nối: " + e.getMessage());
            }
        }
    }
}
