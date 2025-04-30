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
        headerPanel.add(lblTitle);
        add(headerPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(secondaryColor);

        // Table hiển thị sản phẩm
        tableModel = new DefaultTableModel(new Object[]{"Mã SP", "Tên SP", "Giá", "Số lượng"}, 0);
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

        addInputField(inputPanel, gbc, "Tên sản phẩm:", txtTenSP = new JTextField(20), 0);
        addInputField(inputPanel, gbc, "Giá:", txtGia = new JTextField(20), 2);
        addInputField(inputPanel, gbc, "Số lượng:", txtSoLuong = new JTextField(20), 4);

        btnThem = createButton("Thêm sản phẩm", inputPanel, gbc, 6);

        // Khu vực cho Cập nhật/Xóa/Xem chi tiết
        JPanel cudPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cudPanel.setBackground(secondaryColor);

        JLabel lblMaSPCUD = new JLabel("Mã SP:");
        lblMaSPCUD.setFont(mainFont);
        cudPanel.add(lblMaSPCUD);

        txtMaSPCUD = new JTextField(10);
        txtMaSPCUD.setFont(mainFont);
        cudPanel.add(txtMaSPCUD);

        btnCapNhat = createButton("Cập nhật", cudPanel);
        btnXoa = createButton("Xóa", cudPanel);
        btnXemChiTiet = createButton("Xem chi tiết", cudPanel);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(inputPanel, BorderLayout.NORTH);
        southPanel.add(cudPanel, BorderLayout.SOUTH);

        mainPanel.add(southPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);

        loadSanPham();
    }

    private void addInputField(JPanel panel, GridBagConstraints gbc, String label, JTextField field, int yPosition) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(mainFont);
        gbc.gridx = 0;
        gbc.gridy = yPosition;
        panel.add(lbl, gbc);

        field.setFont(mainFont);
        gbc.gridy = yPosition + 1;
        panel.add(field, gbc);
    }

    private JButton createButton(String text, JPanel panel, GridBagConstraints gbc, int yPosition) {
        JButton button = new JButton(text);
        button.setFont(mainFont);
        button.setBackground(primaryColor);
        button.setForeground(Color.WHITE);
        button.addActionListener(this);
        gbc.gridy = yPosition;
        panel.add(button, gbc);
        return button;
    }

    private JButton createButton(String text, JPanel panel) {
        JButton button = new JButton(text);
        button.setFont(mainFont);
        button.setBackground(primaryColor);
        button.setForeground(Color.WHITE);
        button.addActionListener(this);
        panel.add(button);
        return button;
    }

    public void loadSanPham() {
        tableModel.setRowCount(0);
        List<SanPham> danhSach = sanPhamBUS.getAllSanPham();
        for (SanPham sp : danhSach) {
            tableModel.addRow(new Object[]{sp.getMaSP(), sp.getTenSP(), sp.getGia(), sp.getSoLuong()});
        }
    }

    private boolean validateInputFields() {
        if (txtTenSP.getText().isEmpty() || txtGia.getText().isEmpty() || txtSoLuong.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            Double.parseDouble(txtGia.getText());
            Integer.parseInt(txtSoLuong.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá và số lượng phải là số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
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
            if (validateInputFields()) {
                String tenSP = txtTenSP.getText();
                double gia = Double.parseDouble(txtGia.getText());
                int soLuong = Integer.parseInt(txtSoLuong.getText());
                if (sanPhamBUS.themSanPham(tenSP, gia, soLuong)) {
                    JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    loadSanPham();
                    clearInputFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm sản phẩm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == btnCapNhat) {
            try {
                int maSP = Integer.parseInt(txtMaSPCUD.getText());
                if (validateInputFields()) {
                    String tenSP = txtTenSP.getText();
                    double gia = Double.parseDouble(txtGia.getText());
                    int soLuong = Integer.parseInt(txtSoLuong.getText());
                    if (sanPhamBUS.capNhatSanPham(maSP, tenSP, gia, soLuong)) {
                        JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        loadSanPham();
                        clearInputFields();
                    } else {
                        JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Mã sản phẩm phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == btnXoa) {
            try {
                int maSP = Integer.parseInt(txtMaSPCUD.getText());
                int option = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa sản phẩm có mã " + maSP + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    if (sanPhamBUS.xoaSanPham(maSP)) {
                        JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
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
            try {
                int maSP = Integer.parseInt(txtMaSPCUD.getText());
                SanPham sanPham = sanPhamBUS.getSanPhamById(maSP);
                if (sanPham != null) {
                    txtTenSP.setText(sanPham.getTenSP());
                    txtGia.setText(String.valueOf(sanPham.getGia()));
                    txtSoLuong.setText(String.valueOf(sanPham.getSoLuong()));
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Mã sản phẩm phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}