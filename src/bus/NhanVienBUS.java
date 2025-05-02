package bus;

import dao.NhanVienDAO;
import entity.NhanVien;

import java.util.List;

public class NhanVienBUS {

    private NhanVienDAO nhanVienDAO;

    public NhanVienBUS() {
        nhanVienDAO = new NhanVienDAO(); // Khởi tạo đối tượng DAO
    }

    // Lấy danh sách nhân viên
    public List<NhanVien> layDanhSachNhanVien() {
        return nhanVienDAO.layDanhSachNhanVien();
    }

    // Thêm nhân viên
    public boolean themNhanVien(NhanVien nv) {
        // Kiểm tra tính hợp lệ của dữ liệu trước khi thêm
        if (nv.getMaNV() == null || nv.getMaNV().isEmpty()) {
            System.err.println("❌ Mã nhân viên không hợp lệ.");
            return false;
        }
        if (nv.getTenNV() == null || nv.getTenNV().isEmpty()) {
            System.err.println("❌ Tên nhân viên không hợp lệ.");
            return false;
        }

        // Gọi DAO để thêm nhân viên vào cơ sở dữ liệu
        return nhanVienDAO.themNhanVien(nv);
    }

    // Cập nhật nhân viên
    public boolean suaNhanVien(NhanVien nv) {
        // Kiểm tra tính hợp lệ của dữ liệu trước khi sửa
        if (nv.getMaNV() == null || nv.getMaNV().isEmpty()) {
            System.err.println("❌ Mã nhân viên không hợp lệ.");
            return false;
        }

        // Gọi DAO để cập nhật nhân viên trong cơ sở dữ liệu
        return nhanVienDAO.suaNhanVien(nv);
    }

    // Xoá nhân viên
    public boolean xoaNhanVien(String maNV) {
        // Kiểm tra tính hợp lệ của mã nhân viên trước khi xoá
        if (maNV == null || maNV.isEmpty()) {
            System.err.println("❌ Mã nhân viên không hợp lệ.");
            return false;
        }

        // Gọi DAO để xoá nhân viên khỏi cơ sở dữ liệu
        return nhanVienDAO.xoaNhanVien(maNV);
    }
}
