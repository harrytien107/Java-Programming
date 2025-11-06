package edu.uth;

class TienthuongNgoaigio implements ITienThuong {
    @Override
    public double tinhTienThuong(double luongCB, double luongCoBan) {
        // Thưởng 20% lương cơ bản cho làm ngoài giờ
        return luongCoBan * 0.2;
    }
}
