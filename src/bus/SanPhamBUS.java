package bus;

import dao.SanPhamDAO;
import entity.NganhHang;
import entity.SanPham;
import java.math.BigDecimal;
import java.util.List;

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
        if (maSP == null || maSP.trim().isEmpty()) {
            return false; // Mã sản phẩm không được rỗng
        }
        if (tenSP == null || tenSP.trim().isEmpty()) {
            return false; // Tên sản phẩm không được rỗng
        }
        if (giaBan == null || giaBan.compareTo(BigDecimal.ZERO) < 0 || soLuong < 0 || giaNhap == null
                || giaNhap.compareTo(BigDecimal.ZERO) < 0) {
            return false; // Giá bán, giá nhập không được âm và không được null
        }
        SanPham sanPham = new SanPham(maSP, tenSP, giaBan, soLuong, maVach, hinhAnh, giaNhap, donViTinh, nganhHang);
        return sanPhamDAO.themSanPham(sanPham);
    }

    public boolean capNhatSanPham(String maSP, String tenSP, BigDecimal giaBan, int soLuong, String maVach,
            String hinhAnh, BigDecimal giaNhap, String donViTinh, NganhHang nganhHang) {
        if (maSP == null || maSP.trim().isEmpty()) {
            return false; // Mã sản phẩm không được rỗng
        }
        if (tenSP == null || tenSP.trim().isEmpty()) {
            return false; // Tên sản phẩm không được rỗng
        }
        if (giaBan == null || giaBan.compareTo(BigDecimal.ZERO) < 0 || soLuong < 0 || giaNhap == null
                || giaNhap.compareTo(BigDecimal.ZERO) < 0) {
            return false; // Giá bán, giá nhập không được âm và không được null
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