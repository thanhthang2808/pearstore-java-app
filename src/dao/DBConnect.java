package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnect {
    private static Connection connection;
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=pearstoreDB;trustServerCertificate=true;encrypt=true";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "123456";

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                synchronized (DBConnect.class) {
                    if (connection == null || connection.isClosed()) {
                        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                        System.out.println("Kết nối đến SQL Server thành công");
                        createTablesIfNotExists();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Không thể kết nối SQL Server: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing connection.");
                e.printStackTrace();
            } finally {
                connection = null;
            }
        }
    }

    private static void createTablesIfNotExists() {
        try (Statement statement = connection.createStatement()) {

            // Bảng NhanVien 
            String createNhanVienTableSQL = """
                IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'NhanVien')
                BEGIN
                    CREATE TABLE NhanVien (
                        MaNV VARCHAR(20) PRIMARY KEY,
                        TenNV NVARCHAR(100) NOT NULL,
                        NgaySinh DATE,
                        GioiTinh BIT,
                        Email VARCHAR(100),
                        SoDienThoai VARCHAR(20),
                        ChucVu VARCHAR(50)
                    )
                END
            """;
            statement.executeUpdate(createNhanVienTableSQL);

            // Bảng TaiKhoan
            String createTaiKhoanTableSQL = """
                IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'TaiKhoan')
                BEGIN
                    CREATE TABLE TaiKhoan (
                        TenDangNhap VARCHAR(50) PRIMARY KEY,
                        MatKhau VARCHAR(50) NOT NULL,
                        MaNV VARCHAR(20) NOT NULL,
                        FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV)
                    )
                END
            """;            
            statement.executeUpdate(createTaiKhoanTableSQL);

            // Bảng SanPham 
            String createSanPhamTableSQL = "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'dbo' AND TABLE_NAME = 'SanPham') " +
                    "CREATE TABLE SanPham (" +
                    "    MaSP INT PRIMARY KEY IDENTITY(1,1)," +
                    "    TenSP NVARCHAR(100) NOT NULL," +
                    "    Gia DECIMAL(18, 2) NOT NULL," +
                    "    SoLuong INT NOT NULL" +
                    ");";
            statement.executeUpdate(createSanPhamTableSQL);

            // Bảng HoaDon 
            String createHoaDonTableSQL = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='HoaDon' AND xtype='U')\n" +
                    "BEGIN\n" +
                    "   CREATE TABLE HoaDon (\n" +
                    "       MaHD VARCHAR(20) PRIMARY KEY,\n" +
                    "       NgayLap DATETIME,\n" +
                    "       TenNhanVien NVARCHAR(100),\n" +
                    "       TongTien FLOAT\n" +
                    "   )\n" +
                    "END";
            statement.executeUpdate(createHoaDonTableSQL);

            // Bảng ChiTietHoaDon 
            String createChiTietHoaDonSQL = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='ChiTietHoaDon' AND xtype='U')\n" +
                    "BEGIN\n" +
                    "    CREATE TABLE ChiTietHoaDon (\n" +
                    "        MaCTHD VARCHAR(20) PRIMARY KEY,\n" +
                    "        MaHD VARCHAR(20) FOREIGN KEY REFERENCES HoaDon(MaHD),\n" +
                    "        MaSP INT,\n" +
                    "        TenSP NVARCHAR(100),\n" +
                    "        SoLuong INT,\n" +
                    "        DonGia FLOAT,\n" +
                    "        ThanhTien FLOAT\n" +
                    "    )\n" +
                    "END";
            statement.executeUpdate(createChiTietHoaDonSQL);

        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo bảng: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        getConnection();
        closeConnection();
    }
}
