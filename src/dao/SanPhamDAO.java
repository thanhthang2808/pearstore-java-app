package dao;

import entity.NganhHang;
import entity.SanPham;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connect.DBConnect;

public class SanPhamDAO {

    // Removed the unnecessary DBConnect.connection field. Connections should be
    // obtained and closed within the scope of each method.

    public List<SanPham> getAllSanPham() {
        List<SanPham> danhSachSanPham = new ArrayList<>();
        Connection connection = null; // Declare connection within the method
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBConnect.getConnection(); // Get connection here
            System.out.println("Connection status: " + connection != null ? "Connected" : "Not connected");

            String sql = "SELECT sp.MaSP, sp.TenSP, sp.GiaBan, sp.SoLuong, sp.MaVach, sp.HinhAnh, sp.GiaNhap, sp.DonViTinh, nh.MaNganh, nh.TenNganh, nh.MoTa "
                    + "FROM SanPham sp LEFT JOIN NganhHang nh ON sp.MaNganh = nh.MaNganh";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                NganhHang nganhHang = new NganhHang(
                        resultSet.getString("MaNganh"),
                        resultSet.getString("TenNganh"),
                        resultSet.getString("MoTa"));
                SanPham sp = new SanPham(
                        resultSet.getString("MaSP"),
                        resultSet.getString("TenSP"),
                        resultSet.getBigDecimal("GiaBan"),
                        resultSet.getInt("SoLuong"),
                        resultSet.getString("MaVach"),
                        resultSet.getString("HinhAnh"),
                        resultSet.getBigDecimal("GiaNhap"),
                        resultSet.getString("DonViTinh"),
                        nganhHang);
                danhSachSanPham.add(sp);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy tất cả sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
        if (danhSachSanPham.isEmpty()) {
            System.out.println("No products found.");
        } else {
            System.out.println("Products retrieved successfully.");
        }
        return danhSachSanPham;
    }

    public List<SanPham> getAllSanPhamByTen(String tenSanPham) {
        List<SanPham> danhSachSanPham = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBConnect.getConnection();
            System.out.println("Connection status: " + (connection != null ? "Connected" : "Not connected"));

            String sql = "SELECT sp.MaSP, sp.TenSP, sp.GiaBan, sp.SoLuong, sp.MaVach, sp.HinhAnh, sp.GiaNhap, sp.DonViTinh, nh.MaNganh, nh.TenNganh, nh.MoTa "
                    + "FROM SanPham sp LEFT JOIN NganhHang nh ON sp.MaNganh = nh.MaNganh "
                    + "WHERE sp.TenSP LIKE ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "%" + tenSanPham + "%"); // tìm gần đúng

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                NganhHang nganhHang = new NganhHang(
                        resultSet.getString("MaNganh"),
                        resultSet.getString("TenNganh"),
                        resultSet.getString("MoTa"));
                SanPham sp = new SanPham(
                        resultSet.getString("MaSP"),
                        resultSet.getString("TenSP"),
                        resultSet.getBigDecimal("GiaBan"),
                        resultSet.getInt("SoLuong"),
                        resultSet.getString("MaVach"),
                        resultSet.getString("HinhAnh"),
                        resultSet.getBigDecimal("GiaNhap"),
                        resultSet.getString("DonViTinh"),
                        nganhHang);
                danhSachSanPham.add(sp);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm sản phẩm theo tên: " + e.getMessage());
            e.printStackTrace();
        }

        if (danhSachSanPham.isEmpty()) {
            System.out.println("Không tìm thấy sản phẩm nào phù hợp với: " + tenSanPham);
        } else {
            System.out.println("Đã tìm thấy " + danhSachSanPham.size() + " sản phẩm theo tên: " + tenSanPham);
        }

        return danhSachSanPham;
    }

    public SanPham getSanPhamById(String maSP) {
        SanPham sanPham = null;
        Connection connection = null; // Declare here
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBConnect.getConnection(); // Get connection here
            String sql = "SELECT sp.TenSP, sp.GiaBan, sp.SoLuong, sp.MaVach, sp.HinhAnh, sp.GiaNhap, sp.DonViTinh, nh.MaNganh, nh.TenNganh, nh.MoTa "
                    + "FROM SanPham sp LEFT JOIN NganhHang nh ON sp.MaNganh = nh.MaNganh WHERE sp.MaSP = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, maSP);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                NganhHang nganhHang = new NganhHang(
                        resultSet.getString("MaNganh"),
                        resultSet.getString("TenNganh"),
                        resultSet.getString("MoTa"));
                sanPham = new SanPham(
                        maSP,
                        resultSet.getString("TenSP"),
                        resultSet.getBigDecimal("GiaBan"),
                        resultSet.getInt("SoLuong"),
                        resultSet.getString("MaVach"),
                        resultSet.getString("HinhAnh"),
                        resultSet.getBigDecimal("GiaNhap"),
                        resultSet.getString("DonViTinh"),
                        nganhHang);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy sản phẩm theo ID: " + e.getMessage());
            e.printStackTrace();
        }
        return sanPham;
    }

    public SanPham getSanPhamByMaVach(String maVach) {
        SanPham sanPham = null;
        Connection connection = null; // Declare here
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBConnect.getConnection(); // Get connection here
            String sql = "SELECT sp.MaSP, sp.TenSP, sp.GiaBan, sp.SoLuong, sp.HinhAnh, sp.GiaNhap, sp.DonViTinh, nh.MaNganh, nh.TenNganh, nh.MoTa "
                    + "FROM SanPham sp LEFT JOIN NganhHang nh ON sp.MaNganh = nh.MaNganh WHERE sp.MaVach = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, maVach);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                NganhHang nganhHang = new NganhHang(
                        resultSet.getString("MaNganh"),
                        resultSet.getString("TenNganh"),
                        resultSet.getString("MoTa"));
                sanPham = new SanPham(
                        resultSet.getString("MaSP"),
                        resultSet.getString("TenSP"),
                        resultSet.getBigDecimal("GiaBan"),
                        resultSet.getInt("SoLuong"),
                        maVach,
                        resultSet.getString("HinhAnh"),
                        resultSet.getBigDecimal("GiaNhap"),
                        resultSet.getString("DonViTinh"),
                        nganhHang);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy sản phẩm theo mã vạch: " + e.getMessage());
            e.printStackTrace();
        }
        return sanPham;
    }

    public List<SanPham> getAllSanPhamByNganhHang(String maNganh) {
        List<SanPham> danhSachSanPham = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBConnect.getConnection();
            String sql = "SELECT sp.MaSP, sp.TenSP, sp.GiaBan, sp.SoLuong, sp.MaVach, sp.HinhAnh, sp.GiaNhap, sp.DonViTinh "
                    + "FROM SanPham sp WHERE sp.MaNganh = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, maNganh);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                SanPham sp = new SanPham(
                        resultSet.getString("MaSP"),
                        resultSet.getString("TenSP"),
                        resultSet.getBigDecimal("GiaBan"),
                        resultSet.getInt("SoLuong"),
                        resultSet.getString("MaVach"),
                        resultSet.getString("HinhAnh"),
                        resultSet.getBigDecimal("GiaNhap"),
                        resultSet.getString("DonViTinh"),
                        null); // maNganh
                danhSachSanPham.add(sp);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy sản phẩm theo ngành hàng: " + e.getMessage());
            e.printStackTrace();
        }
        return danhSachSanPham;
    }

    public boolean themSanPham(SanPham sanPham) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;

        try {
            connection = DBConnect.getConnection();
            String sql = "INSERT INTO SanPham (MaSP, TenSP, GiaBan, SoLuong, MaVach, HinhAnh, GiaNhap, DonViTinh, MaNganh) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, sanPham.getMaSP());
            preparedStatement.setString(2, sanPham.getTenSP());
            preparedStatement.setBigDecimal(3, sanPham.getGiaBan());
            preparedStatement.setInt(4, sanPham.getSoLuong());
            preparedStatement.setString(5, sanPham.getMaVach());
            preparedStatement.setString(6, sanPham.getHinhAnh());
            preparedStatement.setBigDecimal(7, sanPham.getGiaNhap());
            preparedStatement.setString(8, sanPham.getDonViTinh());
            preparedStatement.setString(9, sanPham.getNganhHang().getMaNganh());
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
        return rowsAffected > 0;
    }

    public boolean capNhatSanPham(SanPham sanPham) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;

        try {
            connection = DBConnect.getConnection();
            String sql = "UPDATE SanPham SET TenSP = ?, GiaBan = ?, SoLuong = ?, MaVach = ?, HinhAnh = ?, GiaNhap = ?, DonViTinh = ?, MaNganh = ? WHERE MaSP = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, sanPham.getTenSP());
            preparedStatement.setBigDecimal(2, sanPham.getGiaBan());
            preparedStatement.setInt(3, sanPham.getSoLuong());
            preparedStatement.setString(4, sanPham.getMaVach());
            preparedStatement.setString(5, sanPham.getHinhAnh());
            preparedStatement.setBigDecimal(6, sanPham.getGiaNhap());
            preparedStatement.setString(7, sanPham.getDonViTinh());
            preparedStatement.setString(8, sanPham.getNganhHang().getMaNganh());
            preparedStatement.setString(9, sanPham.getMaSP());
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
        return rowsAffected > 0;
    }

    public boolean xoaSanPham(String maSP) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;

        try {
            connection = DBConnect.getConnection();
            String sql = "DELETE FROM SanPham WHERE MaSP = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, maSP);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
        return rowsAffected > 0;
    }
}
