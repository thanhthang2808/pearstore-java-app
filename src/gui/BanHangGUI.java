package gui;

import bus.SanPhamBUS;
import dao.HoaDonDAO;
import dao.ChiTietHoaDonDAO;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.SanPham;
import entity.TaiKhoan;

import javax.swing.*;
import java.awt.*;
import java.awt.print.PrinterException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import javax.swing.table.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class BanHangGUI extends JPanel {
    private static final long serialVersionUID = 1L;

    private JTextField txtSearch;
    private JButton btnScanQR;
    private JPanel tabContainer;
    private JPanel hoaDonCardPanel;
    private CardLayout cardLayout;
    private int soHoaDon = 1;
    private String currentHoaDonKey = "Hóa đơn 1";
    private final Map<String, JPanel> hoaDonPanels = new HashMap<>();
    private final Map<String, DefaultTableModel> tableModels = new HashMap<>();
    private SanPhamBUS sanPhamBUS = new SanPhamBUS();

    private JLabel lblNumberProductValue;
    private TaiKhoan taiKhoanDangNhap;
    private JTable tblChiTietHoaDon;
    private DefaultTableModel tableModel;
    private JTextField txtVoucher, txtDiscount, txtCustomerAmount, txtChangeAmount, txtTotalAmount, txtCustomerPhone;

    public BanHangGUI(TaiKhoan taiKhoan) {
        this.taiKhoanDangNhap = taiKhoan;
        setLayout(new BorderLayout());
        initCenterPanel();
        initNorthPanel();
        // addCustomTab("Hóa đơn " + soHoaDon); // add tab đầu tiên vào đây để khởi tạo
        // hóa đơn đầu tiên
    }

    private void initNorthPanel() {
        JPanel northPanel = new JPanel(new BorderLayout(10, 0));
        northPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tìm kiếm
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(300, 30));
        btnScanQR = new JButton("📷");
        btnScanQR.setPreferredSize(new Dimension(40, 30));
        searchPanel.add(txtSearch, BorderLayout.CENTER);
        searchPanel.add(btnScanQR, BorderLayout.EAST);

        // Container tab hóa đơn
        tabContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

        // Thêm tab đầu tiên
        addCustomTab("Hóa đơn " + soHoaDon);

        // Nút thêm hóa đơn
        JButton btnAddTab = new JButton("+");
        btnAddTab.setFocusable(false);
        btnAddTab.addActionListener(e -> {
            soHoaDon++;
            addCustomTab("Hóa đơn " + soHoaDon);
        });

        tabContainer.add(btnAddTab);

        // Gộp tìm kiếm và tab hóa đơn
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(tabContainer, BorderLayout.CENTER);

        northPanel.add(topPanel, BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);

        // Thêm DocumentListener cho txtSearch
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                handleSearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                handleSearch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                handleSearch();
            }

            private void handleSearch() {
                String searchText = txtSearch.getText().trim().toUpperCase();
                if (searchText.contains("SP")) {
                    // Tìm kiếm sản phẩm theo mã SP
                    SanPham sanPham = sanPhamBUS.getSanPhamById(searchText);
                    if (sanPham != null) {
                        addToHoaDon(sanPham);
                        SwingUtilities.invokeLater(() -> txtSearch.setText("")); // Use invokeLater
                    }
                }
                if (searchText.length() == 13) {
                    // Tìm kiếm sản phẩm theo mã vạch
                    SanPham sanPham = sanPhamBUS.getSanPhamByMaVach(searchText);
                    if (sanPham != null) {
                        // Thêm sản phẩm vào hóa đơn hiện tại
                        addToHoaDon(sanPham);
                        SwingUtilities.invokeLater(() -> txtSearch.setText("")); // Use invokeLater
                    } else {
                        JOptionPane.showMessageDialog(BanHangGUI.this,
                                "Không tìm thấy sản phẩm có mã vạch: " + searchText, "Thông báo",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    private void addCustomTab(String tabName) {
        if (hoaDonCardPanel == null) {
            hoaDonCardPanel = new JPanel();
            cardLayout = new CardLayout();
            hoaDonCardPanel.setLayout(cardLayout);
            add(hoaDonCardPanel, BorderLayout.CENTER);
        }

        // Tab Panel chứa nút và đóng tab
        JPanel tabPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
        tabPanel.setBackground(new Color(220, 230, 240));
        tabPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        tabPanel.setPreferredSize(new Dimension(110, 30));

        // Nút tab
        JButton btnTab = new JButton(tabName);
        btnTab.setBorder(null);
        btnTab.setContentAreaFilled(false);
        btnTab.setFocusPainted(false);
        btnTab.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnTab.addActionListener(e -> {
            cardLayout.show(hoaDonCardPanel, tabName);
            currentHoaDonKey = tabName;
            updateTabStyles();
        });

        // Nút đóng tab
        JButton btnClose = new JButton("x");
        btnClose.setMargin(new Insets(0, 4, 0, 4));
        btnClose.setBorder(null);
        btnClose.setContentAreaFilled(false);
        btnClose.setForeground(Color.RED);
        btnClose.setFocusPainted(false);
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 12));

        btnClose.addActionListener(e -> {
            hoaDonCardPanel.remove(hoaDonPanels.get(tabName));
            hoaDonPanels.remove(tabName);
            tableModels.remove(tabName); // Remove the table model
            tabContainer.remove(tabPanel);
            tabContainer.revalidate();
            tabContainer.repaint();

            // Nếu tab hiện tại bị xóa thì hiển thị tab đầu tiên
            if (tabName.equals(currentHoaDonKey) && !hoaDonPanels.isEmpty()) {
                String newKey = hoaDonPanels.keySet().iterator().next();
                cardLayout.show(hoaDonCardPanel, newKey);
                currentHoaDonKey = newKey;
                updateTabStyles();
            } else if (hoaDonPanels.isEmpty()) {
                // Nếu không còn hóa đơn nào, tạo một hóa đơn mặc định
                soHoaDon = 1;
                addCustomTab("Hóa đơn " + soHoaDon);
            }
        });

        tabPanel.add(btnTab);
        tabPanel.add(btnClose);
        tabContainer.add(tabPanel, tabContainer.getComponentCount() - 1);
        tabContainer.revalidate();
        tabContainer.repaint();

        // Chi tiết hóa đơn với table
        JPanel hoaDonContent = new JPanel(new BorderLayout());

        // Table chi tiết hóa đơn
        String[] columns = { "Mã SP", "Sản phẩm", "Số lượng", "Đơn giá", "Thành tiền", "Xóa" };
        tableModel = new DefaultTableModel(columns, 0);
        tableModels.put(tabName, tableModel); // Store the table model
        tblChiTietHoaDon = new JTable(tableModel);
        tblChiTietHoaDon.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(tblChiTietHoaDon);
        tblChiTietHoaDon.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JTextField()) {
            @Override
            public boolean stopCellEditing() {
                int row = tblChiTietHoaDon.getEditingRow();
                try {
                    String value = (String) getCellEditorValue();
                    int soLuong = Integer.parseInt(value);
                    double donGia = Double.parseDouble(tableModel.getValueAt(row, 3).toString());
                    tableModel.setValueAt(soLuong * donGia, row, 4);
                    SwingUtilities.invokeLater(() -> calculateTotal());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Số lượng phải là số.");
                    tableModel.setValueAt(1, row, 2);
                }
                return super.stopCellEditing();
            }
        });

        tblChiTietHoaDon.getColumn("Xóa").setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                JButton btnXoa = new JButton("X");
                btnXoa.setToolTipText("Xóa sản phẩm");
                btnXoa.setBorderPainted(false);
                btnXoa.setContentAreaFilled(false);
                btnXoa.setFocusPainted(false);
                return btnXoa;
            }
        });

        tblChiTietHoaDon.getColumn("Xóa").setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            private JButton button = new JButton("X");
            private int selectedRow;

            {
                button.addActionListener(e -> {
                    if (selectedRow >= 0) {
                        if (tblChiTietHoaDon.isEditing()) {
                            tblChiTietHoaDon.getCellEditor().stopCellEditing();
                        }

                        // Xóa dòng
                        tableModel.removeRow(selectedRow);
                        calculateTotal(); // Cập nhật tổng tiền nếu cần
                    }
                });
            }

            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
                    int row, int column) {
                selectedRow = row;
                return button;
            }

            @Override
            public Object getCellEditorValue() {
                return "Xóa";
            }
        });

        TableColumn colXoa = tblChiTietHoaDon.getColumn("Xóa");
        colXoa.setMinWidth(50);
        colXoa.setMaxWidth(60);
        colXoa.setPreferredWidth(55);

        hoaDonContent.add(scrollPane, BorderLayout.CENTER);

        // Phần thanh toán
        JPanel paymentPanel = new JPanel();
        paymentPanel.setLayout(new GridBagLayout());
        paymentPanel.setBorder(BorderFactory.createTitledBorder("Thông tin thanh toán"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Label và ô nhập thông tin
        JLabel lblPhone = new JLabel("Số điện thoại khách hàng:");
        txtCustomerPhone = new JTextField(15);
        JLabel lblVoucher = new JLabel("Mã Voucher:");
        txtVoucher = new JTextField(10);
        JLabel lblNumberProduct = new JLabel("Số lượng sản phẩm:");
        lblNumberProductValue = new JLabel("0");
        JLabel lblDiscount = new JLabel("Chiết khấu:");
        txtDiscount = new JTextField(10);
        JLabel lblTotalAmount = new JLabel("Tổng tiền:");
        txtTotalAmount = new JTextField(10);
        txtTotalAmount.setEditable(false); // Make it read-only
        JLabel lblCustomerAmount = new JLabel("Tiền khách đưa:");
        txtCustomerAmount = new JTextField(10);
        JLabel lblChangeAmount = new JLabel("Tiền thừa trả lại:");
        txtChangeAmount = new JTextField(10);
        txtChangeAmount.setEditable(false); // Make it read-only

        txtCustomerAmount.addActionListener(e -> calculateTotal());

        // Đặt các thành phần vào GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        paymentPanel.add(lblPhone, gbc);
        gbc.gridx = 1;
        paymentPanel.add(txtCustomerPhone, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        paymentPanel.add(lblNumberProduct, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        paymentPanel.add(lblNumberProductValue, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 2;
        paymentPanel.add(lblDiscount, gbc);
        gbc.gridx = 1;
        paymentPanel.add(txtDiscount, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        paymentPanel.add(lblTotalAmount, gbc);
        gbc.gridx = 1;
        paymentPanel.add(txtTotalAmount, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        paymentPanel.add(lblCustomerAmount, gbc);
        gbc.gridx = 1;
        paymentPanel.add(txtCustomerAmount, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        paymentPanel.add(lblChangeAmount, gbc);
        gbc.gridx = 1;
        paymentPanel.add(txtChangeAmount, gbc);

        // Nút thanh toán
        JButton btnThanhToan = new JButton("Thanh toán");
        btnThanhToan.addActionListener(e -> xuatHoaDon());
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        paymentPanel.add(btnThanhToan, gbc);

        // Thêm phần thanh toán vào panel chính
        hoaDonContent.add(paymentPanel, BorderLayout.EAST);

        hoaDonCardPanel.add(hoaDonContent, tabName);
        hoaDonPanels.put(tabName, hoaDonContent);
        cardLayout.show(hoaDonCardPanel, tabName);
        currentHoaDonKey = tabName;

        updateTabStyles();
    }

    private void updateTabStyles() {
        for (Component comp : tabContainer.getComponents()) {
            if (comp instanceof JPanel) { // Kiểm tra nếu là JPanel
                JPanel tabPanel = (JPanel) comp;
                JButton btnTab = (JButton) tabPanel.getComponent(0);

                if (btnTab.getText().equals(currentHoaDonKey)) {
                    btnTab.setBackground(new Color(30, 144, 255));
                    btnTab.setForeground(Color.BLACK);
                    btnTab.setFont(btnTab.getFont().deriveFont(Font.BOLD));
                } else {
                    btnTab.setBackground(null);
                    btnTab.setForeground(Color.GRAY);
                    btnTab.setFont(btnTab.getFont().deriveFont(Font.PLAIN));
                }
            }
        }
    }

    private void initCenterPanel() {
        // Đảm bảo hoaDonCardPanel được khởi tạo đúng cách
        if (hoaDonCardPanel == null) {
            hoaDonCardPanel = new JPanel();
            cardLayout = new CardLayout();
            hoaDonCardPanel.setLayout(cardLayout);
            add(hoaDonCardPanel, BorderLayout.CENTER);
        }
    }

    private void addToHoaDon(SanPham sanPham) {
        DefaultTableModel model = tableModels.get(currentHoaDonKey);
        if (model == null) {
            // Nếu model chưa tồn tại cho hóa đơn hiện tại, tạo mới
            String[] columns = { "Mã SP", "Sản phẩm", "Số lượng", "Đơn giá", "Thành tiền" };
            model = new DefaultTableModel(columns, 0);
            tableModels.put(currentHoaDonKey, model);
        }

        // Kiểm tra xem sản phẩm đã có trong hóa đơn chưa
        for (int i = 0; i < model.getRowCount(); i++) {
            String maSP = (String) model.getValueAt(i, 0);
            if (maSP.equals(sanPham.getMaSP())) {
                // Nếu sản phẩm đã có, tăng số lượng
                int soLuong = (int) model.getValueAt(i, 2);
                model.setValueAt(soLuong + 1, i, 2);
                // Cập nhật thành tiền
                double donGia = sanPham.getGiaBan().doubleValue();
                model.setValueAt(donGia * (soLuong + 1), i, 4);
                calculateTotal(); // Recalculate the total
                return;
            }
        }

        // Nếu sản phẩm chưa có trong hóa đơn, thêm mới
        double donGia = sanPham.getGiaBan().doubleValue();
        Object[] rowData = {
                sanPham.getMaSP(),
                sanPham.getTenSP(),
                1,
                donGia,
                donGia
        };
        model.addRow(rowData);
        calculateTotal(); // Recalculate the total
    }

    // Hàm tính toán
    private void calculateTotal() {
        DefaultTableModel model = tableModels.get(currentHoaDonKey);
        if (model == null) {
            return; // Nếu không có model nào cho hóa đơn hiện tại, không tính toán
        }
        double total = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            double price = Double.parseDouble(model.getValueAt(i, 3).toString());
            int quantity = Integer.parseInt(model.getValueAt(i, 2).toString());
            total += price * quantity;
        }

        int numberProduct = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            numberProduct += Integer.parseInt(model.getValueAt(i, 2).toString());
        }

        lblNumberProductValue.setText(String.valueOf(numberProduct)); // Cập nhật số lượng sản phẩm

        double discount = 0;
        double voucher = 0;
        try {
            discount = Double.parseDouble(txtDiscount.getText());
        } catch (NumberFormatException e) {
            // Xử lý nếu người dùng nhập không phải số
            txtDiscount.setText("0");
        }
        try {
            voucher = Double.parseDouble(txtVoucher.getText());
        } catch (NumberFormatException e) {
            // Xử lý nếu người dùng nhập không phải số
            txtVoucher.setText("0");
        }
        double finalTotal = total - discount - voucher;
        txtTotalAmount.setText(String.format("%.2f", finalTotal));

        double customerAmount = 0;
        try {
            customerAmount = Double.parseDouble(txtCustomerAmount.getText());
        } catch (NumberFormatException e) {
            txtCustomerAmount.setText("0");
        }
        if (customerAmount < finalTotal) {
            txtChangeAmount.setText("0");
        } else {
            double change = customerAmount - finalTotal;
            txtChangeAmount.setText(String.format("%.2f", change));
        }

    }

    private void resetFields() {
        txtCustomerPhone.setText("");
        txtVoucher.setText("");
        txtDiscount.setText("");
        txtCustomerAmount.setText("");
        txtChangeAmount.setText("");
        lblNumberProductValue.setText("0");
        txtTotalAmount.setText("0.00");
    }

    private void xuatHoaDon() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Không có sản phẩm nào trong hóa đơn!", "Cảnh báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        double tongTien = Double.parseDouble(txtTotalAmount.getText().replace(",", ""));
        String tienKhachDuaStr = txtCustomerAmount.getText().replace(",", "");

        if (tienKhachDuaStr == null || tienKhachDuaStr.trim().isEmpty())
            return;

        double tienKhachDua;
        try {
            tienKhachDua = Double.parseDouble(tienKhachDuaStr.replace(",", ""));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số tiền không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (tienKhachDua < tongTien) {
            JOptionPane.showMessageDialog(this, "Tiền khách đưa không đủ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double tienThoiLai = tienKhachDua - tongTien;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String maSP = tableModel.getValueAt(i, 0).toString();
            int soLuongBan = Integer.parseInt(tableModel.getValueAt(i, 2).toString());

            SanPham sp = sanPhamBUS.getSanPhamById(maSP);
            int soLuongConLai = sp.getSoLuong() - soLuongBan;
            if (soLuongConLai < 0) {
                JOptionPane.showMessageDialog(this,
                        "Sản phẩm " + sp.getTenSP() + " không đủ số lượng!\n" + "Còn " + sp.getSoLuong() + " sản phẩm.",
                        "Cảnh báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            sanPhamBUS.capNhatSanPham(maSP, sp.getTenSP(), sp.getGiaBan(), soLuongConLai,
                    sp.getMaVach(), sp.getHinhAnh(), sp.getGiaNhap(), sp.getDonViTinh(), sp.getNganhHang());
        }

        JTextPane textPane = new JTextPane();
        StyledDocument doc = textPane.getStyledDocument();

        SimpleAttributeSet title = new SimpleAttributeSet();
        StyleConstants.setFontFamily(title, "Monospaced");
        StyleConstants.setFontSize(title, 18);
        StyleConstants.setBold(title, true);
        StyleConstants.setAlignment(title, StyleConstants.ALIGN_CENTER);

        SimpleAttributeSet normal = new SimpleAttributeSet();
        StyleConstants.setFontFamily(normal, "Monospaced");
        StyleConstants.setFontSize(normal, 14);

        SimpleAttributeSet boldAmount = new SimpleAttributeSet();
        StyleConstants.setFontFamily(boldAmount, "Monospaced");
        StyleConstants.setFontSize(boldAmount, 15);
        StyleConstants.setBold(boldAmount, true);

        try {
            doc.insertString(doc.getLength(), "                PEARSTORE\n", title);
            doc.insertString(doc.getLength(), "\n", normal);
            doc.insertString(doc.getLength(), "Địa chỉ: 12 Nguyễn Văn Bảo, P4, Q. Gò Vấp, TP.HCM\n", normal);
            doc.insertString(doc.getLength(), "Điện thoại: 0909 123 456\n", normal);
            doc.insertString(doc.getLength(), "=====================================================\n", normal);
            doc.insertString(doc.getLength(), "Nhân viên: " + taiKhoanDangNhap.getNhanVien().getTenNV() + "\n", normal);
            doc.insertString(doc.getLength(),
                    "Ngày: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "\n", normal);
            doc.insertString(doc.getLength(), "-----------------------------------------------------\n", normal);
            doc.insertString(doc.getLength(),
                    String.format("%-20s %5s %10s %12s\n", "Tên SP", "SL", "Đơn giá", "Thành tiền"), normal);

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String tenSP = tableModel.getValueAt(i, 1).toString();
                int soLuong = Integer.parseInt(tableModel.getValueAt(i, 2).toString());
                double donGia = Double.parseDouble(tableModel.getValueAt(i, 3).toString());
                double thanhTien = Double.parseDouble(tableModel.getValueAt(i, 4).toString());
                doc.insertString(doc.getLength(),
                        String.format("%-20s %5d %,10.0f %,12.0f\n", tenSP, soLuong, donGia, thanhTien), normal);
            }

            DecimalFormat df = new DecimalFormat("#,###");
            doc.insertString(doc.getLength(), "-----------------------------------------------------\n", normal);
            doc.insertString(doc.getLength(), String.format("%-30s %19s\n", "Tổng tiền:", df.format(tongTien)),
                    boldAmount);
            doc.insertString(doc.getLength(), String.format("%-30s %19s\n", "Tiền khách đưa:", df.format(tienKhachDua)),
                    boldAmount);
            doc.insertString(doc.getLength(), String.format("%-30s %19s\n", "Tiền thối lại:", df.format(tienThoiLai)),
                    boldAmount);
            doc.insertString(doc.getLength(), "======================================================", normal);
            doc.insertString(doc.getLength(), "\n", normal);
            doc.insertString(doc.getLength(), "                Hẹn gặp lại quý khách!\n", normal);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }

        textPane.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JButton btnPrint = new JButton("In hóa đơn");
        btnPrint.addActionListener(e -> {
            try {
                boolean complete = textPane.print();
                if (complete) {
                    JOptionPane.showMessageDialog(this, "In hóa đơn thành công.");
                } else {
                    JOptionPane.showMessageDialog(this, "In hóa đơn bị hủy.");
                }
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi in: " + ex.getMessage());
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnPrint, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(this, panel, "Hóa đơn", JOptionPane.INFORMATION_MESSAGE);

        HoaDon hd = new HoaDon(new Date(), taiKhoanDangNhap.getNhanVien().getMaNV(),
                taiKhoanDangNhap.getTenDangNhap(), tongTien);
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

            tableModel.setRowCount(0);
            resetFields();
            JOptionPane.showMessageDialog(this, "Thanh toán thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

}
