package entity;

public class TaiKhoan {
    private String tenDangNhap;
    private String matKhau;
    private int maNV;

    public TaiKhoan() {
    }

    public TaiKhoan(String tenDangNhap, String matKhau, int maNV) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.maNV = maNV;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public int getMaNV() {
        return maNV;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }

    @Override
    public String toString() {
        return "TaiKhoan{" +
                "tenDangNhap='" + tenDangNhap + '\'' +
                ", matKhau='" + matKhau + '\'' +
                ", maNV=" + maNV +
                '}';
    }
}