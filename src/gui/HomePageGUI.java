package gui;

import bus.TaiKhoanBUS;
import entity.TaiKhoan;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePageGUI extends JFrame {

    private Color primaryColor = new Color(52, 152, 219);
    private Color secondaryColor = new Color(236, 240, 241);
    private Color textColor = new Color(44, 62, 80);
    private Font mainFont = new Font("Segoe UI", Font.PLAIN, 14);
    private Font titleFont = new Font("Segoe UI", Font.BOLD, 24);
    private TaiKhoan taiKhoanDangNhap; // Lưu trữ thông tin tài khoản đã đăng nhập

    public HomePageGUI(TaiKhoan taiKhoan) {
        this.taiKhoanDangNhap = taiKhoan;
        setTitle("Trang chủ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(primaryColor);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        JLabel lblTitle = new JLabel("Hệ thống Bán hàng");
        lblTitle.setFont(titleFont);
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(secondaryColor);
        JLabel lblWelcome = new JLabel("Xin chào " + taiKhoanDangNhap.getTenDangNhap() + "!");
        lblWelcome.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        lblWelcome.setForeground(textColor);
        lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(lblWelcome, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu menuSanPham = new JMenu("Sản phẩm");
        JMenu menuBanHang = new JMenu("Bán hàng");
        JMenu menuThongKe = new JMenu("Thống kê");
        JMenu menuNhanVien = new JMenu("Nhân viên");
        JMenu menuTaiKhoan = new JMenu("Tài khoản");

        JMenuItem menuItemDoiMatKhau = new JMenuItem("Đổi mật khẩu");
        menuItemDoiMatKhau.addActionListener(e -> showDoiMatKhauDialog());
        menuTaiKhoan.add(menuItemDoiMatKhau);

        JMenuItem menuItemLogout = new JMenuItem("Đăng xuất");
        menuItemLogout.addActionListener(e -> {
            new LoginGUI();
            dispose();
        });
        menuTaiKhoan.add(menuItemLogout);

        menuBar.add(menuSanPham);
        menuBar.add(menuBanHang);
        menuBar.add(menuThongKe);
        menuBar.add(menuNhanVien);
        menuBar.add(menuTaiKhoan);
        setJMenuBar(menuBar);

        setVisible(true);
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
        dialog.add(new JLabel()); // Empty label for spacing
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
                    taiKhoanDangNhap.setMatKhau(matKhauMoi); // Cập nhật mật khẩu trong đối tượng hiện tại
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