package gui;

import bus.NganhHangBUS;
import bus.SanPhamBUS;
import entity.NganhHang;
import entity.SanPham;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.filechooser.FileNameExtensionFilter;

public class QuanLySanPhamGUI extends JPanel implements ActionListener {

    private Color primaryColor = new Color(52, 152, 219);
    private Color secondaryColor = new Color(236, 240, 241);
    private Font mainFont = new Font("Segoe UI", Font.PLAIN, 16);
    private Font cardFont = new Font("Segoe UI", Font.PLAIN, 14);
    private Font cardTitleFont = new Font("Segoe UI", Font.BOLD, 18);

    private SanPhamBUS sanPhamBUS = new SanPhamBUS();
    private NganhHangBUS nganhHangBUS = new NganhHangBUS();
    private JPanel sanPhamListPanel;
    private JScrollPane sanPhamScrollPane;

    private JTextField txtTimKiem;
    private JComboBox<String> cmbNganhHang;
    private JComboBox<String> cmbFilter;
    private JButton btnThem;
    private JButton btnXoa;

    private List<SanPham> danhSachSanPham;
    private List<NganhHang> danhSachNganhHang;

    private JDialog dialogChiTietSanPham;
    private JTextField txtMaSPChiTiet;
    private JTextField txtTenSPChiTiet;
    private JTextField txtGiaBanChiTiet;
    private JTextField txtSoLuongChiTiet;
    private JTextField txtMaVachChiTiet;
    private JTextField txtHinhAnhChiTiet;
    private JTextField txtGiaNhapChiTiet;
    private JTextField txtDonViTinhChiTiet;
    private JComboBox<String> cmbNganhHangChiTiet;
    private JButton btnLuuChiTiet;

    private SanPham sanPhamDangXem;

    public QuanLySanPhamGUI() {
        setLayout(new BorderLayout());
        setBackground(secondaryColor);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Lấy danh sách sản phẩm và ngành hàng từ BUS
        danhSachSanPham = sanPhamBUS.getAllSanPham();
        // Giả sử có DAO và BUS cho NganhHang
        // danhSachNganhHang = nganhHangBUS.getAllNganhHang();
        danhSachNganhHang = nganhHangBUS.getAllNganhHang(); // Lấy danh sách ngành hàng từ BUS

        // Tạo các thành phần giao diện
        createHeaderPanel();
        createCenterPanel();
        createSouthPanel();
        createDialogChiTietSanPham();

        // Load dữ liệu ban đầu
        loadSanPham(danhSachSanPham);
    }

    private void createHeaderPanel() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        txtTimKiem = new JTextField(20);
        txtTimKiem.setFont(mainFont);
        txtTimKiem.addActionListener(e -> {
            String keyword = txtTimKiem.getText().trim();
            if (!keyword.isEmpty()) {
                danhSachSanPham = timKiemSanPham(keyword);
                loadSanPham(danhSachSanPham);
            } else {
                danhSachSanPham = sanPhamBUS.getAllSanPham(); // Nếu không có từ khóa, tải lại toàn bộ sản phẩm
                loadSanPham(danhSachSanPham);
            }
        });
        txtTimKiem.setBorder(BorderFactory.createTitledBorder("Tìm kiếm sản phẩm"));

        cmbNganhHang = new JComboBox<>();
        cmbNganhHang.setFont(mainFont);
        cmbNganhHang.addItem("Tất cả"); // Thêm mục "Tất cả"
        for (NganhHang nh : danhSachNganhHang) {
            cmbNganhHang.addItem(nh.getTenNganh());
        }
        cmbNganhHang.addActionListener(this);

        cmbFilter = new JComboBox<>();
        cmbFilter.setFont(mainFont);
        cmbFilter.addItem("A-Z");
        cmbFilter.addItem("Z-A");
        cmbFilter.addItem("Giá tăng dần");
        cmbFilter.addItem("Giá giảm dần");
        cmbFilter.addActionListener(this);

        headerPanel.add(txtTimKiem);
        headerPanel.add(cmbNganhHang);
        headerPanel.add(cmbFilter);

        add(headerPanel, BorderLayout.NORTH);
    }

    private void createCenterPanel() {
        sanPhamListPanel = new JPanel();
        sanPhamListPanel.setLayout(new GridLayout(0, 4, 15, 15)); // 4 cột, số hàng tự động
        sanPhamListPanel.setBackground(secondaryColor);

        sanPhamScrollPane = new JScrollPane(sanPhamListPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Scroll dọc

        sanPhamScrollPane.setBorder(null);
        sanPhamScrollPane.getVerticalScrollBar().setUnitIncrement(16); // Mượt hơn khi scroll

        add(sanPhamScrollPane, BorderLayout.CENTER);
    }

    private void createSouthPanel() {
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        southPanel.setBackground(secondaryColor);

        btnThem = new JButton("Thêm sản phẩm");
        btnThem.setFont(mainFont);
        btnThem.setBackground(primaryColor);
        btnThem.setForeground(Color.WHITE);
        btnThem.addActionListener(this);
        southPanel.add(btnThem);

        btnXoa = new JButton("Xóa sản phẩm");
        btnXoa.setFont(mainFont);
        btnXoa.setBackground(primaryColor);
        btnXoa.setForeground(Color.WHITE);
        btnXoa.addActionListener(this);
        southPanel.add(btnXoa);

        add(southPanel, BorderLayout.SOUTH);
    }

    private void createDialogChiTietSanPham() {
        dialogChiTietSanPham = new JDialog((Frame) null, "Chi tiết Sản phẩm", true);
        dialogChiTietSanPham.setLayout(new GridBagLayout());
        dialogChiTietSanPham.setResizable(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // Mã sản phẩm
        JLabel lblMaSP = new JLabel("Mã sản phẩm:");
        lblMaSP.setFont(mainFont);
        gbc.gridx = 0;
        gbc.gridy = row;
        dialogChiTietSanPham.add(lblMaSP, gbc);

        txtMaSPChiTiet = new JTextField(20);
        txtMaSPChiTiet.setFont(mainFont);
        gbc.gridx = 1;
        dialogChiTietSanPham.add(txtMaSPChiTiet, gbc);

        // Tên sản phẩm
        row++;
        JLabel lblTenSP = new JLabel("Tên sản phẩm:");
        lblTenSP.setFont(mainFont);
        gbc.gridx = 0;
        gbc.gridy = row;
        dialogChiTietSanPham.add(lblTenSP, gbc);

        txtTenSPChiTiet = new JTextField(20);
        txtTenSPChiTiet.setFont(mainFont);
        gbc.gridx = 1;
        dialogChiTietSanPham.add(txtTenSPChiTiet, gbc);

        // Giá bán
        row++;
        JLabel lblGiaBan = new JLabel("Giá bán:");
        lblGiaBan.setFont(mainFont);
        gbc.gridx = 0;
        gbc.gridy = row;
        dialogChiTietSanPham.add(lblGiaBan, gbc);

        txtGiaBanChiTiet = new JTextField(20);
        txtGiaBanChiTiet.setFont(mainFont);
        gbc.gridx = 1;
        dialogChiTietSanPham.add(txtGiaBanChiTiet, gbc);

        // Số lượng
        row++;
        JLabel lblSoLuong = new JLabel("Số lượng:");
        lblSoLuong.setFont(mainFont);
        gbc.gridx = 0;
        gbc.gridy = row;
        dialogChiTietSanPham.add(lblSoLuong, gbc);

        txtSoLuongChiTiet = new JTextField(20);
        txtSoLuongChiTiet.setFont(mainFont);
        gbc.gridx = 1;
        dialogChiTietSanPham.add(txtSoLuongChiTiet, gbc);

        // Mã vạch
        row++;
        JLabel lblMaVach = new JLabel("Mã vạch:");
        lblMaVach.setFont(mainFont);
        gbc.gridx = 0;
        gbc.gridy = row;
        dialogChiTietSanPham.add(lblMaVach, gbc);

        txtMaVachChiTiet = new JTextField(20);
        txtMaVachChiTiet.setFont(mainFont);
        gbc.gridx = 1;
        dialogChiTietSanPham.add(txtMaVachChiTiet, gbc);

        // Hình ảnh
        row++;
        JLabel lblHinhAnh = new JLabel("Hình ảnh:");
        lblHinhAnh.setFont(mainFont);
        gbc.gridx = 0;
        gbc.gridy = row;
        dialogChiTietSanPham.add(lblHinhAnh, gbc);

        txtHinhAnhChiTiet = new JTextField(20);
        txtHinhAnhChiTiet.setFont(mainFont);
        gbc.gridx = 1;
        dialogChiTietSanPham.add(txtHinhAnhChiTiet, gbc);

        // Giá nhập
        row++;
        JLabel lblGiaNhap = new JLabel("Giá nhập:");
        lblGiaNhap.setFont(mainFont);
        gbc.gridx = 0;
        gbc.gridy = row;
        dialogChiTietSanPham.add(lblGiaNhap, gbc);

        txtGiaNhapChiTiet = new JTextField(20);
        txtGiaNhapChiTiet.setFont(mainFont);
        gbc.gridx = 1;
        dialogChiTietSanPham.add(txtGiaNhapChiTiet, gbc);

        // Đơn vị tính
        row++;
        JLabel lblDonViTinh = new JLabel("Đơn vị tính:");
        lblDonViTinh.setFont(mainFont);
        gbc.gridx = 0;
        gbc.gridy = row;
        dialogChiTietSanPham.add(lblDonViTinh, gbc);

        txtDonViTinhChiTiet = new JTextField(20);
        txtDonViTinhChiTiet.setFont(mainFont);
        gbc.gridx = 1;
        dialogChiTietSanPham.add(txtDonViTinhChiTiet, gbc);

        // Ngành hàng
        row++;
        JLabel lblNganhHang = new JLabel("Ngành hàng:");
        lblNganhHang.setFont(mainFont);
        gbc.gridx = 0;
        gbc.gridy = row;
        dialogChiTietSanPham.add(lblNganhHang, gbc);

        cmbNganhHangChiTiet = new JComboBox<>();
        cmbNganhHangChiTiet.setFont(mainFont);
        for (NganhHang nh : danhSachNganhHang) {
            cmbNganhHangChiTiet.addItem(nh.getTenNganh());
        }
        gbc.gridx = 1;
        dialogChiTietSanPham.add(cmbNganhHangChiTiet, gbc);

        // Button Lưu
        row++;
        btnLuuChiTiet = new JButton("Lưu thay đổi");
        btnLuuChiTiet.setFont(mainFont);
        btnLuuChiTiet.setBackground(primaryColor);
        btnLuuChiTiet.setForeground(Color.WHITE);
        btnLuuChiTiet.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        dialogChiTietSanPham.add(btnLuuChiTiet, gbc);

        dialogChiTietSanPham.pack();
        dialogChiTietSanPham.setLocationRelativeTo(null);
    }

    private void loadSanPham(List<SanPham> danhSachSanPham) {
        sanPhamListPanel.removeAll(); // Xóa tất cả các card hiện có
        for (SanPham sp : danhSachSanPham) {
            JPanel cardPanel = createSanPhamCard(sp);
            sanPhamListPanel.add(cardPanel);
        }
        sanPhamListPanel.revalidate(); // Cập nhật hiển thị
        sanPhamListPanel.repaint();
    }

    private JPanel createSanPhamCard(SanPham sanPham) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setPreferredSize(new Dimension(200, 250)); // Kích thước card
        cardPanel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Đổi cursor khi hover

        // Tạo các thành phần hiển thị
        JLabel lblHinhAnh = new JLabel(new ImageIcon(
                new ImageIcon(sanPham.getHinhAnh()).getImage().getScaledInstance(180, 120, Image.SCALE_SMOOTH)));
        // Nếu không có ảnh thì có thể để một ảnh mặc định
        lblHinhAnh.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Thêm khoảng cách cho hình ảnh
        lblHinhAnh.setPreferredSize(new Dimension(180, 120)); // Kích thước hình ảnh
        lblHinhAnh.setVerticalAlignment(SwingConstants.CENTER);

        lblHinhAnh.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel lblTenSP = new JLabel(sanPham.getTenSP());
        lblTenSP.setFont(cardTitleFont);
        lblTenSP.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel lblGiaBan = new JLabel("Giá: " + sanPham.getGiaBan() + " VNĐ");
        lblGiaBan.setFont(cardFont);
        lblGiaBan.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel lblSoLuong = new JLabel("Số lượng: " + sanPham.getSoLuong());
        if (sanPham.getSoLuong() <= 0) {
            lblSoLuong.setText("Hết hàng");
            lblSoLuong.setForeground(Color.RED);
        } else {
            lblSoLuong.setForeground(Color.BLACK);
        }
        lblSoLuong.setFont(cardFont);
        lblSoLuong.setHorizontalAlignment(SwingConstants.CENTER);

        // Thêm các thành phần vào card
        cardPanel.add(lblHinhAnh, BorderLayout.NORTH);
        cardPanel.add(lblTenSP, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.add(lblGiaBan);
        infoPanel.add(lblSoLuong);
        cardPanel.add(infoPanel, BorderLayout.SOUTH);

        // Thêm MouseListener để hiển thị chi tiết khi click vào card
        cardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hienThiChiTietSanPham(sanPham.getMaSP());
            }
        });

        return cardPanel;
    }

    private void hienThiChiTietSanPham(String maSP) {
        sanPhamDangXem = sanPhamBUS.getSanPhamById(maSP); // Lấy sản phẩm từ BUS
        if (sanPhamDangXem != null) {
            txtMaSPChiTiet.setText(sanPhamDangXem.getMaSP());
            txtTenSPChiTiet.setText(sanPhamDangXem.getTenSP());
            txtGiaBanChiTiet.setText(sanPhamDangXem.getGiaBan().toString());
            txtSoLuongChiTiet.setText(String.valueOf(sanPhamDangXem.getSoLuong()));
            txtMaVachChiTiet.setText(sanPhamDangXem.getMaVach());
            txtHinhAnhChiTiet.setText(sanPhamDangXem.getHinhAnh());
            txtGiaNhapChiTiet.setText(sanPhamDangXem.getGiaNhap().toString());
            txtDonViTinhChiTiet.setText(sanPhamDangXem.getDonViTinh());
            cmbNganhHangChiTiet.setSelectedItem(sanPhamDangXem.getNganhHang().getTenNganh()); // Chọn đúng ngành hàng

            dialogChiTietSanPham.setVisible(true); // Hiển thị dialog
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm có mã: " + maSP, "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<SanPham> timKiemSanPham(String keyword) {
        return sanPhamBUS.getAllSanPhamByTen(keyword);
    }

    public void ThemSanPhamDialog() {

        JDialog dialog = new JDialog();
        dialog.setLayout(new GridLayout(10, 2, 10, 10));

        // Các trường nhập liệu
        JTextField txtMaSP = new JTextField(20);
        JTextField txtTenSP = new JTextField(20);
        JTextField txtGiaBan = new JTextField(20);
        JTextField txtGiaNhap = new JTextField(20);
        JTextField txtSoLuong = new JTextField(20);
        JTextField txtMaVach = new JTextField(20);
        JTextField txtDonViTinh = new JTextField(20);
        JComboBox<String> cmbNganhHang = new JComboBox<>();
        for (NganhHang nh : danhSachNganhHang) {
            cmbNganhHang.addItem(nh.getTenNganh());
        }

        // Label hình ảnh preview
        JLabel lblHinhAnhPreview = new JLabel("Preview", JLabel.CENTER);
        lblHinhAnhPreview.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Button chọn hình ảnh
        JButton btnChonHinhAnh = new JButton("Chọn hình ảnh");
        String[] hinhAnhFilePath = { "" }; // Để lưu trữ đường dẫn hình ảnh
        btnChonHinhAnh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mở hộp thoại chọn file
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Ảnh", "jpg", "png", "jpeg"));
                int result = fileChooser.showOpenDialog(dialog);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    hinhAnhFilePath[0] = selectedFile.getAbsolutePath();
                    lblHinhAnhPreview.setIcon(new ImageIcon(new ImageIcon(hinhAnhFilePath[0]).getImage()
                            .getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
                }
            }
        });

        // Button thêm sản phẩm
        JButton btnThemSP = new JButton("Thêm sản phẩm");
        btnThemSP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Kiểm tra thông tin nhập vào
                String maSP = txtMaSP.getText();
                String tenSP = txtTenSP.getText();
                BigDecimal giaBan = null;
                BigDecimal giaNhap = null;
                int soLuong = 0;
                try {
                    giaBan = new BigDecimal(txtGiaBan.getText());
                    giaNhap = new BigDecimal(txtGiaNhap.getText());
                    soLuong = Integer.parseInt(txtSoLuong.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Giá và số lượng phải là số hợp lệ!", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String donViTinh = txtDonViTinh.getText();
                String maVach = txtMaVach.getText();
                String tenNganhHang = (String) cmbNganhHang.getSelectedItem();
                NganhHang nganhHang = null;
                for (NganhHang nh : danhSachNganhHang) {
                    if (nh.getTenNganh().equals(tenNganhHang)) {
                        nganhHang = nh;
                        break;
                    }
                }

                // Kiểm tra tính hợp lệ
                if (maSP.isEmpty() || tenSP.isEmpty() || giaBan.compareTo(BigDecimal.ZERO) <= 0 || soLuong <= 0
                        || donViTinh.isEmpty() || nganhHang == null) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin hợp lệ!", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Gọi BUS để thêm sản phẩm
                boolean result = sanPhamBUS.themSanPham(maSP, tenSP, giaBan, soLuong, maVach, hinhAnhFilePath[0],
                        giaNhap, donViTinh, nganhHang);
                if (result) {
                    JOptionPane.showMessageDialog(dialog, "Thêm sản phẩm thành công!", "Thành công",
                            JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose(); // Đóng dialog khi thêm thành công
                } else {
                    JOptionPane.showMessageDialog(dialog, "Thêm sản phẩm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Thêm các thành phần vào dialog
        dialog.add(new JLabel("Mã sản phẩm:"));
        dialog.add(txtMaSP);
        dialog.add(new JLabel("Tên sản phẩm:"));
        dialog.add(txtTenSP);
        dialog.add(new JLabel("Giá bán:"));
        dialog.add(txtGiaBan);
        dialog.add(new JLabel("Giá nhập:"));
        dialog.add(txtGiaNhap);
        dialog.add(new JLabel("Mã vạch:"));
        dialog.add(txtMaVach);
        dialog.add(new JLabel("Số lượng:"));
        dialog.add(txtSoLuong);
        dialog.add(new JLabel("Đơn vị tính:"));
        dialog.add(txtDonViTinh);
        dialog.add(new JLabel("Ngành hàng:"));
        dialog.add(cmbNganhHang);
        dialog.add(new JLabel("Chọn hình ảnh:"));
        dialog.add(btnChonHinhAnh);
        dialog.add(lblHinhAnhPreview);
        dialog.add(btnThemSP);

        dialog.setSize(400, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnThem) {
            this.ThemSanPhamDialog(); // Mở dialog thêm sản phẩm

        } else if (e.getSource() == btnXoa) {
            String maSP = JOptionPane.showInputDialog(this, "Nhập mã sản phẩm cần xóa:", "Xóa sản phẩm",
                    JOptionPane.QUESTION_MESSAGE);
            if (maSP == null || maSP.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mã sản phẩm cần xóa!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            int option = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn xóa sản phẩm có mã " + maSP + "?", "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                if (sanPhamBUS.xoaSanPham(maSP)) {
                    JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!", "Thành công",
                            JOptionPane.INFORMATION_MESSAGE);
                    danhSachSanPham = sanPhamBUS.getAllSanPham(); // Cập nhật danh sách sản phẩm
                    loadSanPham(danhSachSanPham);
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa sản phẩm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == btnLuuChiTiet) {
            // Lấy thông tin từ các trường chi tiết
            String tenSP = txtTenSPChiTiet.getText();
            BigDecimal giaBan = null;
            BigDecimal giaNhap = null;
            int soLuong = 0;
            String maVach = txtMaVachChiTiet.getText();
            String hinhAnh = txtHinhAnhChiTiet.getText();
            try {
                giaBan = new BigDecimal(txtGiaBanChiTiet.getText());
                giaNhap = new BigDecimal(txtGiaNhapChiTiet.getText());
                soLuong = Integer.parseInt(txtSoLuongChiTiet.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Giá và số lượng phải là số hợp lệ!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            String donViTinh = txtDonViTinhChiTiet.getText();
            String tenNganhHang = (String) cmbNganhHangChiTiet.getSelectedItem();
            NganhHang nganhHang = null;
            for (NganhHang nh : danhSachNganhHang) {
                if (nh.getTenNganh().equals(tenNganhHang)) {
                    nganhHang = nh;
                    break;
                }
            }
            // Kiểm tra dữ liệu
            if (tenSP == null || tenSP.trim().isEmpty() || giaBan == null || giaBan.compareTo(BigDecimal.ZERO) < 0
                    || soLuong < 0 || giaNhap == null || giaNhap.compareTo(BigDecimal.ZERO) < 0 || donViTinh == null
                    || donViTinh.trim().isEmpty() || nganhHang == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin hợp lệ!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Gọi BUS để cập nhật sản phẩm
            if (sanPhamBUS.capNhatSanPham(sanPhamDangXem.getMaSP(), tenSP, giaBan, soLuong, maVach, hinhAnh, giaNhap,
                    donViTinh, nganhHang)) {
                JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công!", "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
                dialogChiTietSanPham.dispose();
                danhSachSanPham = sanPhamBUS.getAllSanPham();
                loadSanPham(danhSachSanPham);
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == cmbNganhHang) {
            String selectedNganhHang = (String) cmbNganhHang.getSelectedItem();
            System.out.println("Selected Nganh Hang: " + selectedNganhHang);
            if (selectedNganhHang != null && !selectedNganhHang.equals("Tất cả")) {
                for (NganhHang nh : danhSachNganhHang) {
                    if (nh.getTenNganh().equals(selectedNganhHang)) {
                        danhSachSanPham = sanPhamBUS.getAllSanPhamByNganhHang(nh.getMaNganh());
                        break;
                    }
                }
                loadSanPham(danhSachSanPham);
            } else {
                // Nếu chọn "Tất cả", tải lại toàn bộ danh sách sản phẩm
                danhSachSanPham = sanPhamBUS.getAllSanPham();
                loadSanPham(danhSachSanPham);
            }
            // Tải lại danh sách sản phẩm
        } else if (e.getSource() == cmbFilter) {
            String selectedFilter = (String) cmbFilter.getSelectedItem();
            if (selectedFilter != null) {
                switch (selectedFilter) {
                    case "A-Z":
                        danhSachSanPham.sort((sp1, sp2) -> sp1.getTenSP().compareTo(sp2.getTenSP()));
                        loadSanPham(danhSachSanPham);
                        break;
                    case "Z-A":
                        danhSachSanPham.sort((sp1, sp2) -> sp2.getTenSP().compareTo(sp1.getTenSP()));
                        loadSanPham(danhSachSanPham);
                        break;
                    case "Giá tăng dần":
                        danhSachSanPham.sort((sp1, sp2) -> sp1.getGiaBan().compareTo(sp2.getGiaBan()));
                        loadSanPham(danhSachSanPham);
                        break;
                    case "Giá giảm dần":
                        danhSachSanPham.sort((sp1, sp2) -> sp2.getGiaBan().compareTo(sp1.getGiaBan()));
                        loadSanPham(danhSachSanPham);
                        break;
                }

            }
        }
    }
}