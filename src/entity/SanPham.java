package entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class SanPham implements Serializable {
    private String maSP;
    private String tenSP;
    private BigDecimal giaBan;
    private int soLuong;
    private String maVach;
    private String hinhAnh;
    private BigDecimal giaNhap;
    private String donViTinh;
    private NganhHang nganhHang;

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public BigDecimal getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(BigDecimal giaBan) {
        this.giaBan = giaBan;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getMaVach() {
        return maVach;
    }

    public void setMaVach(String maVach) {
        this.maVach = maVach;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public BigDecimal getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(BigDecimal giaNhap) {
        this.giaNhap = giaNhap;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public NganhHang getNganhHang() {
        return nganhHang;
    }

    public void setNganhHang(NganhHang nganhHang) {
        this.nganhHang = nganhHang;
    }

    public SanPham(String maSP, String tenSP, BigDecimal giaBan, int soLuong, String maVach, String hinhAnh,
            BigDecimal giaNhap,
            String donViTinh, NganhHang nganhHang) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.giaBan = giaBan;
        this.soLuong = soLuong;
        this.maVach = maVach;
        this.hinhAnh = hinhAnh;
        this.giaNhap = giaNhap;
        this.donViTinh = donViTinh;
        this.nganhHang = nganhHang;
    }

    @Override
    public String toString() {
        return "SanPham [maSP=" + maSP + ", tenSP=" + tenSP + ", giaBan=" + giaBan + ", soLuong=" + soLuong
                + ", maVach=" + maVach + ", hinhAnh=" + hinhAnh + ", giaNhap=" + giaNhap + ", donViTinh=" + donViTinh
                + ", nganhHang=" + nganhHang + "]";
    }
}