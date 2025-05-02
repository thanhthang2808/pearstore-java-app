package gui;

import dao.ChiTietHoaDonDAO;
import entity.ChiTietHoaDon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HoaDonChiTietGUI extends JDialog {

    public HoaDonChiTietGUI(JFrame parent, int maHD) {
        super(parent, "Chi tiết hóa đơn - Mã HĐ: " + maHD, true);
        setSize(600, 400);
        setLocationRelativeTo(parent);

        String[] columnNames = { "Mã SP", "Tên SP", "Số lượng", "Đơn giá", "Thành tiền" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        ChiTietHoaDonDAO dao = new ChiTietHoaDonDAO();
        List<ChiTietHoaDon> list = dao.getByMaHD(maHD);
        for (ChiTietHoaDon ct : list) {
            model.addRow(new Object[] {
                    ct.getMaSP(),
                    ct.getTenSP(),
                    ct.getSoLuong(),
                    ct.getDonGia(),
                    ct.getThanhTien()
            });
        }

        setVisible(true);
    }
}
