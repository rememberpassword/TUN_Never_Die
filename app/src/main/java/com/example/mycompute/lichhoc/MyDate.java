package com.example.mycompute.lichhoc;

import java.util.Calendar;

public class MyDate {
    int day;
    int month;
    int year;

    public MyDate() {

    }

    public MyDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }
    public String toString(){
        return day+"/"+month+"/"+year;
    }
    public void setDate(String date){
         day=Integer.parseInt(date.substring(0,2));
         month=Integer.parseInt(date.substring(3,5));
         year=Integer.parseInt(date.substring(6,date.length()));
    }
    public boolean after(MyDate date){
        if(this.year<date.year) return true;
        else if(this.month<date.month && this.year==date.year)  return true;
        else if(this.day<date.day && this.month==date.month && this.year==date.year) return true;
        else return false;
    }
    public boolean equals(MyDate date){
        if(this.day==date.day&&this.month==date.month&&this.year==date.year) return true;
        else return false;
    }
    public void addDay(int day){
        Calendar c=Calendar.getInstance();
        c.set(year,month-1,this.day);
        c.add(Calendar.DATE,day);
        year=c.get(Calendar.YEAR);
        month=c.get(Calendar.MONTH)+1;
        this.day=c.get(Calendar.DATE);
    }
    public void addMonth(int month){
        Calendar c=Calendar.getInstance();
        c.set(year,this.month-1,day);
        c.add(Calendar.MONTH,month);
        year=c.get(Calendar.YEAR);
        this.month=c.get(Calendar.MONTH)+1;
        this.day=c.get(Calendar.DATE);
    }
    public String getDayOfWeek(){
        String dayWeek="";
        Calendar c=Calendar.getInstance();
        c.set(year,month-1,day);
        int dayWee=c.get(Calendar.DAY_OF_WEEK);
        switch (dayWee){
            case 1:dayWeek="CHỦ NHẬT";break;
            case 2:dayWeek="THỨ HAI";break;
            case 3:dayWeek="THỨ BA";break;
            case 4:dayWeek="THỨ TƯ";break;
            case 5:dayWeek="THỨ NĂM";break;
            case 6:dayWeek="THỨ SÁU";break;
            case 7:dayWeek="THỨ BẢY";break;
        }
        return dayWeek;
    }


}
