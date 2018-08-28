package com.example.mycompute.lichhoc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MySQLite extends SQLiteOpenHelper {
    public MySQLite(Context context) {
        super(context, "lichhoc", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table dslichhoc(monHoc text,ngay text,tiet text,diaDiem text,giangVien text)";
        db.execSQL(sql);
        sql="create table dslichthi(monHoc text,ngay text,tiet text,diaDiem text,sbd text,hinhThuc text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql="DROP TABLE IF EXISTS dslichhoc";
        db.execSQL(sql);
        onCreate(db);
    }
    public boolean addLichHoc(itemSQL item){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("monHoc",item.monHoc);
        contentValues.put("ngay",item.ngay);
        contentValues.put("tiet",item.tiet);
        contentValues.put("diaDiem",item.diaDiem);
        contentValues.put("giangVien",item.giangVien);
        if(db.insert("dslichhoc",null,contentValues)>0){
            db.close();
            return true;
        }
        db.close();
        return false;
    }
    public boolean deleteLichHoc(){
        SQLiteDatabase db = this.getWritableDatabase();
        if(db.delete("dslichhoc",   null, null)>0){
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }
    public ArrayList<itemSQL> getLichHoc(){
        ArrayList<itemSQL> data=new ArrayList<>();
        String sql="select * from dslichhoc";
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor=db.rawQuery(sql,null);
        if(cursor.moveToFirst()) {
            do {
                itemSQL item = new itemSQL(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2),cursor.getString(3),cursor.getString(4));
                data.add(item);
            }

            while (cursor.moveToNext());
        }
        db.close();
        return data;
    }
    public String getDotHoc(String date){

        String sql="select * from dslichhoc where ngay='"+date+"'";
        SQLiteDatabase db=this.getReadableDatabase();
        String s="";

        Cursor cursor=db.rawQuery(sql,null);
        if(cursor.moveToFirst()) {
            do {
             s=s+"•";
            }
            while (cursor.moveToNext());
        }
        db.close();
        return s;
    }
    public boolean addLichThi(itemSQL item){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("monHoc",item.monHoc);
        contentValues.put("ngay",item.ngay);
        contentValues.put("tiet",item.tiet);
        contentValues.put("diaDiem",item.diaDiem);
        contentValues.put("sbd",item.sbd);
        contentValues.put("hinhThuc",item.hinhThuc);
        if(db.insert("dslichthi",null,contentValues)>0){
            db.close();
            return true;
        }
        db.close();
        return false;
    }
    public boolean deleteLichThi(){
        SQLiteDatabase db = this.getWritableDatabase();
        if(db.delete("dslichthi",   null, null)>0){
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }
    public ArrayList<itemSQL> getLichThi(){
        ArrayList<itemSQL> data=new ArrayList<>();
        String sql="select * from dslichthi";
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor=db.rawQuery(sql,null);
        if(cursor.moveToFirst()) {
            do {
                itemSQL item = new itemSQL(cursor.getString(0), cursor.getString(1), cursor.getString(2)
                        ,cursor.getString(3),cursor.getString(4),cursor.getString(5));
                data.add(item);
            }

            while (cursor.moveToNext());
        }
        db.close();
        return data;
    }
    public String getDotThi(String date){

        String sql="select * from dslichthi where ngay='"+date+"'";
        SQLiteDatabase db=this.getReadableDatabase();
        String s="";

        Cursor cursor=db.rawQuery(sql,null);
        if(cursor.moveToFirst()) {
            do {
                s=s+"•";
            }
            while (cursor.moveToNext());
        }
        db.close();
        return s;
    }
    public ArrayList<itemSQL> getLichOfDay(String date){
        ArrayList<itemSQL> data=new ArrayList<>();
         itemSQL []itemLH= getLichHocDAY(date);
         itemSQL []itemLT= getLichThiDAY(date);
         for(int i=0;i<itemLH.length;i++){
             data.add(itemLH[i]);
         }
        for(int i=0;i<itemLT.length;i++){
            data.add(itemLT[i]);
        }

        return data;
    }
    public itemSQL[] getLichHocDAY(String date) {

        ArrayList<itemSQL> data = new ArrayList<>();
        String sql = "select * from dslichhoc where ngay='" + date.trim() + "'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                itemSQL item = new itemSQL(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2),cursor.getString(3),cursor.getString(4));
                data.add(item);
            }

            while (cursor.moveToNext());
        }
        db.close();

        itemSQL[] temp =new itemSQL[data.size()];
        for (int i=0;i<temp.length;i++)
            temp[i]=data.get(i);

        for (int i = 0; i < temp.length; i++) {
            for (int j = i; j < temp.length; j++) {
                if (Integer.parseInt(temp[i].tiet.substring(0, 1)) > Integer.parseInt(temp[j].tiet.substring(0, 1))) {
                    itemSQL item = temp[i];
                    temp[i] = temp[j];
                    temp[j] = item;
                }

            }
        }
        return temp;
        }

    public itemSQL[] getLichThiDAY(String date) {

        ArrayList<itemSQL> data = new ArrayList<>();
        String sql = "select * from dslichthi where ngay='" + date.trim() + "'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                itemSQL item = new itemSQL(cursor.getString(0), cursor.getString(1), cursor.getString(2)
                        ,cursor.getString(3),cursor.getString(4),cursor.getString(5));
                data.add(item);
            }

            while (cursor.moveToNext());
        }
        db.close();

        itemSQL[] temp =new itemSQL[data.size()];
        for (int i=0;i<temp.length;i++)
            temp[i]=data.get(i);

        for (int i = 0; i < temp.length; i++) {
            for (int j = i; j < temp.length; j++) {
                if (Integer.parseInt(temp[i].tiet.substring(3, 4)) > Integer.parseInt(temp[j].tiet.substring(3, 4))) {
                    itemSQL item = temp[i];
                    temp[i] = temp[j];
                    temp[j] = item;
                }

            }
        }
        return temp;
    }
}




