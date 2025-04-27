package gui;

import dao.HoaDonDAO;
import entity.HoaDon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BaoCaoDoanhThuGUI extends JPanel {

    private JSpinner spnThoiGianBatDau;
    private JSpinner spnThoiGianKetThuc;
    private JLabel lblDoanhThuDuKien;
    private JTextField txtDoanhThuThucTe;
    private JLabel lblChenhLech;
    private JTextField txtLyDo;
    private HoaDonDAO hoaDonDAO;
    private String tenNhanVien; // Biến lưu trữ tên nhân viên đang đăng nhập

    // Constructor nhận tên nhân viên
    public BaoCaoDoanhThuGUI(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien; // Gán tên nhân viên đăng nhập
        hoaDonDAO = new HoaDonDAO(); // Khởi tạo DAO

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // Tiêu đề
        JLabel lblTitle = new JLabel("Báo cáo doanh thu", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(33, 37, 41));
        add(lblTitle, BorderLayout.NORTH);

        // Form nhập liệu
        JPanel inputPanel = new JPanel(new GridBagLayout());

        TitledBorder titledBorder = new TitledBorder("Thông tin thống kê");

        titledBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 18));
        titledBorder.setTitleColor(new Color(33, 37, 41));

        inputPanel.setBorder(titledBorder);
        inputPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

       // Thời gian bắt đầu
        JLabel lblThoiGianBatDau = new JLabel("Thời gian bắt đầu:");
        lblThoiGianBatDau.setFont(new Font("Segoe UI", Font.PLAIN, 25));
        spnThoiGianBatDau = createDateTimePicker();
        spnThoiGianBatDau.setPreferredSize(new Dimension(280, 45)); 
        JSpinner.DateEditor editorBatDau = (JSpinner.DateEditor) spnThoiGianBatDau.getEditor();
        editorBatDau.getTextField().setFont(new Font("Segoe UI", Font.PLAIN, 22)); // Tăng kích thước chữ ngày
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(lblThoiGianBatDau, gbc);
        gbc.gridx = 1;
        inputPanel.add(spnThoiGianBatDau, gbc);

        // Thời gian kết thúc
        JLabel lblThoiGianKetThuc = new JLabel("Thời gian kết thúc:");
        lblThoiGianKetThuc.setFont(new Font("Segoe UI", Font.PLAIN, 25));
        spnThoiGianKetThuc = createDateTimePicker();
        spnThoiGianKetThuc.setPreferredSize(new Dimension(280, 45));
        JSpinner.DateEditor editorKetThuc = (JSpinner.DateEditor) spnThoiGianKetThuc.getEditor();
        editorKetThuc.getTextField().setFont(new Font("Segoe UI", Font.PLAIN, 22));
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(lblThoiGianKetThuc, gbc);
        gbc.gridx = 1;
        inputPanel.add(spnThoiGianKetThuc, gbc);

        // Doanh thu dự kiến
        JLabel lblDoanhThuDuKienText = new JLabel("Doanh thu dự kiến:");
        lblDoanhThuDuKienText.setFont(new Font("Segoe UI", Font.PLAIN, 25)); 
        lblDoanhThuDuKien = new JLabel("0 VND");
        lblDoanhThuDuKien.setFont(new Font("Segoe UI", Font.BOLD, 25)); 
        lblDoanhThuDuKien.setForeground(Color.BLUE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(lblDoanhThuDuKienText, gbc);
        gbc.gridx = 1;
        inputPanel.add(lblDoanhThuDuKien, gbc);

        // Doanh thu thực tế
        JLabel lblDoanhThuThucTe = new JLabel("Doanh thu thực tế:");
        lblDoanhThuThucTe.setFont(new Font("Segoe UI", Font.PLAIN, 25)); 
        txtDoanhThuThucTe = new JTextField();
        txtDoanhThuThucTe.setPreferredSize(new Dimension(280, 45));
        txtDoanhThuThucTe.setFont(new Font("Segoe UI", Font.PLAIN, 25)); 
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(lblDoanhThuThucTe, gbc);
        gbc.gridx = 1;
        inputPanel.add(txtDoanhThuThucTe, gbc);

        // Chênh lệch
        JLabel lblChenhLechText = new JLabel("Chênh lệch:");
        lblChenhLechText.setFont(new Font("Segoe UI", Font.PLAIN, 25)); 
        lblChenhLech = new JLabel("0 VND");
        lblChenhLech.setFont(new Font("Segoe UI", Font.BOLD, 25)); 
        lblChenhLech.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(lblChenhLechText, gbc);
        gbc.gridx = 1;
        inputPanel.add(lblChenhLech, gbc);

        // Lý do
        JLabel lblLyDo = new JLabel("Lý do (nếu có):");
        lblLyDo.setFont(new Font("Segoe UI", Font.PLAIN, 25)); 
        txtLyDo = new JTextField();
        txtLyDo.setPreferredSize(new Dimension(280, 45)); 
        txtLyDo.setFont(new Font("Segoe UI", Font.PLAIN, 25)); 
        gbc.gridx = 0;
        gbc.gridy = 5;
        inputPanel.add(lblLyDo, gbc);
        gbc.gridx = 1;
        inputPanel.add(txtLyDo, gbc);

add(inputPanel, BorderLayout.CENTER);

        // Lắng nghe thay đổi ngày
        addDateChangeListener();

        // Nút chức năng: Tính toán và In báo cáo
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton btnTinhToan = new JButton("Tính toán");
        btnTinhToan.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        btnTinhToan.setBackground(new Color(100 , 149, 237));
        btnTinhToan.setForeground(Color.WHITE);

        JButton btnInBaoCao = new JButton("In báo cáo");
        btnInBaoCao.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        btnInBaoCao.setBackground(new Color(100 , 149, 237));
        btnInBaoCao.setForeground(Color.WHITE);

        buttonPanel.add(btnTinhToan);
        buttonPanel.add(btnInBaoCao);
        add(buttonPanel, BorderLayout.SOUTH);

        // Sự kiện nút "Tính toán"
        btnTinhToan.addActionListener(e -> {
            System.out.println("Nút Tính toán được nhấn");
            tinhToanChenhLech(); // Gọi phương thức tính toán chênh lệch
        });

        // Sự kiện nút "In báo cáo"
        btnInBaoCao.addActionListener(e -> inBaoCao());
    }

    private JSpinner createDateTimePicker() {
        SpinnerDateModel model = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.MINUTE);
        JSpinner spinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "dd/MM/yyyy HH:mm");
        spinner.setEditor(editor);
        return spinner;
    }

    private void addDateChangeListener() {
        ChangeListener listener = e -> tinhToanDoanhThu(); // Gọi hàm tính toán doanh thu khi ngày thay đổi
        spnThoiGianBatDau.addChangeListener(listener); // Lắng nghe sự kiện thay đổi thời gian bắt đầu
        spnThoiGianKetThuc.addChangeListener(listener); // Lắng nghe sự kiện thay đổi thời gian kết thúc
    }

    private void tinhToanDoanhThu() {
        try {
            // Lấy giá trị từ các JSpinner
            java.util.Date thoiGianBatDauUtil = ((SpinnerDateModel) spnThoiGianBatDau.getModel()).getDate();
            java.util.Date thoiGianKetThucUtil = ((SpinnerDateModel) spnThoiGianKetThuc.getModel()).getDate();

            // Chuyển đổi java.util.Date sang java.sql.Timestamp
            java.sql.Timestamp thoiGianBatDau = new java.sql.Timestamp(thoiGianBatDauUtil.getTime());
            java.sql.Timestamp thoiGianKetThuc = new java.sql.Timestamp(thoiGianKetThucUtil.getTime());

            // Truy vấn danh sách hóa đơn từ cơ sở dữ liệu
            List<HoaDon> hoaDonList = hoaDonDAO.getHoaDonByTimeRange(thoiGianBatDau, thoiGianKetThuc);

            // Tính tổng doanh thu dự kiến
            double doanhThuDuKienValue = hoaDonList.stream()
                                                   .mapToDouble(HoaDon::getTongTien)
                                                   .sum();

            // Cập nhật doanh thu dự kiến trên giao diện
            lblDoanhThuDuKien.setText(formatCurrency(doanhThuDuKienValue) + " VND");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Đã xảy ra lỗi khi tính toán doanh thu: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tinhToanChenhLech() {
        System.out.println("Phương thức tinhToanChenhLech được gọi");
        try {
            // Lấy giá trị từ ô nhập doanh thu thực tế
            String doanhThuThucTeText = txtDoanhThuThucTe.getText().trim();
            System.out.println("Doanh thu thực tế (text): " + doanhThuThucTeText);

            // Kiểm tra và chuyển đổi doanh thu thực tế thành số nguyên
            int doanhThuThucTeValue;
            try {
                // Chỉ giữ lại ký tự số
                doanhThuThucTeText = doanhThuThucTeText.replaceAll("[^\\d]", "");
                doanhThuThucTeValue = doanhThuThucTeText.isEmpty() ? 0 : Integer.parseInt(doanhThuThucTeText);
            } catch (NumberFormatException ex) {
                // Hiển thị thông báo lỗi nếu giá trị không hợp lệ
                JOptionPane.showMessageDialog(this, "Vui lòng nhập một số nguyên hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return; // Kết thúc phương thức nếu giá trị không hợp lệ
            }
            System.out.println("Doanh thu thực tế (value): " + doanhThuThucTeValue);

            // Chuẩn hóa giá trị doanh thu dự kiến
            String doanhThuDuKienText = lblDoanhThuDuKien.getText().replaceAll("[^\\d]", "");
            System.out.println("Doanh thu dự kiến (text đã chuẩn hóa): " + doanhThuDuKienText);

            int doanhThuDuKienValue;
            try {
                doanhThuDuKienValue = Integer.parseInt(doanhThuDuKienText);
            } catch (NumberFormatException ex) {
                // Hiển thị thông báo lỗi nếu giá trị không thể chuyển đổi
                JOptionPane.showMessageDialog(this, "Có lỗi khi xử lý doanh thu dự kiến. Vui lòng kiểm tra định dạng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return; // Kết thúc phương thức nếu giá trị không hợp lệ
            }
            System.out.println("Doanh thu dự kiến (value): " + doanhThuDuKienValue);

            // Tính toán chênh lệch
            int chenhLech = doanhThuThucTeValue - doanhThuDuKienValue;
            System.out.println("Chênh lệch: " + chenhLech);

            // Cập nhật giao diện
            lblChenhLech.setText(formatCurrency(chenhLech) + " VND");
            lblChenhLech.setForeground(chenhLech < 0 ? Color.RED : Color.GREEN);
            txtLyDo.setEnabled(chenhLech < 0); // Bật ô nhập lý do nếu chênh lệch âm
        } catch (Exception ex) {
            // Xử lý lỗi không mong muốn
            lblChenhLech.setText("0 VND");
            lblChenhLech.setForeground(Color.BLACK);
            ex.printStackTrace();
        }
    }

    private void inBaoCao() {
        System.out.println("Phương thức inBaoCao được gọi");

        try {
            // Chuẩn bị dữ liệu báo cáo
            StringBuilder baoCao = new StringBuilder();
            String thoiGianBatDau = ((JSpinner.DateEditor) spnThoiGianBatDau.getEditor()).getFormat().format(spnThoiGianBatDau.getValue());
            String thoiGianKetThuc = ((JSpinner.DateEditor) spnThoiGianKetThuc.getEditor()).getFormat().format(spnThoiGianKetThuc.getValue());
            String doanhThuDuKien = lblDoanhThuDuKien.getText();
            String doanhThuThucTe = txtDoanhThuThucTe.getText();
            String chenhLech = lblChenhLech.getText();
            String lyDo = txtLyDo.getText().trim();

            // Tên nhân viên đang đăng nhập
            String tenNhanVien = this.tenNhanVien;

            // Xây dựng nội dung báo cáo
            baoCao.append("=================== BÁO CÁO DOANH THU ===================\n");
            baoCao.append("Nhân viên: ").append(tenNhanVien).append("\n");
            baoCao.append("Ngày: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())).append("\n");
            baoCao.append("---------------------------------------------------------\n");
            baoCao.append("Thời gian bắt đầu: ").append(thoiGianBatDau).append("\n");
            baoCao.append("Thời gian kết thúc: ").append(thoiGianKetThuc).append("\n");
            baoCao.append("Doanh thu dự kiến: ").append(doanhThuDuKien).append("\n");
            baoCao.append("Doanh thu thực tế: ").append(doanhThuThucTe).append("\n");
            baoCao.append("Chênh lệch: ").append(chenhLech).append("\n");
            if (!lyDo.isEmpty()) {
                baoCao.append("Lý do: ").append(lyDo).append("\n");
            }
            baoCao.append("=========================================================");

            // Hiển thị báo cáo trong cửa sổ popup
            JTextArea txtArea = new JTextArea(baoCao.toString());
            txtArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
            txtArea.setEditable(false);

            JScrollPane scrollPane = new JScrollPane(txtArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));

            JOptionPane.showMessageDialog(this, scrollPane, "Báo cáo doanh thu", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi xuất báo cáo: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(amount);
    }
}