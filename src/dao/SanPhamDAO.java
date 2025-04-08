package dao;

import entity.SanPham;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SanPhamDAO {

    public List<SanPham> getAllSanPham() {
        List<SanPham> danhSachSanPham = new ArrayList<>();
        Connection connection = DBConnect.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT MaSP, TenSP, Gia, SoLuong FROM SanPham";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                SanPham sp = new SanPham(
                        resultSet.getInt("MaSP"),
                        resultSet.getString("TenSP"),
                        resultSet.getDouble("Gia"),
                        resultSet.getInt("SoLuong"));
                danhSachSanPham.add(sp);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy tất cả sản phẩm: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return danhSachSanPham;
    }

    public SanPham getSanPhamById(int maSP) {
        SanPham sanPham = null;
        Connection connection = DBConnect.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT TenSP, Gia, SoLuong FROM SanPham WHERE MaSP = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, maSP);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                sanPham = new SanPham(
                        maSP,
                        resultSet.getString("TenSP"),
                        resultSet.getDouble("Gia"),
                        resultSet.getInt("SoLuong"));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy sản phẩm theo ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return sanPham;
    }

    public boolean themSanPham(SanPham sanPham) {
        Connection connection = DBConnect.getConnection();
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;

        try {
            String sql = "INSERT INTO SanPham (TenSP, Gia, SoLuong) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, sanPham.getTenSP());
            preparedStatement.setDouble(2, sanPham.getGia());
            preparedStatement.setInt(3, sanPham.getSoLuong());
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm sản phẩm: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rowsAffected > 0;
    }

    public boolean capNhatSanPham(SanPham sanPham) {
        Connection connection = DBConnect.getConnection();
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;

        try {
            String sql = "UPDATE SanPham SET TenSP = ?, Gia = ?, SoLuong = ? WHERE MaSP = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, sanPham.getTenSP());
            preparedStatement.setDouble(2, sanPham.getGia());
            preparedStatement.setInt(3, sanPham.getSoLuong());
            preparedStatement.setInt(4, sanPham.getMaSP());
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật sản phẩm: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rowsAffected > 0;
    }

    public boolean xoaSanPham(int maSP) {
        Connection connection = DBConnect.getConnection();
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;

        try {
            String sql = "DELETE FROM SanPham WHERE MaSP = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, maSP);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa sản phẩm: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rowsAffected > 0;
    }
}