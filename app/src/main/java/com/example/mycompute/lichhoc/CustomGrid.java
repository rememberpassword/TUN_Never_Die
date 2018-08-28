package com.example.mycompute.lichhoc;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class CustomGrid extends BaseAdapter {

    Context context;
    ArrayList<LichHoc> data;
    MyDate today;
    public CustomGrid(Context context,ArrayList<LichHoc> data) {
        Calendar c=Calendar.getInstance();
        today=new MyDate(c.get(Calendar.DATE),c.get(Calendar.MONTH)+1,c.get(Calendar.YEAR));
        this.context=context;
        this.data=data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        convertView = LayoutInflater.from(context).inflate(R.layout.item_grid,parent,false);
        TextView ngayDuong=convertView.findViewById(R.id.ngayDuong);
        TextView ngayAm=convertView.findViewById(R.id.ngayAm);
        TextView dotHoc=convertView.findViewById(R.id.dot_hoc);
        TextView dotThi=convertView.findViewById(R.id.dot_thi);
        ///
        LichHoc item=data.get(position);
        if((position+1)%7==0){
            ngayAm.setTextColor(Color.rgb(250,38,100));
            ngayDuong.setTextColor(Color.rgb(250,38,100));
        }
        if(item.ngayAm!=null) ngayAm.setText(item.ngayAm); else ngayAm.setText("");
        if(item.ngayDuong!=null) {
            ngayDuong.setText(item.ngayDuong);
            MyDate myDate=new MyDate(Integer.parseInt(item.ngayDuong),item.thang,item.nam);
            if(myDate.equals(today)){
                ConstraintLayout constraintLayout=convertView.findViewById(R.id.itemConstrain);
                constraintLayout.setBackgroundResource(R.drawable.around);
            }
        }
        else ngayDuong.setText("");
        if(item.dotHoc!=null)dotHoc.setText(item.dotHoc);
        else dotHoc.setText("");
        if(item.dotThi!=null)dotThi.setText(item.dotThi); else dotThi.setText("");

        return convertView;
    }
}
