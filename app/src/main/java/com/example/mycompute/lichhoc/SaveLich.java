package com.example.mycompute.lichhoc;



import android.content.Context;
import android.widget.ArrayAdapter;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SaveLich {

     public ArrayList<itemSQL> tachLHFromHTML(Element dataHTML){
        ArrayList<itemSQL> data=new ArrayList<>();

        Elements listData=dataHTML.getElementsByTag("tr");

        listData.remove(listData.size()-1);
        listData.remove(0);

        if(listData.size()>0){
            for (int i=0;i<listData.size();i++) {
                Element item = listData.get(i);
                String monHoc=item.getElementsByTag("td").get(1).text();
                String thoiGian=item.getElementsByTag("td").get(3).html();
                String diaDiem=item.getElementsByTag("td").get(4).html();
                String giangVien=item.getElementsByTag("td").get(5).text();
                if(thoiGian.trim().isEmpty() || monHoc.trim().isEmpty() ||diaDiem.trim().isEmpty() )
                    return null;
                else {
                    ArrayList<itemSQL> itemsql = ConverterToitemSQL(monHoc, thoiGian, diaDiem, giangVien);
                    data.addAll(itemsql);
                }
            }

        }
        return data;
    }

    private  ArrayList<itemSQL> ConverterToitemSQL(String monHoc, String thoiGian, String diaDiem, String giangVien) {
         ArrayList<itemSQL> tDataItem=new ArrayList<>();
        String []listDay=thoiGian.split("br>T");
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String []dsDiaDiem=diaDiem.split("<b>");
        String [] stt=new String[dsDiaDiem.length];
        for(int j=1;j<dsDiaDiem.length;j++)
        {
            stt[j]=dsDiaDiem[j].substring(0,dsDiaDiem[j].indexOf("</b>"));
            dsDiaDiem[j]=dsDiaDiem[j].substring(dsDiaDiem[j].indexOf("<br>")+4);
        }

        for (int i=0;i<listDay.length;i++)
        {
            String starDate = listDay[i].substring(listDay[i].indexOf(' ') + 1, listDay[i].indexOf(' ') + 11);
            String endDate = listDay[i].substring(listDay[i].indexOf(' ', 16)+1, listDay[i].indexOf(' ', 16) + 11);
            ///lay ra thu
            int thu;
            if (listDay[i].indexOf("Ch") > 0) thu = 1;
            else {
                thu = Integer.parseInt(listDay[i].substring(listDay[i].indexOf(">Th") + 5, listDay[i].indexOf(">Th") + 6));
            }
            ///lay ra tiet hoc

            String tietHoc=listDay[i].substring(listDay[i].indexOf("ti") + 5, listDay[i].length());
            tietHoc=tietHoc.substring(0,tietHoc.indexOf('('));
            ///lay ra dia diem
            String tDiaDiem;
            if(dsDiaDiem.length>1) {
                 tDiaDiem = listDay[i].substring(listDay[i].indexOf('(') + 1, listDay[i].indexOf('(') + 2);
                for (int j = 1; j < dsDiaDiem.length; j++) {
                    if (stt[j].indexOf(tDiaDiem) > 0 && stt[j].charAt(stt[j].indexOf(tDiaDiem) - 1) != '1') {
                        tDiaDiem = dsDiaDiem[j].substring(dsDiaDiem[j].indexOf(')') + 1);
                        break;
                    }
                }
            }else  tDiaDiem=diaDiem;

            MyDate sDate=new MyDate();
            sDate.setDate(starDate);

            MyDate eDate=new MyDate();
            eDate.setDate(endDate);
//
            if(thu!=1)
            sDate.addDay(thu-2);
            else
                sDate.addDay(6);
            while (sDate.after(eDate)){
                    itemSQL item= new itemSQL(monHoc,sDate.toString(),tietHoc,tDiaDiem,giangVien);
                    tDataItem.add(item);
                    sDate.addDay(7);
            }

        }


    return tDataItem;
    }

    public boolean SaveLichHoc(ArrayList<itemSQL> dataLH, Context context){
         MySQLite mySQLite=new MySQLite(context);
         try {
             mySQLite.deleteLichHoc();
             for (int i=0;i<dataLH.size();i++){
                 mySQLite.addLichHoc(dataLH.get(i));
             }
             return true;
         }catch (Exception e){
             return false;
         }
    }
    public ArrayList<itemSQL> tachLTFromHTML(Element dataHTML){
        ArrayList<itemSQL> data=new ArrayList<>();

        Elements listData=dataHTML.getElementsByTag("tr");
        listData.remove(listData.size()-1);
        listData.remove(0);

        if(listData.size()>0){
            for (int i=0;i<listData.size();i++) {
                Element item = listData.get(i);
                String monHoc=item.getElementsByTag("td").get(2).text();
                String thoiGian=item.getElementsByTag("td").get(4).text();
                String diaDiem=item.getElementsByTag("td").get(8).text();
                String sbd=item.getElementsByTag("td").get(7).text();
                String hinhThuc=item.getElementsByTag("td").get(6).text();
                String caThi=item.getElementsByTag("td").get(5).text();
                if(thoiGian.trim().isEmpty() || monHoc.trim().isEmpty() ||diaDiem.trim().isEmpty()||sbd.trim().isEmpty() )
                    return null;
                else {
                    MyDate myDate=new MyDate();
                    myDate.setDate(thoiGian.trim());
                    itemSQL itemsql = new itemSQL(monHoc, myDate.toString(), caThi,diaDiem,sbd,hinhThuc );
                    data.add(itemsql);
                }
            }

        }
        return data;
    }
    public boolean SaveLichThi(ArrayList<itemSQL> dataLH, Context context){
        MySQLite mySQLite=new MySQLite(context);
        try {
            mySQLite.deleteLichThi();
            for (int i=0;i<dataLH.size();i++){
                mySQLite.addLichThi(dataLH.get(i));
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
