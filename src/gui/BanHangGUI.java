package gui;

import bus.SanPhamBUS;
import dao.ChiTietHoaDonDAO;
import dao.HoaDonDAO;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.SanPham;
import entity.TaiKhoan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BanHangGUI extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtMaSP;
    private JButton btnThemSanPham;
    private JButton btnXoaSanPham;
    private JButton btnInHoaDon;
    private JLabel lblTongTien;

    private LichSuBanHangGUI lichSuBanHangGUI;
    private SanPhamBUS sanPhamBUS = new SanPhamBUS();

    private TaiKhoan taiKhoanDangNhap;

    public BanHangGUI(TaiKhoan taiKhoan,LichSuBanHangGUI l) {
        this.taiKhoanDangNhap = taiKhoan;
        this.lichSuBanHangGUI = l;

        setLayout(new BorderLayout());

        // Panel nhập liệu phía trên
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTop.add(new JLabel("Mã sản phẩm:"));
        txtMaSP = new JTextField(15);
        pnlTop.add(txtMaSP);

        btnThemSanPham = new JButton("Thêm sản phẩm");
        pnlTop.add(btnThemSanPham);

        btnXoaSanPham = new JButton("Xóa sản phẩm");
        pnlTop.add(btnXoaSanPham);

        btnInHoaDon = new JButton("In hóa đơn");
        pnlTop.add(btnInHoaDon);

        add(pnlTop, BorderLayout.NORTH);

        // Bảng sản phẩm
        String[] columnNames = { "Mã SP", "Tên SP", "Số lượng", "Đơn giá", "Thành tiền" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // chỉ cột số lượng có thể chỉnh sửa
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Tổng tiền
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTongTien = new JLabel("Tổng tiền: 0 VNĐ");
        pnlBottom.add(lblTongTien);
        add(pnlBottom, BorderLayout.SOUTH);

        // Thêm sản phẩm
        btnThemSanPham.addActionListener(e -> {
            String maSPStr = txtMaSP.getText().trim();
            try {
                int maSP = Integer.parseInt(maSPStr);
                SanPham sp = sanPhamBUS.getSanPhamById(maSP);
                if (sp != null) {
                    int soLuong = 0;
                    double thanhTien = sp.getGia() * soLuong;

                    Object[] row = {
                        sp.getMaSP(),
                        sp.getTenSP(),
                        soLuong,
                        sp.getGia(),
                        thanhTien
                    };
                    tableModel.addRow(row);
                    updateTongTien();
                    txtMaSP.setText("");
                }
                 else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Mã sản phẩm phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Xóa sản phẩm
        btnXoaSanPham.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                tableModel.removeRow(selectedRow);
                updateTongTien();
            } else {
                JOptionPane.showMessageDialog(this, "Hãy chọn sản phẩm để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        });

        // In hóa đơn
        btnInHoaDon.addActionListener(e -> xuatHoaDon());

        // Khi sửa số lượng
        table.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();
            if (column == 2) {
                try {
                    int soLuongMoi = Integer.parseInt(tableModel.getValueAt(row, 2).toString());
                    int maSP = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        
                    SanPham sp = sanPhamBUS.getSanPhamById(maSP);
                    if (soLuongMoi > sp.getSoLuong()) {
                        JOptionPane.showMessageDialog(this,
                                "Sản phẩm đã hết hàng! Tồn kho: " + sp.getSoLuong(),
                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                        tableModel.setValueAt(0, row, 2);
                        tableModel.setValueAt(sp.getGia(), row, 4);
                    } else {
                        double donGia = Double.parseDouble(tableModel.getValueAt(row, 3).toString());
                        tableModel.setValueAt(soLuongMoi * donGia, row, 4);
                    }
        
                    updateTongTien();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi định dạng số lượng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        
    }

    private void updateTongTien() {
        double tong = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            try {
                tong += Double.parseDouble(tableModel.getValueAt(i, 4).toString());
            } catch (Exception e) {

            }
        }
        lblTongTien.setText("Tổng tiền: " + String.format("%,.0f", tong) + " VNĐ");
    }
    private double tinhTongTien() {
        double tong = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tong += Double.parseDouble(tableModel.getValueAt(i, 4).toString());
        }
        return tong;
    }
    

    private void xuatHoaDon() {
        StringBuilder hoaDon = new StringBuilder();

        hoaDon.append("=================== HÓA ĐƠN BÁN HÀNG ===================\n");
        hoaDon.append("Nhân viên: ").append(taiKhoanDangNhap.getTenDangNhap()).append("\n");
        hoaDon.append("Ngày: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())).append("\n");
        hoaDon.append("---------------------------------------------------------\n");
        hoaDon.append(String.format("%-20s %5s %10s %12s\n", "Tên SP", "SL", "Đơn giá", "Thành tiền"));

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String tenSP = tableModel.getValueAt(i, 1).toString();
            int soLuong = Integer.parseInt(tableModel.getValueAt(i, 2).toString());
            double donGia = Double.parseDouble(tableModel.getValueAt(i, 3).toString());
            double thanhTien = Double.parseDouble(tableModel.getValueAt(i, 4).toString());

            hoaDon.append(String.format("%-20s %5d %,10.0f %,12.0f\n", tenSP, soLuong, donGia, thanhTien));
        }

        hoaDon.append("--------------------------------------------------------\n");
        hoaDon.append(lblTongTien.getText()).append("\n");
        hoaDon.append("=========================================================");

        JTextArea txtArea = new JTextArea(hoaDon.toString());
        txtArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txtArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(txtArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Hóa đơn", JOptionPane.INFORMATION_MESSAGE);

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int maSP = Integer.parseInt(tableModel.getValueAt(i, 0).toString());
            int soLuongBan = Integer.parseInt(tableModel.getValueAt(i, 2).toString());

            SanPham sp = sanPhamBUS.getSanPhamById(maSP);
            int soLuongConLai = sp.getSoLuong() - soLuongBan;
            if (soLuongConLai < 0) soLuongConLai = 0;

            sanPhamBUS.capNhatSanPham(maSP, sp.getTenSP(), sp.getGia(), soLuongConLai);
        }

        // Lưu hóa đơn trước
        HoaDon hd = new HoaDon(new Date(), taiKhoanDangNhap.getTenDangNhap(), tinhTongTien());
        HoaDonDAO hoaDonDAO = new HoaDonDAO();
        int maHD = hoaDonDAO.themHoaDonVaLayMa(hd);

        if (maHD > 0) {
             ChiTietHoaDonDAO ctDAO = new ChiTietHoaDonDAO();
             for (int i = 0; i < tableModel.getRowCount(); i++) {
             String maSP = tableModel.getValueAt(i, 0).toString();
             String tenSP = tableModel.getValueAt(i, 1).toString();
             int soLuong = Integer.parseInt(tableModel.getValueAt(i, 2).toString());
             double donGia = Double.parseDouble(tableModel.getValueAt(i, 3).toString());
             double thanhTien = Double.parseDouble(tableModel.getValueAt(i, 4).toString());

             ChiTietHoaDon ct = new ChiTietHoaDon(maHD, maSP, tenSP, soLuong, donGia, thanhTien);
             ctDAO.themChiTietHoaDon(ct);
            }
        
        // Gọi phương thức làm mới lịch sử bán hàng
        if (lichSuBanHangGUI != null) {
            lichSuBanHangGUI.refreshLichSuHoaDon();
        } 

        }
        tableModel.setRowCount(0);
        updateTongTien();
    }
}
