package edu.uth;

abstract class Nhanvien {
    protected String maso;
    protected String hoten;
    protected double luongCB;
    protected ITienThuong phuongthucTinhThuong;

    public Nhanvien(String maso, String hoten, double luongCB) {
        this.maso = maso;
        this.hoten = hoten;
        this.luongCB = luongCB;
    }

    public Nhanvien() {

    }

    @Override
    public String toString() {
        return "Nhanvien{" +
                "luongCB=" + luongCB +
                ", hoten='" + hoten + '\'' +
                ", maso='" + maso + '\'' +
                '}';
    }
    public double getTienthuong(){return luongCB;}

    public String getMaso() {
        return maso;
    }

    public String getHoten() {
        return hoten;
    }

    public double getLuongCB() {
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

    public void setLuongCB(double luongCB) {
        this.luongCB = luongCB;
    }

    public void setTienThuong(ITienThuong phuongthucTinhThuong) {
        this.phuongthucTinhThuong = phuongthucTinhThuong;
    }

    public double tinhTienThuong() {
        if (phuongthucTinhThuong != null) {
            return phuongthucTinhThuong.tinhTienThuong(0, luongCB);
        }
        return 0;
    }

    public double tinhLuong() {
        return luongCB + tinhTienThuong();
    }

    public String getThongTin() {
        return String.format("%-5s | %-20s | %,15.0f | %,15.0f | %,15.0f", 
                maso, hoten, luongCB, tinhTienThuong(), tinhLuong());
    }
}
