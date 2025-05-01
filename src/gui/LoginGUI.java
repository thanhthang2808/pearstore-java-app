package gui;

import bus.TaiKhoanBUS;
import entity.TaiKhoan;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;

public class LoginGUI extends JFrame implements ActionListener {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblMessage;
    private TaiKhoanBUS taiKhoanBUS;

    private Color primaryColor = new Color(52, 152, 219);
    private Color secondaryColor = new Color(236, 240, 241);
    private Color textColor = new Color(44, 62, 80);
    private Font mainFont = new Font("Segoe UI", Font.PLAIN, 14);
    private Font titleFont = new Font("Segoe UI", Font.BOLD, 20);

    public LoginGUI() {
        taiKhoanBUS = new TaiKhoanBUS();
        setTitle("Đăng nhập hệ thống");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 250);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(secondaryColor);
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        add(mainPanel, BorderLayout.CENTER);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblTitle = new JLabel("Đăng nhập");
        lblTitle.setFont(titleFont);
        lblTitle.setForeground(primaryColor);
        titlePanel.setBackground(secondaryColor);
        add(titlePanel, BorderLayout.NORTH);

        // Username Field
        JPanel usernamePanel = new JPanel(new GridLayout(1, 2, 5, 0));
        usernamePanel.setBackground(secondaryColor);
        JLabel lblUsername = new JLabel("Tên đăng nhập:");
        lblUsername.setFont(mainFont);
        lblUsername.setForeground(textColor);
        txtUsername = new JTextField(20);
        txtUsername.setFont(mainFont);
        usernamePanel.add(lblUsername);
        usernamePanel.add(txtUsername);
        mainPanel.add(usernamePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Password Field
        JPanel passwordPanel = new JPanel(new GridLayout(1, 2, 5, 0));
        passwordPanel.setBackground(secondaryColor);
        JLabel lblPassword = new JLabel("Mật khẩu:");
        lblPassword.setFont(mainFont);
        lblPassword.setForeground(textColor);
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(mainFont);
        passwordPanel.add(lblPassword);
        passwordPanel.add(txtPassword);
        mainPanel.add(passwordPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Login Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(secondaryColor);
        btnLogin = new JButton("Đăng nhập");
        btnLogin.setFont(mainFont);
        btnLogin.setBackground(primaryColor);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        buttonPanel.add(btnLogin);
        mainPanel.add(buttonPanel);
        btnLogin.addActionListener(this);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        txtUsername.addActionListener(this);
        txtPassword.addActionListener(this);

        // Message Label
        lblMessage = new JLabel("");
        lblMessage.setForeground(Color.RED);
        lblMessage.setFont(mainFont);
        lblMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblMessage);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin || e.getSource() == txtUsername || e.getSource() == txtPassword) {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            TaiKhoan taiKhoan = taiKhoanBUS.login(username, password);

            if (taiKhoan != null) {
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công!", "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
                new HomePageGUI(taiKhoan);
                dispose();
            } else {
                lblMessage.setText("Tên đăng nhập hoặc mật khẩu không đúng!");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginGUI());
    }
}