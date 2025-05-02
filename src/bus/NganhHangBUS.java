package bus;

import dao.NganhHangDAO;
import entity.NganhHang;
import java.util.List;

public class NganhHangBUS {

    private NganhHangDAO nganhHangDAO;

    public NganhHangBUS() {
        nganhHangDAO = new NganhHangDAO();
    }

    public List<NganhHang> getAllNganhHang() {
        return nganhHangDAO.getAllNganhHang();
    }

    public NganhHang getSanPhamById(String maNganh) {
        return nganhHangDAO.getNganhHangById(maNganh);
    }

    public boolean themNganhHang(String maNganh, String tenNganh, String moTa) {
        if (maNganh == null || maNganh.trim().isEmpty()) {
            return false; // Mã ngành hàng không được rỗng
        }
        if (tenNganh == null || tenNganh.trim().isEmpty()) {
            return false; // Tên ngành hàng không được rỗng
        }
        NganhHang nganhHang = new NganhHang(maNganh, tenNganh, moTa);
        return nganhHangDAO.themNganhHang(nganhHang);
    }

    public boolean capNhatNganhHang(String maNganh, String tenNganh, String moTa) {
        if (maNganh == null || maNganh.trim().isEmpty()) {
            return false; // Mã ngành hàng không được rỗng
        }
        if (tenNganh == null || tenNganh.trim().isEmpty()) {
            return false; // Tên ngành hàng không được rỗng
        }
        NganhHang nganhHang = new NganhHang(maNganh, tenNganh, moTa);
        return nganhHangDAO.capNhatNganhHang(nganhHang);
    }

    public boolean xoaNganhHang(String maNganh) {
        if (maNganh == null || maNganh.trim().isEmpty()) {
            return false; // Mã ngành hàng không được rỗng
        }
        return nganhHangDAO.xoaNganhHang(maNganh);
    }
}