package com.example.mycompute.lichhoc;

public class itemSQL {
    String monHoc;//mon thi
    String ngay;//ngay thi
    String tiet;//ca thi
    String diaDiem;//phong thi
    String giangVien;//
    String sbd;///sbd
    String hinhThuc;//hinh thuc

    public itemSQL(String monHoc, String ngay, String tiet, String diaDiem, String giangVien) {
        this.monHoc = monHoc;
        this.ngay = ngay;
        this.tiet = tiet;
        this.diaDiem = diaDiem;
        this.giangVien = giangVien;
    }

    public itemSQL(String monHoc, String ngay, String tiet, String diaDiem, String sbd, String hinhThuc) {
        this.monHoc = monHoc;
        this.ngay = ngay;
        this.tiet = tiet;
        this.diaDiem = diaDiem;
        this.sbd = sbd;
        this.hinhThuc = hinhThuc;
    }
}
