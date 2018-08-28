package com.example.mycompute.lichhoc;

public class LichHoc {
    String ngayDuong;
    String ngayAm;
    String dotHoc;
    String dotThi;
    String dayWeek;
    int thangAm;
    int namAM;
    int thang;
    int nam;

    public LichHoc(int dayWee,String ngayDuong, String ngayAm, String dotHoc, String dotThi,int thang,int nam) {
        this.ngayDuong = ngayDuong;
        this.ngayAm = ngayAm;
        this.dotHoc = dotHoc;
        this.dotThi = dotThi;
        this.thangAm = thangAm;
        this.namAM = namAM;
        this.thang=thang;
        this.nam=nam;
        switch (dayWee){
            case 1:dayWeek="CHỦ NHẬT";break;
            case 2:dayWeek="THỨ HAI";break;
            case 3:dayWeek="THỨ BA";break;
            case 4:dayWeek="THỨ TƯ";break;
            case 5:dayWeek="THỨ NĂM";break;
            case 6:dayWeek="THỨ SÁU";break;
            case 7:dayWeek="THỨ BẢY";break;
        }
    }

    public LichHoc() {

    }

    public LichHoc(String ngayDuong, int thang, int nam) {
        this.ngayDuong = ngayDuong;
        this.thang = thang;
        this.nam = nam;
    }

    public LichHoc(String ngayDuong, String ngayAm, String dotHoc, String dotThi) {
        this.ngayDuong = ngayDuong;
        this.ngayAm = ngayAm;
        this.dotHoc = dotHoc;
        this.dotThi = dotThi;
    }

    public LichHoc(String ngayDuong, String ngayAm) {
        this.ngayDuong = ngayDuong;
        this.ngayAm = ngayAm;
    }
}
