package com.example.mycompute.lichhoc;

import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.SimpleFormatter;

public class Calender {
    Context context;

    public Calender(Context context) {
        this.context = context;
    }

    public ArrayList<LichHoc> getCalender(int month, int year){


        ArrayList<LichHoc> data=new ArrayList<>();
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
        ///today
        Calendar calendar=Calendar.getInstance();
        Date today1=calendar.getTime();
        ///
        calendar.set(year,month,1);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        int dayWeek= calendar.get(Calendar.DAY_OF_WEEK);
        calendar.set(year,month+1,1);
        calendar.add(Calendar.DATE,-1);
        int maxDayMonth=calendar.get(Calendar.DATE);
        if(dayWeek!=1)
        for(int i=1;i<=(maxDayMonth+dayWeek-2);i++){
            if(i>=(dayWeek-1)) {
                ChinaCalendar china=new ChinaCalendar(i-(dayWeek-1),month+1,year,7);
                Date datelunnar = china.ConVertToLunar();
                DateFormat df = new SimpleDateFormat("dd/MM");
                String dateString  = df.format(datelunnar);
                /////gán lịch


                if(dateString.indexOf("01/")==0)
                    data.add(create(dayWeek,String.valueOf(i-(dayWeek-2)),dateString,month+1,year));
                else
                    data.add(create(dayWeek,String.valueOf(i-(dayWeek-2)),dateString.substring(0,2),month+1,year));
                /////
            }
            else data.add(new LichHoc());
        }
        else
            for(int i=1;i<=(maxDayMonth+6);i++){
                if(i>6) {
                    ChinaCalendar china=new ChinaCalendar(i-6,month+1,year,7);
                    Date datelunnar = china.ConVertToLunar();
                    //String date1 = datelunnar.toString("dd/MM/yyy") ;
                    DateFormat df = new SimpleDateFormat("dd/MM");
                    String dateString  = df.format(datelunnar);

                    ////gán lịch
                    if(dateString.indexOf("01/")==0)
                        data.add(create(dayWeek,String.valueOf(i-6),dateString,month+1,year));
                    else
                        data.add(create(dayWeek,String.valueOf(i-6),dateString.substring(0,2),month+1,year));
                    /////
                }
                else data.add(new LichHoc());
            }
       return data;
    }

    LichHoc create(int dayWeek,String ngayDuong,String ngayAm,int month,int nam){
        MyDate date=new MyDate(Integer.parseInt(ngayDuong),month,nam);
        MySQLite mySQLite=new MySQLite(context);
        String dotHoc=mySQLite.getDotHoc(date.toString());
        String dotThi=mySQLite.getDotThi(date.toString());
        return new LichHoc(dayWeek,ngayDuong,ngayAm,dotHoc,dotThi,month,nam);
    }

}
