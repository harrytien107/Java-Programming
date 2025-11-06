package edu.uth;

public class Main {
    public static void main(String[] args) {
        // Tạo factory
        INhanvienFactory factory = new NhanvienFactory();
        
        // Tạo các nhân viên
        Nhanvien dev = factory.createNhanvien("LTV", "Nguyen Van A", "NV001", 15000000);
        Nhanvien tester = factory.createNhanvien("NVKT", "Tran Thi B", "NV002", 12000000);
        
        // Thiết lập chiến lược tính thưởng
        dev.setTienThuong(new TienthuongNgoaigio()); // Thưởng làm ngoài giờ (20%)
        tester.setTienThuong(new TienthuongThongthuong()); // Thưởng thông thường (10%)
        
        // In tiêu đề
        System.out.println("BANG LUONG NHAN VIEN");
        System.out.println("-".repeat(80));
        System.out.println(String.format("%-5s | %-20s | %15s | %15s | %15s", 
                "Ma", "Ho ten", "Luong co ban", "Tien thuong", "Tong luong"));
        System.out.println("-".repeat(80));
        
        // In thông tin nhân viên
        System.out.println(dev.getThongTin());
        System.out.println(tester.getThongTin());
        
        // Tổng kết
        System.out.println("-".repeat(80));
        System.out.println(String.format("Tong cong: %,15.0f VND", 
                dev.tinhLuong() + tester.tinhLuong()));
    }
}
