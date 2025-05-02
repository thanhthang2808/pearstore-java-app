package gui;

import dao.ChiTietHoaDonDAO;
import dao.HoaDonDAO;
import dao.SanPhamDAO;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.SanPham;
import entity.TaiKhoan;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ThongKeGUI extends JPanel {
    private JComboBox<Integer> cbNgayBatDau, cbThangBatDau, cbNamBatDau;
    private JComboBox<Integer> cbNgayKetThuc, cbThangKetThuc, cbNamKetThuc;
    private JLabel lblNguoiIn;
    private JButton btnTaoBaoCao, btnInBaoCao;
    public JTextArea txtKetQua;
    private TaiKhoan taiKhoanDangNhap;
    private DecimalFormat currencyFormat = new DecimalFormat("#,### VNĐ");

    private Color primaryColor = new Color(52, 152, 219);
    private Color secondaryColor = new Color(236, 240, 241);
    private Color textColor = new Color(44, 62, 80);

    public ThongKeGUI(TaiKhoan taiKhoanDangNhap) {
        if (taiKhoanDangNhap == null) {
            JOptionPane.showMessageDialog(this, "Bạn cần đăng nhập để truy cập chức năng này!", "Cảnh báo",
                    JOptionPane.WARNING_MESSAGE);
            new LoginGUI();
            return;
        }

        this.taiKhoanDangNhap = taiKhoanDangNhap;
        setLayout(new BorderLayout());
        setBackground(secondaryColor);

        // Panel nhập thông tin
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(primaryColor, 2),
                "Nhập thông tin báo cáo"));
        inputPanel.setBackground(secondaryColor);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Thời gian bắt đầu
        JLabel lblThoiGianBatDau = new JLabel("Thời gian bắt đầu:");
        lblThoiGianBatDau.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblThoiGianBatDau.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(lblThoiGianBatDau, gbc);

        JPanel panelBatDau = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelBatDau.setBackground(secondaryColor);
        cbNgayBatDau = createDayComboBox();
        cbThangBatDau = createMonthComboBox();
        cbNamBatDau = createYearComboBox(2020);
        panelBatDau.add(cbNgayBatDau);
        panelBatDau.add(cbThangBatDau);
        panelBatDau.add(cbNamBatDau);
        gbc.gridx = 1;
        inputPanel.add(panelBatDau, gbc);

        // Thời gian kết thúc
        JLabel lblThoiGianKetThuc = new JLabel("Thời gian kết thúc:");
        lblThoiGianKetThuc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblThoiGianKetThuc.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(lblThoiGianKetThuc, gbc);

        JPanel panelKetThuc = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelKetThuc.setBackground(secondaryColor);
        cbNgayKetThuc = createDayComboBox();
        cbThangKetThuc = createMonthComboBox();
        cbNamKetThuc = createYearComboBox(2020);
        panelKetThuc.add(cbNgayKetThuc);
        panelKetThuc.add(cbThangKetThuc);
        panelKetThuc.add(cbNamKetThuc);
        gbc.gridx = 1;
        inputPanel.add(panelKetThuc, gbc);

        // Người in
        JLabel lblNguoiInTitle = new JLabel("Người in:");
        lblNguoiInTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblNguoiInTitle.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(lblNguoiInTitle, gbc);

        lblNguoiIn = new JLabel(taiKhoanDangNhap.getNhanVien().getTenNV());
        lblNguoiIn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNguoiIn.setForeground(primaryColor);
        gbc.gridx = 1;
        inputPanel.add(lblNguoiIn, gbc);

        // Nút tạo Thống kê
        btnTaoBaoCao = new JButton("Xem Thống kê");
        btnTaoBaoCao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnTaoBaoCao.setBackground(primaryColor);
        btnTaoBaoCao.setForeground(Color.WHITE);
        btnTaoBaoCao.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        inputPanel.add(btnTaoBaoCao, gbc);

        // Nút in báo cáo
        btnInBaoCao = new JButton("In báo cáo");
        btnInBaoCao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnInBaoCao.setBackground(primaryColor);
        btnInBaoCao.setForeground(Color.WHITE);
        btnInBaoCao.setFocusPainted(false);
        gbc.gridy = 4;
        inputPanel.add(btnInBaoCao, gbc);

        add(inputPanel, BorderLayout.NORTH);

        // Panel hiển thị kết quả
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(primaryColor, 2), "Kết quả thống kê"));
        resultPanel.setBackground(secondaryColor);

        txtKetQua = new JTextArea();
        txtKetQua.setEditable(false);
        txtKetQua.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtKetQua.setLineWrap(true);
        txtKetQua.setWrapStyleWord(true);
        resultPanel.add(new JScrollPane(txtKetQua), BorderLayout.CENTER);

        add(resultPanel, BorderLayout.CENTER);

        // Xử lý sự kiện
        btnTaoBaoCao.addActionListener(e -> taoBaoCao());
        btnInBaoCao.addActionListener(e -> inBaoCao());
    }

    public void taoBaoCao() {
        try {
            // Lấy thời gian
            String thoiGianBatDau = formatDateFromComboBox(cbNgayBatDau, cbThangBatDau, cbNamBatDau);
            String thoiGianKetThuc = formatDateFromComboBox(cbNgayKetThuc, cbThangKetThuc, cbNamKetThuc);

            if (thoiGianBatDau.isEmpty() || thoiGianKetThuc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ thời gian!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Lấy danh sách hóa đơn theo khoảng thời gian
            HoaDonDAO hoaDonDAO = new HoaDonDAO();
            List<HoaDon> danhSachHoaDon = hoaDonDAO.getHoaDonByTimeRange(
                    java.sql.Timestamp.valueOf(thoiGianBatDau + " 00:00:00"),
                    java.sql.Timestamp.valueOf(thoiGianKetThuc + " 23:59:59"));

            // Tính tổng hóa đơn
            int tongSoHoaDon = danhSachHoaDon.size();

            // Lọc dữ liệu và tính toán
            double tongDoanhThu = 0;
            Map<String, Integer> sanPhamSoLuongBanRa = new HashMap<>();
            Map<String, Double> sanPhamDoanhThu = new HashMap<>();

            ChiTietHoaDonDAO chiTietHoaDonDAO = new ChiTietHoaDonDAO();
            for (HoaDon hoaDon : danhSachHoaDon) {
                List<ChiTietHoaDon> chiTietList = chiTietHoaDonDAO.getByMaHD(hoaDon.getMaHD());
                for (ChiTietHoaDon chiTiet : chiTietList) {
                    sanPhamSoLuongBanRa.merge(chiTiet.getMaSP(), chiTiet.getSoLuong(), Integer::sum);
                    sanPhamDoanhThu.merge(chiTiet.getMaSP(), chiTiet.getThanhTien(), Double::sum);
                }
                tongDoanhThu += hoaDon.getTongTien();
            }

            // Lấy danh sách sản phẩm từ DAO
            SanPhamDAO sanPhamDAO = new SanPhamDAO();
            List<SanPham> danhSachSanPham = sanPhamDAO.getAllSanPham();

            // Thêm sản phẩm chưa được bán vào danh sách
            for (SanPham sp : danhSachSanPham) {
                sanPhamSoLuongBanRa.putIfAbsent(sp.getMaSP(), 0); // Nếu sản phẩm chưa bán, gán số lượng bán = 0
                sanPhamDoanhThu.putIfAbsent(sp.getMaSP(), 0.0); // Nếu sản phẩm chưa bán, gán doanh thu = 0
            }

            // Sắp xếp sản phẩm theo số lượng bán ra
            List<Map.Entry<String, Integer>> sanPhamBanSorted = sanPhamSoLuongBanRa.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue()) // Sắp xếp tăng dần theo số lượng bán
                    .collect(Collectors.toList());

            // Lấy 3 sản phẩm bán chạy nhất
            List<Map.Entry<String, Integer>> sanPhamBanChaySorted = sanPhamSoLuongBanRa.entrySet()
                    .stream()
                    .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // Sắp xếp giảm dần
                    .limit(3)
                    .collect(Collectors.toList());

            // Lấy 3 sản phẩm bán ế nhất
            List<String> maSanPhamBanE = sanPhamBanSorted.stream()
                    .limit(3)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            // Tính Thuế VAT (8%) và Doanh thu sau thuế
            double thueVAT = tongDoanhThu * 0.08; // 8% của tổng doanh thu
            double doanhThuSauThue = tongDoanhThu - thueVAT; // Tổng doanh thu trừ thuế

            // Hiển thị kết quả
            StringBuilder ketQua = new StringBuilder();
            ketQua.append("Thời gian bắt đầu: ").append(thoiGianBatDau).append("\n")
                    .append("Thời gian kết thúc: ").append(thoiGianKetThuc).append("\n\n")
                    .append("Tổng doanh thu: ").append(currencyFormat.format(tongDoanhThu)).append("\n")
                    .append("Thuế VAT (8%): ").append(currencyFormat.format(thueVAT)).append("\n")
                    .append("Doanh thu sau thuế: ").append(currencyFormat.format(doanhThuSauThue)).append("\n")
                    .append("Tổng số hóa đơn: ").append(tongSoHoaDon).append("\n");

            // Thống kê sản phẩm bán chạy
            ketQua.append("\nSản phẩm bán chạy:\n");
            for (Map.Entry<String, Integer> entry : sanPhamBanChaySorted) {
                String maSP = entry.getKey();
                SanPham sp = danhSachSanPham.stream().filter(s -> s.getMaSP().equals(maSP)).findFirst().orElse(null);
                if (sp != null) {
                    int soLuongBan = sanPhamSoLuongBanRa.get(maSP);
                    double doanhThu = sanPhamDoanhThu.get(maSP);
                    ketQua.append("- ").append(sp.getTenSP())
                            .append(": Đã bán ").append(soLuongBan)
                            .append(", Doanh thu ").append(currencyFormat.format(doanhThu))
                            .append(", Số lượng còn lại: ").append(sp.getSoLuong()).append("\n");
                }
            }

            // Thống kê sản phẩm bán chậm
            ketQua.append("\nSản phẩm bán chậm:\n");
            for (String maSP : maSanPhamBanE) {
                SanPham sp = danhSachSanPham.stream().filter(s -> s.getMaSP().equals(maSP)).findFirst().orElse(null);
                if (sp != null) {
                    int soLuongBan = sanPhamSoLuongBanRa.get(maSP);
                    double doanhThu = sanPhamDoanhThu.get(maSP);
                    ketQua.append("- ").append(sp.getTenSP())
                            .append(": Đã bán ").append(soLuongBan)
                            .append(", Doanh thu ").append(currencyFormat.format(doanhThu))
                            .append(", Số lượng còn lại: ").append(sp.getSoLuong()).append("\n");
                }
            }

            txtKetQua.setText(ketQua.toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi tạo báo cáo: " + ex.getMessage(), "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void inBaoCao() {
        System.out.println("Phương thức inBaoCao được gọi");

        try {
            // Chuẩn bị dữ liệu báo cáo
            StringBuilder baoCao = new StringBuilder();

            // Lấy thông tin thời gian và nhân viên
            String thoiGianBatDau = formatDateFromComboBox(cbNgayBatDau, cbThangBatDau, cbNamBatDau);
            String thoiGianKetThuc = formatDateFromComboBox(cbNgayKetThuc, cbThangKetThuc, cbNamKetThuc);
            String tenNhanVien = lblNguoiIn.getText();

            // Xây dựng nội dung báo cáo
            baoCao.append("============================ BÁO CÁO THỐNG KÊ =============================\n");
            baoCao.append("                              Pear Store\n");
            baoCao.append("                  12 Nguyễn Văn Bảo, Q.Gò Vấp, Hồ Chí Minh\n");
            baoCao.append("Nhân viên: ").append(tenNhanVien).append("\n");
            baoCao.append("Ngày in: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()))
                    .append("\n");
            baoCao.append("---------------------------------------------------------------------------\n");
            baoCao.append("                             THỜI GIAN THỐNG KÊ\n");
            baoCao.append("Thời gian bắt đầu: ").append(thoiGianBatDau).append("\n");
            baoCao.append("Thời gian kết thúc: ").append(thoiGianKetThuc).append("\n");
            baoCao.append("---------------------------------------------------------------------------\n");
            baoCao.append("                             CHI TIẾT THỐNG KÊ\n");
            // Lấy nội dung từ txtKetQua và loại bỏ dòng thời gian
            String[] lines = txtKetQua.getText().split("\n");
            StringBuilder filteredContent = new StringBuilder();
            for (String line : lines) {
                // Bỏ qua các dòng chứa "Thời gian bắt đầu" hoặc "Thời gian kết thúc"
                if (line.startsWith("Thời gian bắt đầu:") || line.startsWith("Thời gian kết thúc:")) {
                    continue;
                }
                filteredContent.append(line).append("\n");
            }

            // Thêm nội dung đã lọc vào báo cáo
            baoCao.append(filteredContent.toString()).append("\n");
            baoCao.append("                            Được cung cấp bởi N21 \n");
            // Hiển thị báo cáo trong cửa sổ popup
            JTextArea txtArea = new JTextArea(baoCao.toString());
            txtArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
            txtArea.setEditable(false);

            JScrollPane scrollPane = new JScrollPane(txtArea);
            scrollPane.setPreferredSize(new Dimension(630, 550));

            JOptionPane.showMessageDialog(this, scrollPane, "Báo cáo thống kê", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi xuất báo cáo: " + ex.getMessage(), "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private JComboBox<Integer> createDayComboBox() {
        JComboBox<Integer> cb = new JComboBox<>();
        for (int i = 1; i <= 31; i++) {
            cb.addItem(i);
        }
        return cb;
    }

    private JComboBox<Integer> createMonthComboBox() {
        JComboBox<Integer> cb = new JComboBox<>();
        for (int i = 1; i <= 12; i++) {
            cb.addItem(i);
        }
        return cb;
    }

    private JComboBox<Integer> createYearComboBox(int startYear) {
        JComboBox<Integer> cb = new JComboBox<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = startYear; i <= currentYear; i++) {
            cb.addItem(i);
        }
        return cb;
    }

    private String formatDateFromComboBox(JComboBox<Integer> day, JComboBox<Integer> month, JComboBox<Integer> year) {
        return String.format("%04d-%02d-%02d", year.getSelectedItem(), month.getSelectedItem(), day.getSelectedItem());
    }
}