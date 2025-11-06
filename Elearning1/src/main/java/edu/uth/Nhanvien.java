package edu.uth;

abstract class Nhanvien {
    protected String maso;
    protected String hoten;
    protected Double luongCB;
    protected ITienThuong phuongthucTinhThuong;

    public Nhanvien(String maso, String hoten, Double luongCB) {
        this.maso = maso;
        this.hoten = hoten;
        this.luongCB = luongCB;
    }

    public Nhanvien() {
    }
    
    @Override
    public String toString() {
        return "Nhanvien{" +
                "maso='" + maso + '\'' +
                ", hoten='" + hoten + '\'' +
                ", luongCB=" + luongCB +
                '}';
    }
    public double getTienthuong() {
        return luongCB;
    }
    public String getMaso() {
        return maso;
    }

    public String getHoten() {
        return hoten;
    }

    public Double getLuongCB() {
        return luongCB;
    }
    public ITienThuong getPhuongthucTinhThuong() {
        return phuongthucTinhThuong;
    }
    public void setMaso(String maso) {
        this.maso = maso;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public void setLuongCB(Double luongCB) {
        this.luongCB = luongCB;
    }
}
