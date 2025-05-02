package entity;

public class NganhHang {
    private String maNganh;
    private String tenNganh;
    private String moTa;

    public String getMaNganh() {
        return maNganh;
    }

    public void setMaNganh(String maNganh) {
        this.maNganh = maNganh;
    }

    public String getTenNganh() {
        return tenNganh;
    }

    public void setTenNganh(String tenNganh) {
        this.tenNganh = tenNganh;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public NganhHang(String maNganh, String tenNganh, String moTa) {
        this.maNganh = maNganh;
        this.tenNganh = tenNganh;
        this.moTa = moTa;
    }

    public NganhHang() {
    }

    @Override
    public String toString() {
        return "NganhHang [maNganh=" + maNganh + ", tenNganh=" + tenNganh + ", moTa=" + moTa + "]";
    }

}
