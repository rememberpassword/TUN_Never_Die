package com.example.mycompute.lichhoc;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PagerAdapter extends android.support.v4.view.PagerAdapter {

    LichHoc lichHoc;
    Context context;
    MySQLite mySQLite;
    public PagerAdapter(LichHoc lichHoc, Context context){

        this.lichHoc=lichHoc;
        this.context=context;
        mySQLite=new MySQLite(context);

    }

    @Override
    public int getCount() {
        return 1000;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        int k=position-500;
        View v= LayoutInflater.from(context).inflate(R.layout.item_viewpager,container,false);
        MyDate myDate=new MyDate(Integer.parseInt(lichHoc.ngayDuong),lichHoc.thang,lichHoc.nam);
        myDate.addDay(k);
        ChinaCalendar china=new ChinaCalendar(myDate.day,myDate.month,myDate.year,7);
        china.ConVertToLunar();

        TextView ngayDuong=v.findViewById(R.id.txtNgayDuong);
        TextView thNamDuong=v.findViewById(R.id.txtThangNamDuong);
        TextView ngayAm=v.findViewById(R.id.txtNgayAm);
        TextView thNamAm=v.findViewById(R.id.txtThangNamAm);
        TextView thu=v.findViewById(R.id.txtThu);
        ///
        ngayDuong.setText(String.valueOf(myDate.day));
        thNamDuong.setText(myDate.month+" / "+myDate.year);
        ngayAm.setText(String.valueOf(china.getLNDay()));
        thNamAm.setText(china.getLNMonth()+" / "+china.getLNYear());
        thu.setText(myDate.getDayOfWeek());
        ///
        ListView listView=v.findViewById(R.id.listViewDay);
        ArrayList<itemSQL> listOfDay=new ArrayList<>();
        listOfDay=mySQLite.getLichOfDay(myDate.toString());
        CustomAdapterList adapterList=new CustomAdapterList(context,listOfDay);
        listView.setAdapter(adapterList);
        //

        container.addView(v);
        return v;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
