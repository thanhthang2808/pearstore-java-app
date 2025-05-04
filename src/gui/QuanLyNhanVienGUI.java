package gui;

import bus.NhanVienBUS;
import entity.ChucVu;
import entity.NhanVien;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.util.List;
import java.util.regex.Pattern;

public class QuanLyNhanVienGUI extends JPanel implements ActionListener {

    private Color primaryColor = new Color(52, 152, 219);
    private Color secondaryColor = new Color(236, 240, 241);
    private Font mainFont = new Font("Segoe UI", Font.PLAIN, 16);
    private Font titleFont = new Font("Segoe UI", Font.BOLD, 24);

    private NhanVienBUS nhanVienBUS = new NhanVienBUS();
    private DefaultTableModel tableModel;
    private JTable table;

    private JTextField txtMaNV, txtTenNV, txtNgaySinh, txtEmail, txtSoDT;
    private JComboBox<String> cboGioiTinh;
    private JComboBox<ChucVu> cboChucVu;
    private JButton btnThem, btnCapNhat, btnXoa, btnTim;

    public QuanLyNhanVienGUI() {
        setLayout(new BorderLayout());
        setBackground(secondaryColor);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Tiêu đề
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(primaryColor);
        JLabel lblTitle = new JLabel("Quản lý Nhân viên");
        lblTitle.setFont(titleFont);
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);
        add(headerPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(
                new Object[] { "Mã NV", "Tên NV", "Ngày sinh", "Giới tính", "Email", "SĐT", "Chức vụ" }, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ==== Form nhập liệu + nút chức năng dạng lưới 3 cột x 2 dòng ====
        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        gridPanel.setBackground(secondaryColor);
        gridPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // ==== Dòng 1: Các field nhập liệu (chia 3 ô) ====
        JPanel col1 = new JPanel(new GridLayout(3, 1, 5, 5));
        col1.setBackground(secondaryColor);
        col1.add(new JLabel("Mã NV:"));
        txtMaNV = new JTextField();
        col1.add(txtMaNV);
        col1.add(new JLabel("Ngày sinh (yyyy-mm-dd):"));
        txtNgaySinh = new JTextField();
        col1.add(txtNgaySinh);

        JPanel col2 = new JPanel(new GridLayout(3, 1, 5, 5));
        col2.setBackground(secondaryColor);
        col2.add(new JLabel("Tên NV:"));
        txtTenNV = new JTextField();
        col2.add(txtTenNV);
        col2.add(new JLabel("Giới tính:"));
        cboGioiTinh = new JComboBox<>(new String[] { "Nam", "Nữ" });
        col2.add(cboGioiTinh);

        JPanel col3 = new JPanel(new GridLayout(3, 1, 5, 5));
        col3.setBackground(secondaryColor);
        col3.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        col3.add(txtEmail);
        col3.add(new JLabel("Số điện thoại:"));
        txtSoDT = new JTextField();
        col3.add(txtSoDT);
        col3.add(new JLabel("Chức vụ:"));
        cboChucVu = new JComboBox<>(ChucVu.values());
        col3.add(cboChucVu);

        gridPanel.add(col1);
        gridPanel.add(col2);
        gridPanel.add(col3);

        // ==== Dòng 2: Các nút chức năng ====
        btnThem = createButton("Thêm");
        btnCapNhat = createButton("Cập nhật");
        btnXoa = createButton("Xóa");
        btnTim = createButton("Tìm theo mã");

        JPanel btnPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel1.setBackground(secondaryColor);
        btnPanel1.add(btnThem);

        JPanel btnPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel2.setBackground(secondaryColor);
        btnPanel2.add(btnCapNhat);

        JPanel btnPanel3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel3.setBackground(secondaryColor);
        btnPanel3.add(btnXoa);
        btnPanel3.add(btnTim);

        gridPanel.add(btnPanel1);
        gridPanel.add(btnPanel2);
        gridPanel.add(btnPanel3);

        add(gridPanel, BorderLayout.SOUTH);

        // Load dữ liệu
        loadNhanVien();

        // Sự kiện
        btnThem.addActionListener(this);
        btnCapNhat.addActionListener(this);
        btnXoa.addActionListener(this);
        btnTim.addActionListener(this);

        // Click vào dòng trong bảng thì load dữ liệu lên form
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtMaNV.setText(tableModel.getValueAt(row, 0).toString());
                    txtTenNV.setText(tableModel.getValueAt(row, 1).toString());
                    txtNgaySinh.setText(tableModel.getValueAt(row, 2).toString());
                    cboGioiTinh.setSelectedItem(tableModel.getValueAt(row, 3).toString());
                    txtEmail.setText(tableModel.getValueAt(row, 4).toString());
                    txtSoDT.setText(tableModel.getValueAt(row, 5).toString());
                    cboChucVu.setSelectedItem(ChucVu.valueOf(tableModel.getValueAt(row, 6).toString()));
                }
            }
        });
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(mainFont);
        btn.setBackground(primaryColor);
        btn.setForeground(Color.WHITE);
        return btn;
    }

    private void loadNhanVien() {
        tableModel.setRowCount(0);
        List<NhanVien> list = nhanVienBUS.layDanhSachNhanVien();
        for (NhanVien nv : list) {
            tableModel.addRow(new Object[] {
                    nv.getMaNV(), nv.getTenNV(), nv.getNgaySinh().toString(),
                    nv.isGioiTinh() ? "Nam" : "Nữ",
                    nv.getEmail(), nv.getSoDienThoai(),
                    nv.getChucVu().name()
            });
        }
    }

    private void timTheoMa(String maNV) {
        for (NhanVien nv : nhanVienBUS.layDanhSachNhanVien()) {
            if (nv.getMaNV().equalsIgnoreCase(maNV)) {
                txtMaNV.setText(nv.getMaNV());
                txtTenNV.setText(nv.getTenNV());
                txtNgaySinh.setText(nv.getNgaySinh().toString());
                cboGioiTinh.setSelectedItem(nv.isGioiTinh() ? "Nam" : "Nữ");
                txtEmail.setText(nv.getEmail());
                txtSoDT.setText(nv.getSoDienThoai());
                cboChucVu.setSelectedItem(nv.getChucVu());
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên có mã: " + maNV, "Lỗi",
                JOptionPane.ERROR_MESSAGE);
    }

    private NhanVien getNhanVienFromForm() {
        return new NhanVien(
                txtMaNV.getText().trim(),
                txtTenNV.getText().trim(),
                Date.valueOf(txtNgaySinh.getText().trim()),
                cboGioiTinh.getSelectedItem().equals("Nam"),
                txtEmail.getText().trim(),
                txtSoDT.getText().trim(),
                (ChucVu) cboChucVu.getSelectedItem());
    }

    private boolean validateNhanVienForm() {
        String maNV = txtMaNV.getText().trim();
        String tenNV = txtTenNV.getText().trim();
        String ngaySinh = txtNgaySinh.getText().trim();
        String email = txtEmail.getText().trim();
        String soDT = txtSoDT.getText().trim();

        if (!Pattern.matches("^NV\\d{3}$", maNV)) {
            JOptionPane.showMessageDialog(this, "Mã nhân viên phải bắt đầu bằng 'NV' và theo sau là 3 chữ số!");
            return false;
        }
        if (!Pattern.matches("^[\\p{L}\\s]+$", tenNV)) {
            JOptionPane.showMessageDialog(this,
                    "Tên nhân viên phải là các chữ cái (bao gồm tiếng Việt có dấu) cách nhau bởi khoảng trắng!");
            return false;
        }
        if (!Pattern.matches("^\\d{4}-\\d{2}-\\d{2}$", ngaySinh)) {
            JOptionPane.showMessageDialog(this, "Ngày sinh phải có định dạng yyyy-mm-dd!");
            return false;
        }
        if (!Pattern.matches("^[\\w-\\.]+@[\\w-]+(\\.[\\w-]+)*$", email)) {
            JOptionPane.showMessageDialog(this, "Email phải có định dạng hợp lệ (ví dụ: abc@xyz.com)!");
            return false;
        }
        if (!Pattern.matches("^\\d{10}$", soDT)) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải là dãy 10 chữ số!");
            return false;
        }
        return true;
    }

    private void clearForm() {
        txtMaNV.setText("");
        txtTenNV.setText("");
        txtNgaySinh.setText("");
        txtEmail.setText("");
        txtSoDT.setText("");
        cboGioiTinh.setSelectedIndex(0);
        cboChucVu.setSelectedIndex(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btnThem) {
            try {
                if (validateNhanVienForm()) {
                    NhanVien nv = getNhanVienFromForm();
                    if (nhanVienBUS.themNhanVien(nv)) {
                        JOptionPane.showMessageDialog(this, "Thêm thành công!");
                        loadNhanVien();
                        clearForm();
                    } else {
                        JOptionPane.showMessageDialog(this, "Thêm thất bại!");
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng kiểm tra lại dữ liệu!");
            }

        } else if (src == btnCapNhat) {
            try {
                NhanVien nv = getNhanVienFromForm();
                if (nhanVienBUS.suaNhanVien(nv)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                    loadNhanVien();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!");
            }

        } else if (src == btnXoa) {
            String ma = txtMaNV.getText().trim();
            int option = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn xóa nhân viên mã " + ma + "?", "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                if (nhanVienBUS.xoaNhanVien(ma)) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    loadNhanVien();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại!");
                }
            }

        } else if (src == btnTim) {
            String ma = txtMaNV.getText().trim();
            timTheoMa(ma);
        }
    }
}