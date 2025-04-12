package gui;

import bus.SanPhamBUS;
import entity.SanPham;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class QuanLySanPhamGUI extends JPanel implements ActionListener {

    private Color primaryColor = new Color(52, 152, 219);
    private Color secondaryColor = new Color(236, 240, 241);
    private Font mainFont = new Font("Segoe UI", Font.PLAIN, 16);
    private Font titleFont = new Font("Segoe UI", Font.BOLD, 24);

    private SanPhamBUS sanPhamBUS = new SanPhamBUS();
    private DefaultTableModel tableModel;
    private JTable sanPhamTable;

    private JTextField txtTenSP;
    private JTextField txtGia;
    private JTextField txtSoLuong;
    private JTextField txtMaSPCUD;

    private JButton btnThem;
    private JButton btnCapNhat;
    private JButton btnXoa;
    private JButton btnXemChiTiet;

    public QuanLySanPhamGUI() {
        setLayout(new BorderLayout());
        setBackground(secondaryColor);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(primaryColor);
        JLabel lblTitle = new JLabel("Quản lý Sản phẩm");
        lblTitle.setFont(titleFont);
        lblTitle.setForeground(Color.WHITE);
        add(headerPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(secondaryColor);

        // Table hiển thị sản phẩm
        tableModel = new DefaultTableModel(new Object[] { "Mã SP", "Tên SP", "Giá", "Số lượng" }, 0);
        sanPhamTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(sanPhamTable);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
    
        

        // Form nhập liệu và nút chức năng
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(secondaryColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2; // Mặc định chiếm 2 cột

        JLabel lblTenSP = new JLabel("Tên sản phẩm:");
        lblTenSP.setFont(mainFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(lblTenSP, gbc);

        txtTenSP = new JTextField(20);
        txtTenSP.setFont(mainFont);
        gbc.gridy = 1;
        inputPanel.add(txtTenSP, gbc);

        JLabel lblGia = new JLabel("Giá:");
        lblGia.setFont(mainFont);
        gbc.gridy = 2;
        inputPanel.add(lblGia, gbc);

        txtGia = new JTextField(20);
        txtGia.setFont(mainFont);
        gbc.gridy = 3;
        inputPanel.add(txtGia, gbc);

        JLabel lblSoLuong = new JLabel("Số lượng:");
        lblSoLuong.setFont(mainFont);
        gbc.gridy = 4;
        inputPanel.add(lblSoLuong, gbc);

        txtSoLuong = new JTextField(20);
        txtSoLuong.setFont(mainFont);
        gbc.gridy = 5;
        inputPanel.add(txtSoLuong, gbc);

        btnThem = new JButton("Thêm sản phẩm");
        btnThem.setFont(mainFont);
        btnThem.setBackground(primaryColor);
        btnThem.setForeground(Color.WHITE);
        btnThem.addActionListener(this);
        gbc.gridy = 6;
        inputPanel.add(btnThem, gbc);

        // Khu vực cho Cập nhật/Xóa/Xem chi tiết
        JPanel cudPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cudPanel.setBackground(secondaryColor);

        JLabel lblMaSPCUD = new JLabel("Mã SP:");
        lblMaSPCUD.setFont(mainFont);
        cudPanel.add(lblMaSPCUD);

        txtMaSPCUD = new JTextField(10);
        txtMaSPCUD.setFont(mainFont);
        cudPanel.add(txtMaSPCUD);

        btnCapNhat = new JButton("Cập nhật");
        btnCapNhat.setFont(mainFont);
        btnCapNhat.setBackground(primaryColor);
        btnCapNhat.setForeground(Color.WHITE);
        btnCapNhat.addActionListener(this);
        cudPanel.add(btnCapNhat);

        btnXoa = new JButton("Xóa");
        btnXoa.setFont(mainFont);
        btnXoa.setBackground(primaryColor);
        btnXoa.setForeground(Color.WHITE);
        btnXoa.addActionListener(this);
        cudPanel.add(btnXoa);

        btnXemChiTiet = new JButton("Xem chi tiết");
        btnXemChiTiet.setFont(mainFont);
        btnXemChiTiet.setBackground(primaryColor);
        btnXemChiTiet.setForeground(Color.WHITE);
        btnXemChiTiet.addActionListener(this);
        cudPanel.add(btnXemChiTiet);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(inputPanel, BorderLayout.NORTH);
        southPanel.add(cudPanel, BorderLayout.SOUTH);

        mainPanel.add(southPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        loadSanPham();
    }

    private void loadSanPham() {
        tableModel.setRowCount(0);
        List<SanPham> danhSach = sanPhamBUS.getAllSanPham();
        for (SanPham sp : danhSach) {
            tableModel.addRow(new Object[] { sp.getMaSP(), sp.getTenSP(), sp.getGia(), sp.getSoLuong() });
        }
    }

    private void hienThiChiTietSanPham(int maSP) {
        SanPham sanPham = sanPhamBUS.getSanPhamById(maSP);
        if (sanPham != null) {
            txtTenSP.setText(sanPham.getTenSP());
            txtGia.setText(String.valueOf(sanPham.getGia()));
            txtSoLuong.setText(String.valueOf(sanPham.getSoLuong()));
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm có mã: " + maSP, "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearInputFields() {
        txtTenSP.setText("");
        txtGia.setText("");
        txtSoLuong.setText("");
        txtMaSPCUD.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnThem) {
            String tenSP = txtTenSP.getText();
            try {
                double gia = Double.parseDouble(txtGia.getText());
                try {
                    int soLuong = Integer.parseInt(txtSoLuong.getText());
                    if (!tenSP.isEmpty() && gia >= 0 && soLuong >= 0) {
                        if (sanPhamBUS.themSanPham(tenSP, gia, soLuong)) {
                            JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!", "Thành công",
                                    JOptionPane.INFORMATION_MESSAGE);
                            loadSanPham();
                            clearInputFields();
                        } else {
                            JOptionPane.showMessageDialog(this, "Thêm sản phẩm thất bại!", "Lỗi",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin hợp lệ!", "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên!", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Giá phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == btnCapNhat) {
            String maSPStr = txtMaSPCUD.getText();
            try {
                int maSP = Integer.parseInt(maSPStr);
                String tenSP = txtTenSP.getText();
                try {
                    double gia = Double.parseDouble(txtGia.getText());
                    try {
                        int soLuong = Integer.parseInt(txtSoLuong.getText());
                        if (!tenSP.isEmpty() && gia >= 0 && soLuong >= 0) {
                            if (sanPhamBUS.capNhatSanPham(maSP, tenSP, gia, soLuong)) {
                                JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công!", "Thành công",
                                        JOptionPane.INFORMATION_MESSAGE);
                                loadSanPham();
                                clearInputFields();
                            } else {
                                JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thất bại!", "Lỗi",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin hợp lệ!", "Lỗi",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên!", "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Giá phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Mã sản phẩm phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == btnXoa) {
            String maSPStr = txtMaSPCUD.getText();
            try {
                int maSP = Integer.parseInt(maSPStr);
                int option = JOptionPane.showConfirmDialog(this,
                        "Bạn có chắc chắn muốn xóa sản phẩm có mã " + maSP + "?", "Xác nhận xóa",
                        JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    if (sanPhamBUS.xoaSanPham(maSP)) {
                        JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!", "Thành công",
                                JOptionPane.INFORMATION_MESSAGE);
                        loadSanPham();
                        clearInputFields();
                    } else {
                        JOptionPane.showMessageDialog(this, "Xóa sản phẩm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Mã sản phẩm phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == btnXemChiTiet) {
            String maSPStr = txtMaSPCUD.getText();
            try {
                int maSP = Integer.parseInt(maSPStr);
                hienThiChiTietSanPham(maSP);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Mã sản phẩm phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}