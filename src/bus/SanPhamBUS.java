package bus;

import dao.SanPhamDAO;
import entity.SanPham;
import java.util.List;

public class SanPhamBUS {
    private SanPhamDAO sanPhamDAO;

    public SanPhamBUS() {
        sanPhamDAO = new SanPhamDAO();
    }

    public List<SanPham> getAllSanPham() {
        return sanPhamDAO.getAllSanPham();
    }

    public SanPham getSanPhamById(int maSP) {
        return sanPhamDAO.getSanPhamById(maSP);
    }

    public boolean themSanPham(String tenSP, double gia, int soLuong) {
        if (tenSP == null || tenSP.trim().isEmpty()) {
            return false; // Tên sản phẩm không được rỗng
        }
        if (gia < 0 || soLuong < 0) {
            return false; // Giá và số lượng không được âm
        }
        SanPham sanPham = new SanPham(0, tenSP, gia, soLuong); // MaSP sẽ được auto-increment trong DB
        return sanPhamDAO.themSanPham(sanPham);
    }

    public boolean capNhatSanPham(int maSP, String tenSP, double gia, int soLuong) {
        if (tenSP == null || tenSP.trim().isEmpty()) {
            return false; // Tên sản phẩm không được rỗng
        }
        if (gia < 0 || soLuong < 0) {
            return false; // Giá và số lượng không được âm
        }
        SanPham sanPham = new SanPham(maSP, tenSP, gia, soLuong);
        return sanPhamDAO.capNhatSanPham(sanPham);
    }

    public boolean xoaSanPham(int maSP) {
        return sanPhamDAO.xoaSanPham(maSP);
    }
}
