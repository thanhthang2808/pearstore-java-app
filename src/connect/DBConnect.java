package connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnect {
    private static Connection connection;
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=testData;trustServerCertificate=true;encrypt=true";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "123456";

    public static Connection getConnection() {
        if (connection == null) {
            synchronized (DBConnect.class) {
                if (connection == null) {
                    try {
                        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                        System.out.println("Connected to SQL Server successfully!");
                        System.out.println("Database name: " + connection.getCatalog());
                        createTablesIfNotExists();
                    } catch (ClassNotFoundException e) {
                        System.err.println("SQL Server JDBC driver not found!");
                        e.printStackTrace();
                    } catch (SQLException e) {
                        System.err.println("Failed to connect to SQL Server!");
                        e.printStackTrace();
                    }
                }
            }
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
            // Tạo bảng NhanVien
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

            // Tạo bảng TaiKhoan
            String createTaiKhoanTableSQL = "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'dbo' AND TABLE_NAME = 'TaiKhoan') "
                    + "CREATE TABLE TaiKhoan ("
                    + "    TenDangNhap VARCHAR(50) PRIMARY KEY,"
                    + "    MatKhau VARCHAR(50) NOT NULL,"
                    + "    MaNV VARCHAR(20) NOT NULL,"
                    + "    FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV)"
                    + ");";
            statement.executeUpdate(createTaiKhoanTableSQL);

            // Tạo bảng NganhHang
            String createNganhHangTableSQL = "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'dbo' AND TABLE_NAME = 'NganhHang') "
                    + "CREATE TABLE NganhHang ("
                    + "    MaNganh VARCHAR(20) PRIMARY KEY," // Mã ngành hàng là VARCHAR
                    + "    TenNganh NVARCHAR(100) NOT NULL,"
                    + "    MoTa NVARCHAR(255)"
                    + ");";
            statement.executeUpdate(createNganhHangTableSQL);

            // Tạo bảng SanPham
            String createSanPhamTableSQL = "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'dbo' AND TABLE_NAME = 'SanPham') "
                    + "CREATE TABLE SanPham ("
                    + "    MaSP VARCHAR(20) PRIMARY KEY," // Mã sản phẩm là VARCHAR
                    + "    TenSP NVARCHAR(100) NOT NULL,"
                    + "    GiaBan DECIMAL(18, 2) NOT NULL,"
                    + "    SoLuong INT NOT NULL,"
                    + "    MaVach VARCHAR(50),"
                    + "    HinhAnh VARCHAR(255),"
                    + "    GiaNhap DECIMAL(18, 2) NOT NULL,"
                    + "    DonViTinh NVARCHAR(50),"
                    + "    MaNganh VARCHAR(20) NOT NULL," // Khóa ngoại tham chiếu bảng NganhHang
                    + "    FOREIGN KEY (MaNganh) REFERENCES NganhHang(MaNganh)"
                    + ");";
            statement.executeUpdate(createSanPhamTableSQL);

            // Tạo bảng HoaDon
            String createHoaDonTableSQL = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='HoaDon' AND xtype='U')\n"
                    +
                    "BEGIN\n" +
                    "   CREATE TABLE HoaDon (\n" +
                    "       MaHD INT IDENTITY(1,1) PRIMARY KEY,\n" +
                    "       NgayLap DATETIME,\n" +
                    "       MaNV VARCHAR(20) FOREIGN KEY REFERENCES NhanVien(MaNV),\n" +
                    "       TenNhanVien NVARCHAR(100),\n" +
                    "       TongTien FLOAT\n" +
                    "   )\n" +
                    "END";
            statement.executeUpdate(createHoaDonTableSQL);

            // Tạo bảng ChiTietHoaDon
            String createChiTietHoaDonSQL = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='ChiTietHoaDon' AND xtype='U')\n"
                    +
                    "BEGIN\n" +
                    "    CREATE TABLE ChiTietHoaDon (\n" +
                    "        MaCTHD INT IDENTITY(1,1) PRIMARY KEY,\n" +
                    "        MaHD INT FOREIGN KEY REFERENCES HoaDon(MaHD),\n" +
                    "        MaSP VARCHAR(20),\n" +
                    "        TenSP NVARCHAR(100),\n" +
                    "        SoLuong INT,\n" +
                    "        DonGia FLOAT,\n" +
                    "        ThanhTien FLOAT\n" +
                    "        FOREIGN KEY (MaSP) REFERENCES SanPham(MaSP)" +
                    "    )\n" +
                    "END";
            statement.executeUpdate(createChiTietHoaDonSQL);

        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo bảng: " + e.getMessage());
            e.printStackTrace();
        }
    }
}