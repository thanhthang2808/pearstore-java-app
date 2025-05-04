package gui;

import dao.ChiTietHoaDonDAO;
import dao.HoaDonDAO;
import dao.SanPhamDAO;
import entity.ChiTietHoaDon;
import entity.ChucVu;
import entity.HoaDon;
import entity.SanPham;
import entity.TaiKhoan;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardPanel extends JPanel {
    private JLabel lblRevenueToday, lblOrdersToday;
    private DecimalFormat currencyFormat = new DecimalFormat("#,### VNĐ");
    private HoaDonDAO hoaDonDAO;
    private ChiTietHoaDonDAO chiTietHoaDonDAO;
    private SanPhamDAO sanPhamDAO;
    private TaiKhoan taiKhoanDangNhap;

    public DashboardPanel(TaiKhoan taiKhoan) {
        this.taiKhoanDangNhap = taiKhoan;
        // Khởi tạo các DAO
        hoaDonDAO = new HoaDonDAO();
        chiTietHoaDonDAO = new ChiTietHoaDonDAO();
        sanPhamDAO = new SanPhamDAO();

        setLayout(new BorderLayout());
        setBackground(new Color(236, 240, 241)); // Màu nền

        // Chia giao diện thành 2 phần
        JPanel leftPanel = createChartPanel(); // Biểu đồ doanh thu
        JPanel rightPanel = createInfoPanel(); // Thông tin doanh thu trong ngày

        // Thêm các phần vào giao diện
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        // Cập nhật dữ liệu
        updateDashboardData();
    }

    private JPanel createChartPanel() {
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setPreferredSize(new Dimension(600, getHeight())); // Giảm kích thước đồ thị
        chartPanel.setBorder(
                new TitledBorder(BorderFactory.createLineBorder(new Color(52, 152, 219), 2), "Biểu đồ doanh thu"));
        chartPanel.setBackground(Color.WHITE);

        // Tạo biểu đồ doanh thu
        JFreeChart barChart = createRevenueChart("Doanh thu theo tuần");
        ChartPanel chart = new ChartPanel(barChart);
        chart.setPreferredSize(new Dimension(600, 300));

        // Thêm biểu đồ vào panel
        chartPanel.add(chart, BorderLayout.CENTER);

        return chartPanel;
    }

    private JFreeChart createRevenueChart(String title) {
        // Lấy dữ liệu doanh thu theo ngày
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            LocalDate today = LocalDate.now();
            LocalDate startOfWeek = today.minusDays(6);
            DateTimeFormatter sqlFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Định dạng cho SQL
            DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd/MM"); // Định dạng để hiển thị

            // Lặp qua 7 ngày gần nhất
            for (int i = 0; i < 7; i++) {
                LocalDate date = startOfWeek.plusDays(i);
                String sqlDate = date.format(sqlFormatter); // Định dạng để truy vấn SQL
                String displayDate = date.format(displayFormatter); // Định dạng để hiển thị

                // Lấy tổng doanh thu của ngày
                double revenue = hoaDonDAO.tinhTongDoanhThu(sqlDate + " 00:00:00", sqlDate + " 23:59:59");

                // Thêm dữ liệu vào dataset
                dataset.addValue(revenue, "Doanh thu", displayDate); // Hiển thị ngày-tháng
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Tạo biểu đồ
        return ChartFactory.createBarChart(
                title, // Tiêu đề
                "Ngày", // X
                "Doanh thu (VNĐ)", // Y
                dataset);
    }

    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        infoPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(new Color(52, 152, 219), 2), "Tổng quan"));
        infoPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Tiêu đề Tổng quan
        JLabel overviewTitle = new JLabel("TỔNG QUAN");
        overviewTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        overviewTitle.setForeground(new Color(52, 152, 219));
        overviewTitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0; // Đặt tiêu đề ở trên cùng
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Chiếm toàn bộ chiều ngang
        gbc.anchor = GridBagConstraints.CENTER; // Căn giữa
        infoPanel.add(overviewTitle, gbc);

        // Reset lại GridBagConstraints sau khi thêm tiêu đề
        gbc.gridwidth = 1; // Reset chiều rộng
        gbc.anchor = GridBagConstraints.WEST; // Căn trái cho các thành phần khác

        // Doanh thu trong ngày
        JLabel revenueLabel = new JLabel("Doanh thu hôm nay:");
        revenueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridy = 1;
        infoPanel.add(revenueLabel, gbc);

        lblRevenueToday = new JLabel("Đang tải...");
        lblRevenueToday.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblRevenueToday.setForeground(new Color(39, 174, 96));
        gbc.gridy = 2;
        infoPanel.add(lblRevenueToday, gbc);

        // Số đơn hàng trong ngày
        JLabel ordersLabel = new JLabel("Số đơn hàng hôm nay:");
        ordersLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridy = 3;
        infoPanel.add(ordersLabel, gbc);

        lblOrdersToday = new JLabel("Đang tải...");
        lblOrdersToday.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblOrdersToday.setForeground(new Color(41, 128, 185));
        gbc.gridy = 4;
        infoPanel.add(lblOrdersToday, gbc);

        // Thống kê sản phẩm bán chạy trong tuần
        JLabel topProductsLabel = new JLabel("Sản phẩm bán chạy trong tuần:");
        topProductsLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridy = 5;
        infoPanel.add(topProductsLabel, gbc);

        JTextArea txtTopProducts = new JTextArea(5, 20);
        txtTopProducts.setEditable(false);
        txtTopProducts.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTopProducts.setLineWrap(true);
        txtTopProducts.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(txtTopProducts);
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.BOTH;
        infoPanel.add(scrollPane, gbc);

        // Thêm phần trang trí
        gbc.gridy = 7; // Xuống dòng mới
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        infoPanel.add(createDecorationPanel(), gbc);

        // Cập nhật danh sách sản phẩm bán chạy trong tuần
        updateTopProductsThisWeek(txtTopProducts);

        return infoPanel;
    }

    private JPanel createDecorationPanel() {
        JPanel decorationPanel = new JPanel();
        decorationPanel.setLayout(new BoxLayout(decorationPanel, BoxLayout.Y_AXIS));
        decorationPanel.setBackground(Color.WHITE);

        Dimension boxSize = new Dimension(300, 50); // Kích thước cố định cho tất cả các hộp

        // Hotline Panel
        JPanel hotlinePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        hotlinePanel.setBackground(new Color(240, 240, 240)); // Màu nền hộp
        hotlinePanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1)); // Đường viền
        hotlinePanel.setPreferredSize(boxSize);
        hotlinePanel.setMaximumSize(boxSize);

        // Hotline Icon and Label
        JLabel phoneIconLabel = new JLabel(new ImageIcon(
                new ImageIcon(getClass().getResource("/icons/phone.png"))
                        .getImage()
                        .getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        JLabel lblHotline = new JLabel("HOTLINE: 1900.636.680");
        lblHotline.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblHotline.setForeground(new Color(192, 57, 43)); // Màu đỏ

        hotlinePanel.add(phoneIconLabel);
        hotlinePanel.add(lblHotline);
        decorationPanel.add(hotlinePanel);

        // Khoảng cách
        decorationPanel.add(Box.createVerticalStrut(10));

        // Nút "Thống kê chi tiết"
        JButton btnThongKeChiTiet = new JButton("Thống kê chi tiết");
        btnThongKeChiTiet.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnThongKeChiTiet.setBackground(new Color(52, 152, 219)); // Màu xanh
        btnThongKeChiTiet.setForeground(Color.WHITE); // Màu chữ
        btnThongKeChiTiet.setFocusPainted(false);
        btnThongKeChiTiet.setAlignmentX(Component.CENTER_ALIGNMENT); // Căn giữa trong BoxLayout
        btnThongKeChiTiet.setPreferredSize(new Dimension(200, 40)); // Kích thước tùy chỉnh

        // Xử lý sự kiện khi nhấn nút
        btnThongKeChiTiet.addActionListener(e -> navigateToThongKeGUI());

        if (taiKhoanDangNhap.getNhanVien().getChucVu() == ChucVu.QUAN_LY) {
            decorationPanel.add(btnThongKeChiTiet);
        }

        return decorationPanel;
    }

    private void navigateToThongKeGUI() {
        // Tìm cha của panel hiện tại
        Container parent = this.getParent();
        while (parent != null && !(parent instanceof JFrame || parent instanceof JPanel)) {
            parent = parent.getParent();
        }

        if (parent instanceof JPanel) {
            // Nếu cha là JPanel và sử dụng CardLayout, chuyển sang panel ThongKeGUI
            CardLayout layout = (CardLayout) parent.getLayout();
            layout.show(parent, "Thống kê"); // Tên của JPanel ThongKeGUI
        } else if (parent instanceof JFrame) {
            // Nếu cha là JFrame, thay đổi nội dung JFrame
            JFrame frame = (JFrame) parent;
            frame.getContentPane().removeAll(); // Xóa giao diện hiện tại
            frame.add(new ThongKeGUI(new TaiKhoan())); // Thêm giao diện ThongKeGUI
            frame.revalidate();
            frame.repaint();
        } else {
            System.err.println("Không thể điều hướng đến ThongKeGUI: Không tìm thấy cha phù hợp.");
        }
    }

    private void updateTopProductsThisWeek(JTextArea txtTopProducts) {
        try {
            // Lấy ngày hiện tại
            LocalDate today = LocalDate.now();

            // Tính ngày bắt đầu và kết thúc của tuần hiện tại
            LocalDate startOfWeek = today.with(java.time.DayOfWeek.MONDAY);
            LocalDate endOfWeek = today.with(java.time.DayOfWeek.SUNDAY);

            // Định dạng ngày cho SQL
            String startOfWeekFormatted = startOfWeek.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String endOfWeekFormatted = endOfWeek.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            // Lấy danh sách hóa đơn trong tuần
            List<HoaDon> danhSachHoaDon = hoaDonDAO.getHoaDonByTimeRange(
                    java.sql.Timestamp.valueOf(startOfWeekFormatted + " 00:00:00"),
                    java.sql.Timestamp.valueOf(endOfWeekFormatted + " 23:59:59"));

            // Tính số lượng bán ra của từng sản phẩm (String là mã SP)
            Map<String, Integer> sanPhamSoLuongBanRa = new HashMap<>();
            for (HoaDon hoaDon : danhSachHoaDon) {
                List<ChiTietHoaDon> chiTietList = chiTietHoaDonDAO.getByMaHD(hoaDon.getMaHD());
                for (ChiTietHoaDon chiTiet : chiTietList) {
                    sanPhamSoLuongBanRa.merge(chiTiet.getMaSP(), chiTiet.getSoLuong(), Integer::sum);
                }
            }

            // Lấy danh sách sản phẩm từ DAO
            List<SanPham> danhSachSanPham = sanPhamDAO.getAllSanPham();

            // Sắp xếp sản phẩm theo số lượng bán ra (giảm dần)
            List<Map.Entry<String, Integer>> sanPhamBanChaySorted = sanPhamSoLuongBanRa.entrySet()
                    .stream()
                    .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // Sắp xếp giảm dần
                    .limit(5) // Lấy top 5 sản phẩm bán chạy
                    .collect(Collectors.toList());

            // Hiển thị kết quả
            StringBuilder ketQua = new StringBuilder();
            ketQua.append("Top sản phẩm bán chạy trong tuần:\n");

            for (Map.Entry<String, Integer> entry : sanPhamBanChaySorted) {
                String maSP = entry.getKey();
                SanPham sp = danhSachSanPham.stream()
                        .filter(s -> s.getMaSP().equals(maSP))
                        .findFirst()
                        .orElse(null);
                if (sp != null) {
                    int soLuongBan = entry.getValue();
                    ketQua.append("- ").append(sp.getTenSP())
                            .append(": ").append(soLuongBan).append(" sản phẩm\n");
                }
            }

            txtTopProducts.setText(ketQua.toString());
        } catch (Exception e) {
            e.printStackTrace();
            txtTopProducts.setText("Đã xảy ra lỗi khi tải dữ liệu sản phẩm bán chạy trong tuần.");
        }
    }

    public void updateChartData() {
        JFreeChart barChart = createRevenueChart("Doanh thu theo tuần");
        ChartPanel chart = new ChartPanel(barChart);
        chart.setPreferredSize(new Dimension(600, 300));
        JPanel chartPanel = (JPanel) getComponent(0);
        chartPanel.removeAll();
        chartPanel.add(chart, BorderLayout.CENTER);
        chartPanel.revalidate();
        chartPanel.repaint();
    }

    public void updateDashboardData() {
        try {
            System.out.println("Cập nhật DashboardPanel");

            LocalDate today = LocalDate.now();
            String todayFormatted = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            double dailyRevenue = hoaDonDAO.tinhTongDoanhThu(todayFormatted + " 00:00:00",
                    todayFormatted + " 23:59:59");
            System.out.println("Doanh thu hôm nay: " + dailyRevenue);

            int dailyOrders = hoaDonDAO.demSoHoaDon(todayFormatted + " 00:00:00", todayFormatted + " 23:59:59");
            System.out.println("Số hóa đơn hôm nay: " + dailyOrders);

            lblRevenueToday.setText(currencyFormat.format(dailyRevenue));
            lblOrdersToday.setText(dailyOrders + " đơn");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refresh() {
        updateDashboardData();
        updateChartData();
        updateTopProductsThisWeek(
                (JTextArea) ((JScrollPane) ((JPanel) getComponent(1)).getComponent(6)).getViewport().getView());
    }
}