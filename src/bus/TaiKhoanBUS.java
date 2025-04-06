package bus;

import dao.TaiKhoanDAO;
import entity.TaiKhoan;

public class TaiKhoanBUS {
    private TaiKhoanDAO taiKhoanDAO;

    public TaiKhoanBUS() {
        taiKhoanDAO = new TaiKhoanDAO();
    }

    public TaiKhoan login(String username, String password) {
        TaiKhoan taiKhoan = taiKhoanDAO.getTaiKhoanByUsername(username);

        if (taiKhoan != null && taiKhoan.getMatKhau().equals(password)) {
            return taiKhoan;
        }
        return null;
    }

    public boolean capNhatMatKhau(String username, String newPassword) {
        return taiKhoanDAO.capNhatMatKhau(username, newPassword);
    }
}