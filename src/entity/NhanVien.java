package entity;

public class NhanVien {
    private int maNV;
    private String tenNV;
    private String soDienThoai;
    private String diaChi;
    private TaiKhoan taiKhoan;

    public NhanVien() {
    }

    public NhanVien(int maNV, String tenNV, String soDienThoai, String diaChi, TaiKhoan taiKhoan) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.taiKhoan = taiKhoan;
    }

    public int getMaNV() {
        return maNV;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public TaiKhoan getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    @Override
    public String toString() {
        return "NhanVien{" +
                "maNV=" + maNV +
                ", tenNV='" + tenNV + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", diaChi='" + diaChi + '\'' +
                ", taiKhoan=" + taiKhoan +
                '}';
    }
}