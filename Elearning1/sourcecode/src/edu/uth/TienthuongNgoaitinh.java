package edu.uth;

class TienthuongNgoaitinh implements ITienThuong {
    @Override
    public double tinhTienThuong(double luongCB, double luongCoBan) {
        // Thưởng 15% lương cơ bản cho thưởng ngoại tình
        return luongCoBan * 0.15;
    }
}
