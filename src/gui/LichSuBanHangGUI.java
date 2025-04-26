package gui;

import dao.HoaDonDAO;
import entity.HoaDon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class LichSuBanHangGUI extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public LichSuBanHangGUI() {
        setLayout(new BorderLayout());
        JLabel title = new JLabel("Lịch sử bán hàng", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[] { "Mã HĐ", "Ngày lập", "Nhân viên", "Tổng tiền" }, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadLichSuHoaDon();

        
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) { // Double-click
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        int maHD = (int) tableModel.getValueAt(selectedRow, 0);
                        new HoaDonChiTietGUI((JFrame) SwingUtilities.getWindowAncestor(LichSuBanHangGUI.this), maHD);
                    }
                }
            }
        });
    }

    private void loadLichSuHoaDon() {
        HoaDonDAO hoaDonDAO = new HoaDonDAO();
        List<HoaDon> list = hoaDonDAO.getAllHoaDon();
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (HoaDon hd : list) {
            tableModel.addRow(new Object[] {
                hd.getMaHD(),
                sdf.format(hd.getNgayLap()),
                hd.getTenNhanVien(),
                String.format("%,.0f", hd.getTongTien()) + " VNĐ"
            });
        }
    }
}
