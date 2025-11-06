package edu.uth;

public interface INhanvienFactory {
    public Nhanvien createNhanvien(String loaiNV, String hoten, String maso, double luongCB);
    public Nhanvien createNhanvien(String loaiNV);
}
