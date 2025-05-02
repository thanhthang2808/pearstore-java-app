package dao;

import entity.NganhHang;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connect.DBConnect;

public class NganhHangDAO {

    public List<NganhHang> getAllNganhHang() {
        List<NganhHang> danhSachNganhHang = new ArrayList<>();
        Connection connection = DBConnect.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT MaNganh, TenNganh, MoTa FROM NganhHang";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                NganhHang nganhHang = new NganhHang(
                        resultSet.getString("MaNganh"),
                        resultSet.getString("TenNganh"),
                        resultSet.getString("MoTa"));
                danhSachNganhHang.add(nganhHang);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy tất cả ngành hàng: " + e.getMessage());
            e.printStackTrace();
        }
        return danhSachNganhHang;
    }

    public NganhHang getNganhHangById(String maNganh) {
        NganhHang nganhHang = null;
        Connection connection = DBConnect.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT TenNganh, MoTa FROM NganhHang WHERE MaNganh = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, maNganh);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                nganhHang = new NganhHang(
                        maNganh,
                        resultSet.getString("TenNganh"),
                        resultSet.getString("MoTa"));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy ngành hàng theo ID: " + e.getMessage());
            e.printStackTrace();
        }
        return nganhHang;
    }

    public boolean themNganhHang(NganhHang nganhHang) {
        Connection connection = DBConnect.getConnection();
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;

        try {
            String sql = "INSERT INTO NganhHang (MaNganh, TenNganh, MoTa) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, nganhHang.getMaNganh());
            preparedStatement.setString(2, nganhHang.getTenNganh());
            preparedStatement.setString(3, nganhHang.getMoTa());
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm ngành hàng: " + e.getMessage());
            e.printStackTrace();
        }
        return rowsAffected > 0;
    }

    public boolean capNhatNganhHang(NganhHang nganhHang) {
        Connection connection = DBConnect.getConnection();
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;

        try {
            String sql = "UPDATE NganhHang SET TenNganh = ?, MoTa = ? WHERE MaNganh = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, nganhHang.getTenNganh());
            preparedStatement.setString(2, nganhHang.getMoTa());
            preparedStatement.setString(3, nganhHang.getMaNganh());
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật ngành hàng: " + e.getMessage());
            e.printStackTrace();
        }
        return rowsAffected > 0;
    }

    public boolean xoaNganhHang(String maNganh) {
        Connection connection = DBConnect.getConnection();
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;

        try {
            String sql = "DELETE FROM NganhHang WHERE MaNganh = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, maNganh);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa ngành hàng: " + e.getMessage());
            e.printStackTrace();
        }
        return rowsAffected > 0;
    }
}
