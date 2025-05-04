package bus;

import dao.SanPhamDAO;
import entity.NganhHang;
import entity.SanPham;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.JOptionPane;

public class SanPhamBUS {
    private SanPhamDAO sanPhamDAO;

    public SanPhamBUS() {
        sanPhamDAO = new SanPhamDAO();
    }

    public List<SanPham> getAllSanPham() {
        return sanPhamDAO.getAllSanPham();
    }

    public List<SanPham> getAllSanPhamByTen(String tenSP) {
        return sanPhamDAO.getAllSanPhamByTen(tenSP);
    }

    public SanPham getSanPhamById(String maSP) {
        return sanPhamDAO.getSanPhamById(maSP);
    }

    public SanPham getSanPhamByMaVach(String maVach) {
        return sanPhamDAO.getSanPhamByMaVach(maVach);
    }

    public List<SanPham> getAllSanPhamByNganhHang(String maNganhHang) {
        return sanPhamDAO.getAllSanPhamByNganhHang(maNganhHang);
    }

    public boolean themSanPham(String maSP, String tenSP, BigDecimal giaBan, int soLuong, String maVach, String hinhAnh,
            BigDecimal giaNhap, String donViTinh, NganhHang nganhHang) {
        SanPham sanPham = new SanPham(maSP, tenSP, giaBan, soLuong, maVach, hinhAnh, giaNhap, donViTinh, nganhHang);
        if (sanPhamDAO.getSanPhamById(maSP) != null) {
            JOptionPane.showMessageDialog(null, "Mã sản phẩm đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (sanPhamDAO.getSanPhamByMaVach(maVach) != null) {
            JOptionPane.showMessageDialog(null, "Mã vạch đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return sanPhamDAO.themSanPham(sanPham);
    }

    public boolean capNhatSanPham(String maSP, String tenSP, BigDecimal giaBan, int soLuong, String maVach,
            String hinhAnh, BigDecimal giaNhap, String donViTinh, NganhHang nganhHang) {

        if (maSP == null || maSP.trim().isEmpty()) {
            System.err.println("Lỗi: Mã sản phẩm không được để trống.");
            return false;
        }
        if (!maSP.matches("^SP\\d+$")) {
            System.err.println("Lỗi: Mã sản phẩm phải bắt đầu bằng 'SP' và theo sau là các chữ số.");
            return false;
        }
        if (tenSP == null || tenSP.trim().isEmpty()) {
            System.err.println("Lỗi: Tên sản phẩm không được để trống.");
            return false;
        }
        if (!tenSP.matches("^[\\p{L}\\p{N}\\s]+$")) {
            System.err.println("Lỗi: Tên sản phẩm phải là các chữ cái, chữ số hoặc khoảng trắng.");
            return false;
        }
        if (giaBan == null || giaBan.compareTo(BigDecimal.ZERO) < 0) {
            System.err.println("Lỗi: Giá bán phải là số không âm.");
            return false;
        }
        if (giaNhap == null || giaNhap.compareTo(BigDecimal.ZERO) < 0) {
            System.err.println("Lỗi: Giá nhập phải là số không âm.");
            return false;
        }
        if (soLuong < 0) {
            System.err.println("Lỗi: Số lượng không được âm.");
            return false;
        }
        if (maVach != null && !maVach.trim().isEmpty() && !maVach.matches("^\\d{13}$")) {
            System.err.println("Lỗi: Mã vạch phải là dãy 13 chữ số.");
            return false;
        }
        if (donViTinh == null || donViTinh.trim().isEmpty()) {
            System.err.println("Lỗi: Đơn vị tính không được để trống.");
            return false;
        }
        if (nganhHang == null) {
            System.err.println("Lỗi: Ngành hàng không được để trống.");
            return false;
        }

        SanPham sanPham = new SanPham(maSP, tenSP, giaBan, soLuong, maVach, hinhAnh, giaNhap, donViTinh, nganhHang);
        return sanPhamDAO.capNhatSanPham(sanPham);
    }

    public boolean xoaSanPham(String maSP) {
        if (maSP == null || maSP.trim().isEmpty()) {
            return false; // Mã sản phẩm không được rỗng
        }
        return sanPhamDAO.xoaSanPham(maSP);
    }

}