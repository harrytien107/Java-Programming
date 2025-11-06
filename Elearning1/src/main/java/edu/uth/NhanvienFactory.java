package edu.uth;

class NhanvienFactory implements INhanvienFactory {
    @Override
    public Nhanvien createNhanvien(String loaiNV, String hoten, String maso, double luongCB) {
        Nhanvien nv ;
        switch (loaiNV) {
            case "LTV":
                nv = new Laptrinhvien(hoten, maso, luongCB);
                break;

                case "KTV":
                    nv = new Ketoanvien(hoten, maso, luongCB);
                    break;
                case "NVKT":
                    nv = new NhanvienKiemthu(hoten, maso, luongCB);
                    break;
                default:
                    nv = new ChuyenvienPhantich(hoten, maso, luongCB);
                    break;
        }
        return nv;
    }

    @Override
    public Nhanvien createNhanvien(String loaiNV) {
        Nhanvien nv ;
        switch (loaiNV) {
            case "LTV":
                nv = new Laptrinhvien();
                break;

                case "KTV":
                    nv = new Ketoanvien();
                    break;
                case "NVKT":
                    nv = new NhanvienKiemthu();
                    break;
                default:
                    nv = new ChuyenvienPhantich();
                    break;
        }
        return nv;
    }
}