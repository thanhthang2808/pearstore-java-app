package gui;

import bus.TaiKhoanBUS;
import entity.ChucVu;
import entity.TaiKhoan;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePageGUI extends JFrame {

    private Color primaryColor = new Color(52, 152, 219);
    private Color secondaryColor = new Color(236, 240, 241);
    private Color textColor = new Color(44, 62, 80);
    private Font mainFont = new Font("Segoe UI", Font.PLAIN, 16);
    private Font titleFont = new Font("Segoe UI", Font.BOLD, 24);
    private Font welcomeFont = new Font("Segoe UI", Font.ITALIC, 18);

    private JButton selectedButton = null;
    private Color defaultButtonColor;
    private Color selectedButtonColor = new Color(100, 149, 237);
    private TaiKhoan taiKhoanDangNhap;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private BanHangGUI banHangGUI;
    private QuanLySanPhamGUI quanLySanPhamGUI;
    private ChucVu chucVu;

    public HomePageGUI(TaiKhoan taiKhoan) {
        this.taiKhoanDangNhap = taiKhoan;
        this.chucVu = taiKhoan.getNhanVien().getChucVu();
        setTitle("Phần mềm quản lý bán hàng");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel leftMenuPanel = new JPanel();
        leftMenuPanel.setLayout(new BoxLayout(leftMenuPanel, BoxLayout.Y_AXIS));
        leftMenuPanel.setBackground(new Color(184, 225, 255));
        leftMenuPanel.setPreferredSize(new Dimension(200, getHeight()));
        leftMenuPanel.setBorder(new EmptyBorder(20, 10, 20, 10));

        JLabel logoLabel = new JLabel("PEARSTORE");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 20));
        logoLabel.setForeground(primaryColor);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftMenuPanel.add(logoLabel);
        leftMenuPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        ImageIcon avatarIcon;
        if (chucVu == ChucVu.QUAN_LY) {
            avatarIcon = new ImageIcon(getClass().getResource("/icons/manager.png"));
        } else {
            avatarIcon = new ImageIcon(getClass().getResource("/icons/user.png"));
        }
        Image scaledAvatar = avatarIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        JLabel avatarLabel = new JLabel(new ImageIcon(scaledAvatar));
        avatarLabel.setPreferredSize(new Dimension(50, 50));
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftMenuPanel.add(avatarLabel);
        leftMenuPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel welcomeLabel = new JLabel("Xin chào,");
        welcomeLabel.setFont(mainFont);
        welcomeLabel.setForeground(textColor);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftMenuPanel.add(welcomeLabel);

        JLabel usernameLabel = new JLabel(taiKhoanDangNhap.getTenDangNhap());
        usernameLabel.setFont(welcomeFont);
        usernameLabel.setForeground(primaryColor);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftMenuPanel.add(usernameLabel);
        leftMenuPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JButton homeButton = createMenuButton("Trang chủ", "/icons/home.png");
        homeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton sellButton = createMenuButton("Bán hàng", "/icons/sell.png");
        sellButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton historyButton = createMenuButton("Lịch sử bán hàng", "/icons/history.png");
        historyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton productButton = createMenuButton("Sản phẩm", "/icons/box.png");
        productButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton employeeButton = createMenuButton("Quản lý nhân viên", "/icons/employee.png");
        employeeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton statisticButton = createMenuButton("Báo cáo doanh thu", "/icons/statistic.png");
        statisticButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton overviewButton = createMenuButton("Thống kê", "/icons/overview.png");
        overviewButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton accountButton = createMenuButton("Tài khoản", "/icons/account.png");
        accountButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton logoutButton = createMenuButton("Đăng xuất", "/icons/logout.png");
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        homeButton.addActionListener(e -> showPanel("Trang chủ"));
        sellButton.addActionListener(e -> showPanel("Bán hàng"));
        historyButton.addActionListener(e -> showPanel("Lịch sử bán hàng"));
        productButton.addActionListener(e -> showPanel("Quản lý sản phẩm"));
        employeeButton.addActionListener(e -> showPanel("Quản lý nhân viên"));
        statisticButton.addActionListener(e -> showPanel("Báo cáo doanh thu"));
        overviewButton.addActionListener(e -> showPanel("Thống kê"));
        accountButton.addActionListener(e -> showPanel("Tài khoản"));
        logoutButton.addActionListener(e -> {
            new LoginGUI();
            dispose();
        });

        leftMenuPanel.add(homeButton);
        if (chucVu == ChucVu.NHAN_VIEN) {
            leftMenuPanel.add(sellButton);
            leftMenuPanel.add(historyButton);
        }
        if (chucVu == ChucVu.QUAN_LY) {
            leftMenuPanel.add(employeeButton);
        }
        leftMenuPanel.add(productButton);
        leftMenuPanel.add(statisticButton);
        if (chucVu == ChucVu.QUAN_LY) {
            leftMenuPanel.add(overviewButton);
        }
        leftMenuPanel.add(accountButton);
        leftMenuPanel.add(Box.createVerticalGlue());
        leftMenuPanel.add(logoutButton);

        mainPanel.add(leftMenuPanel, BorderLayout.WEST);

        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);
        contentPanel.setBackground(secondaryColor);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        DashboardPanel dashboardPanel = new DashboardPanel();
        contentPanel.add(dashboardPanel, "Trang chủ");

        banHangGUI = new BanHangGUI(taiKhoanDangNhap);
        contentPanel.add(banHangGUI, "Bán hàng");

        LichSuBanHangGUI lichSuBanHangGUI = new LichSuBanHangGUI();
        contentPanel.add(lichSuBanHangGUI, "Lịch sử bán hàng");

        quanLySanPhamGUI = new QuanLySanPhamGUI();
        contentPanel.add(quanLySanPhamGUI, "Quản lý sản phẩm");

        QuanLyNhanVienGUI quanLyNhanVienGUI = new QuanLyNhanVienGUI();
        contentPanel.add(quanLyNhanVienGUI, "Quản lý nhân viên");

        BaoCaoDoanhThuGUI baoCaoDoanhThuGUI = new BaoCaoDoanhThuGUI(taiKhoanDangNhap.getTenDangNhap());
        contentPanel.add(baoCaoDoanhThuGUI, "Báo cáo doanh thu");

        ThongKeGUI thongKeGUI = new ThongKeGUI(taiKhoanDangNhap);
        contentPanel.add(thongKeGUI, "Thống kê");

        contentPanel.add(createTaiKhoanPanel(taiKhoanDangNhap), "Tài khoản");

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);

        showPanel("Trang chủ");
    }

    private JButton createMenuButton(String text, String iconPath) {
        JButton button = new JButton(text);
        try {
            ImageIcon icon = null;
            try {
                Image img = new ImageIcon(getClass().getResource(iconPath)).getImage();
                icon = new ImageIcon(img.getScaledInstance(20, 20, Image.SCALE_SMOOTH));
            } catch (Exception e) {
                System.err.println("Không tìm thấy icon: " + iconPath);
            }
            button.setIcon(icon);
        } catch (Exception e) {
            System.err.println("Không tìm thấy icon: " + iconPath);
        }
        button.setFont(mainFont);
        button.setForeground(textColor);
        button.setBackground(new Color(184, 225, 255));
        defaultButtonColor = button.getBackground();
        button.setBorder(new EmptyBorder(15, 15, 15, 0));
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setIconTextGap(10);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedButton != null && selectedButton != button) {
                    selectedButton.setBackground(defaultButtonColor);
                }
                selectedButton = button;
                selectedButton.setBackground(selectedButtonColor);
            }
        });
        return button;
    }

    private JPanel createTaiKhoanPanel(TaiKhoan taiKhoan) {
        JPanel taiKhoanPanel = new JPanel();
        taiKhoanPanel.setLayout(new BorderLayout());
        taiKhoanPanel.setBackground(secondaryColor);
        taiKhoanPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Tiêu đề
        JLabel titleLabel = new JLabel("Thông tin Tài khoản");
        titleLabel.setFont(mainFont.deriveFont(Font.BOLD, 20));
        titleLabel.setForeground(primaryColor);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        taiKhoanPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel thông tin dạng lưới
        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 15, 10));
        infoPanel.setBackground(secondaryColor);
        // infoPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Loại bỏ padding cũ,
        // thêm vào scrollPane
        infoPanel.setPreferredSize(new Dimension(400, 250)); // Tăng chiều cao để chứa đủ thông tin

        JLabel lblMaNV = new JLabel("Mã nhân viên:");
        lblMaNV.setFont(mainFont);
        JLabel lblTenNV = new JLabel("Tên nhân viên:");
        lblTenNV.setFont(mainFont);
        JLabel lblNgaySinh = new JLabel("Ngày sinh:");
        lblNgaySinh.setFont(mainFont);
        JLabel lblGioiTinh = new JLabel("Giới tính:");
        lblGioiTinh.setFont(mainFont);
        JLabel lblChucVu = new JLabel("Chức vụ:");
        lblChucVu.setFont(mainFont);
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(mainFont);
        JLabel lblSoDienThoai = new JLabel("Số điện thoại:");
        lblSoDienThoai.setFont(mainFont);

        JLabel txtMaNV = new JLabel(taiKhoan.getNhanVien().getMaNV());
        txtMaNV.setFont(mainFont);
        JLabel txtTenNV = new JLabel(taiKhoan.getNhanVien().getTenNV());
        txtTenNV.setFont(mainFont);
        JLabel txtNgaySinh = new JLabel(taiKhoan.getNhanVien().getNgaySinh().toString());
        txtNgaySinh.setFont(mainFont);
        JLabel txtGioiTinh = new JLabel(taiKhoan.getNhanVien().isGioiTinh() ? "Nam" : "Nữ");
        txtGioiTinh.setFont(mainFont);
        JLabel txtChucVu = new JLabel(taiKhoan.getNhanVien().getChucVu().name());
        txtChucVu.setFont(mainFont);
        JLabel txtEmail = new JLabel(taiKhoan.getNhanVien().getEmail());
        txtEmail.setFont(mainFont);
        JLabel txtSoDienThoai = new JLabel(taiKhoan.getNhanVien().getSoDienThoai());
        txtSoDienThoai.setFont(mainFont);

        // Thêm các label và text vào infoPanel
        infoPanel.add(lblMaNV);
        infoPanel.add(txtMaNV);
        infoPanel.add(lblTenNV);
        infoPanel.add(txtTenNV);
        infoPanel.add(lblNgaySinh);
        infoPanel.add(txtNgaySinh);
        infoPanel.add(lblGioiTinh);
        infoPanel.add(txtGioiTinh);
        infoPanel.add(lblChucVu);
        infoPanel.add(txtChucVu);
        infoPanel.add(lblEmail);
        infoPanel.add(txtEmail);
        infoPanel.add(lblSoDienThoai);
        infoPanel.add(txtSoDienThoai);

        // Thêm infoPanel vào JScrollPane
        JScrollPane scrollPane = new JScrollPane(infoPanel);
        scrollPane.setBorder(new EmptyBorder(20, 20, 20, 20)); // Thêm padding vào scrollPane
        scrollPane.setPreferredSize(new Dimension(450, 300)); // Điều chỉnh kích thước scrollPane

        taiKhoanPanel.add(scrollPane, BorderLayout.CENTER);

        // Nút đổi mật khẩu
        JButton btnDoiMatKhau = new JButton("Đổi mật khẩu");
        btnDoiMatKhau.setFont(mainFont);
        btnDoiMatKhau.setBackground(primaryColor);
        btnDoiMatKhau.setForeground(Color.WHITE);
        btnDoiMatKhau.setFocusPainted(false);
        btnDoiMatKhau.setBorder(new EmptyBorder(10, 20, 10, 20));
        btnDoiMatKhau.addActionListener(e -> showDoiMatKhauDialog());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(secondaryColor);
        buttonPanel.add(btnDoiMatKhau);
        taiKhoanPanel.add(buttonPanel, BorderLayout.SOUTH);

        return taiKhoanPanel;
    }

    private void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);
    }

    private void showDoiMatKhauDialog() {
        JDialog dialog = new JDialog(this, "Đổi mật khẩu", true);
        dialog.setLayout(new GridLayout(4, 2, 5, 5));
        dialog.setPreferredSize(new Dimension(300, 200));

        JLabel lblMatKhauCu = new JLabel("Mật khẩu cũ:");
        JPasswordField txtMatKhauCu = new JPasswordField();
        JLabel lblMatKhauMoi = new JLabel("Mật khẩu mới:");
        JPasswordField txtMatKhauMoi = new JPasswordField();
        JLabel lblXacNhanMKM = new JLabel("Xác nhận MK mới:");
        JPasswordField txtXacNhanMKM = new JPasswordField();
        JButton btnXacNhan = new JButton("Xác nhận");

        dialog.add(lblMatKhauCu);
        dialog.add(txtMatKhauCu);
        dialog.add(lblMatKhauMoi);
        dialog.add(txtMatKhauMoi);
        dialog.add(lblXacNhanMKM);
        dialog.add(txtXacNhanMKM);
        dialog.add(new JLabel());
        dialog.add(btnXacNhan);

        btnXacNhan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String matKhauCu = new String(txtMatKhauCu.getPassword());
                String matKhauMoi = new String(txtMatKhauMoi.getPassword());
                String xacNhanMKM = new String(txtXacNhanMKM.getPassword());

                if (!taiKhoanDangNhap.getMatKhau().equals(matKhauCu)) {
                    JOptionPane.showMessageDialog(dialog, "Mật khẩu cũ không đúng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!matKhauMoi.equals(xacNhanMKM)) {
                    JOptionPane.showMessageDialog(dialog, "Mật khẩu mới và xác nhận không khớp!", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (matKhauCu.equals(matKhauMoi)) {
                    JOptionPane.showMessageDialog(dialog, "Mật khẩu mới không được trùng với mật khẩu cũ!", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                TaiKhoanBUS taiKhoanBUS = new TaiKhoanBUS();
                if (taiKhoanBUS.capNhatMatKhau(taiKhoanDangNhap.getTenDangNhap(), matKhauMoi)) {
                    JOptionPane.showMessageDialog(dialog, "Đổi mật khẩu thành công!", "Thành công",
                            JOptionPane.INFORMATION_MESSAGE);
                    taiKhoanDangNhap.setMatKhau(matKhauMoi);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Đã có lỗi xảy ra khi đổi mật khẩu!", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}