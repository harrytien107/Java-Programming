package edu.uth;

class TienthuongThongthuong implements ITienThuong {
    @Override
    public double tinhTienThuong(double luongCB, double luongCoBan) {
        // Thưởng 10% lương cơ bản cho thưởng thông thường
        return luongCoBan * 0.1;
    }
}
