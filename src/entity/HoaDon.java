package entity;

import java.util.Date;

public class HoaDon {
    private int maHD;
    private Date ngayLap;
    private String maNV;
    private String tenNhanVien;
    private double tongTien;

    public HoaDon() {
    }

    public HoaDon(Date ngayLap, String maNV, String tenNhanVien, double tongTien) {
        this.ngayLap = ngayLap;
        this.maNV = maNV;
        this.tenNhanVien = tenNhanVien;
        this.tongTien = tongTien;
    }

    // Getter – Setter
    public int getMaHD() {
        return maHD;
    }

    public void setMaHD(int maHD) {
        this.maHD = maHD;
    }

    public Date getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(Date ngayLap) {
        this.ngayLap = ngayLap;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }
}
