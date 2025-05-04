package gui;

import dao.HoaDonDAO;
import entity.HoaDon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class LichSuBanHangGUI extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtTimKiem;
    private JButton btnTimKiem;
    private HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private List<HoaDon> danhSachHoaDon;

    private final Color primaryColor = new Color(52, 152, 219);
    private final Color secondaryColor = new Color(236, 240, 241);
    private final Color tableHeaderColor = new Color(41, 128, 185);
    private final Color tableHeaderTextColor = Color.WHITE;

    public LichSuBanHangGUI() {
        setLayout(new BorderLayout());
        setBackground(secondaryColor);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        headerPanel.setBackground(secondaryColor);

        JLabel title = new JLabel("Lịch sử bán hàng", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(primaryColor);
        headerPanel.add(title);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        searchPanel.setBackground(secondaryColor);

        txtTimKiem = new JTextField(10);
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchPanel.add(txtTimKiem);

        btnTimKiem = new JButton("Tìm");
        btnTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnTimKiem.setBackground(primaryColor);
        btnTimKiem.setForeground(Color.WHITE);
        btnTimKiem.setFocusPainted(false);
        btnTimKiem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timKiemHoaDon();
            }
        });
        searchPanel.add(btnTimKiem);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(secondaryColor);
        topPanel.add(headerPanel, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[] { "Mã HĐ", "Ngày lập", "Nhân viên", "Tổng tiền" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);

        customizeTable();

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadLichSuHoaDon();

        txtTimKiem.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    timKiemHoaDon();
                }
            }
        });

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
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
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 18));
        table.getTableHeader().setBackground(tableHeaderColor);
        table.getTableHeader().setForeground(tableHeaderTextColor);
        table.setGridColor(new Color(189, 195, 199));
    }

    public void loadLichSuHoaDon() {
        System.out.println("Loading data in LichSuBanHangGUI...");
        danhSachHoaDon = hoaDonDAO.getAllHoaDon();
        if (danhSachHoaDon.isEmpty()) {
            System.out.println("No data found in HoaDonDAO.getAllHoaDon()");
        } else {
            System.out.println("Data loaded: " + danhSachHoaDon.size() + " records");
        }
        hienThiDanhSachHoaDon(danhSachHoaDon);
    }

    private void hienThiDanhSachHoaDon(List<HoaDon> danhSach) {
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (HoaDon hd : danhSach) {
            tableModel.addRow(new Object[] {
                    hd.getMaHD(),
                    sdf.format(hd.getNgayLap()),
                    hd.getTenNhanVien(),
                    String.format("%,.0f", hd.getTongTien()) + " VNĐ"
            });
        }
    }

    private void timKiemHoaDon() {
        String tuKhoa = txtTimKiem.getText().trim();
        if (tuKhoa.isEmpty()) {
            hienThiDanhSachHoaDon(danhSachHoaDon);
        } else {
            try {
                int maHDTimKiem = Integer.parseInt(tuKhoa);
                List<HoaDon> ketQuaTimKiem = danhSachHoaDon.stream()
                        .filter(hd -> hd.getMaHD() == maHDTimKiem)
                        .collect(Collectors.toList());
                hienThiDanhSachHoaDon(ketQuaTimKiem);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mã hóa đơn là một số nguyên!", "Lỗi tìm kiếm",
                        JOptionPane.ERROR_MESSAGE);
                hienThiDanhSachHoaDon(danhSachHoaDon);
            }
        }
    }

    public void refreshLichSuHoaDon() {
        loadLichSuHoaDon();
    }
}