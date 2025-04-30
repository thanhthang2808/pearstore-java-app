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

    // Tạo màu
    private final Color primaryColor = new Color(52, 152, 219);
    private final Color secondaryColor = new Color(236, 240, 241);
    private final Color tableHeaderColor = new Color(41, 128, 185);
    private final Color tableHeaderTextColor = Color.WHITE;

    public LichSuBanHangGUI() {
        setLayout(new BorderLayout());
        setBackground(secondaryColor); // Đặt màu nền

        // Thiết lập tiêu đề
        JLabel title = new JLabel("Lịch sử bán hàng", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(primaryColor); // Đặt màu chữ
        add(title, BorderLayout.NORTH);

        // Thiết lập bảng
        tableModel = new DefaultTableModel(new String[]{"Mã HĐ", "Ngày lập", "Nhân viên", "Tổng tiền"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa bất kỳ ô nào
            }
        };
        table = new JTable(tableModel);

        // Tùy chỉnh bảng
        customizeTable();

        // Thêm bảng vào giao diện
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Tải dữ liệu
        loadLichSuHoaDon();

        // Xử lý sự kiện click chuột
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

    private void customizeTable() {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Tăng kích thước chữ trong bảng
        table.setRowHeight(30); // Tăng chiều cao hàng
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 18)); // Tăng kích thước chữ tiêu đề cột
        table.getTableHeader().setBackground(tableHeaderColor); //màu tiêu đề cột
        table.getTableHeader().setForeground(tableHeaderTextColor); //màu chữ tiêu đề cột
        table.setGridColor(new Color(189, 195, 199)); // Màu đường viền 
    }

    public void loadLichSuHoaDon() {
        System.out.println("Loading data in LichSuBanHangGUI...");
        HoaDonDAO hoaDonDAO = new HoaDonDAO();
        List<HoaDon> list = hoaDonDAO.getAllHoaDon();

        if (list.isEmpty()) {
            System.out.println("No data found in HoaDonDAO.getAllHoaDon()");
        } else {
            System.out.println("Data loaded: " + list.size() + " records");
        }

        tableModel.setRowCount(0); // Xóa dữ liệu cũ
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (HoaDon hd : list) {
            tableModel.addRow(new Object[]{
                hd.getMaHD(),
                sdf.format(hd.getNgayLap()),
                hd.getTenNhanVien(),
                String.format("%,.0f", hd.getTongTien()) + " VNĐ"
            });
        }
    }

    public void refreshLichSuHoaDon() {
        loadLichSuHoaDon();
    }
}