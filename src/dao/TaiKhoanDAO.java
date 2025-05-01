package dao;

import entity.TaiKhoan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaiKhoanDAO {

    public TaiKhoan getTaiKhoanByUsername(String username) {
        Connection connection = DBConnect.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        TaiKhoan taiKhoan = null;

        try {
            String sql = "SELECT TenDangNhap, MatKhau, MaNV FROM TaiKhoan WHERE TenDangNhap = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                taiKhoan = new TaiKhoan();
                taiKhoan.setTenDangNhap(resultSet.getString("TenDangNhap"));
                taiKhoan.setMatKhau(resultSet.getString("MatKhau"));
                taiKhoan.setMaNV(resultSet.getString("MaNV"));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn thông tin tài khoản theo tên đăng nhập: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (preparedStatement != null)
                    preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return taiKhoan;
    }

    public boolean themTaiKhoan(TaiKhoan taiKhoan) {
        Connection connection = DBConnect.getConnection();
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;

        try {
            String sql = "INSERT INTO TaiKhoan (TenDangNhap, MatKhau, MaNV) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, taiKhoan.getTenDangNhap());
            preparedStatement.setString(2, taiKhoan.getMatKhau());
            preparedStatement.setString(3, taiKhoan.getMaNV());
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm tài khoản: " + e.getMessage());
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

    public boolean capNhatMatKhau(String username, String newPassword) {
        Connection connection = DBConnect.getConnection();
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;

        try {
            String sql = "UPDATE TaiKhoan SET MatKhau = ? WHERE TenDangNhap = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, username);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật mật khẩu: " + e.getMessage());
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

    public boolean xoaTaiKhoan(String username) {
        Connection connection = DBConnect.getConnection();
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;

        try {
            String sql = "DELETE FROM TaiKhoan WHERE TenDangNhap = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa tài khoản: " + e.getMessage());
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