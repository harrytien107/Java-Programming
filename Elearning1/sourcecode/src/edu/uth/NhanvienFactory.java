package edu.uth;

class NhanvienFactory implements INhanvienFactory{
    @Override
    public Nhanvien createNhanvien(String loaiNV, String hoten, String maso, double luongCB) {
        Nhanvien nv ;
        switch (loaiNV) {
            case "LTV":
                nv = new Laptrinhvien(maso, hoten, luongCB);
                break;

            case "KTV":
                nv = new Ketoanvien(maso, hoten, luongCB);
                break;

            case "NVKT":
                nv = new NhanvienKiemthu(maso, hoten, luongCB);
                break;
                default:
                    nv = new ChuyenvienPhantich(maso, hoten, luongCB);
                    break;
        }
        return nv ;
    }

    @Override
    public Nhanvien createNhanvien(String loaiNV) {
        Nhanvien nv ;
        switch (loaiNV) {
            case "LTV":
                nv = new Laptrinhvien(); //
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
        return nv ;
    }
}
