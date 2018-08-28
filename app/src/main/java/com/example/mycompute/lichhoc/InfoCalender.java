package com.example.mycompute.lichhoc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import java.util.Calendar;

public class InfoCalender extends AppCompatActivity {
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_grid);
        final ViewPager viewPager=this.findViewById(R.id.viewPager);
        ///
        context=this;
        Intent intent=getIntent();
        Bundle bundle;
        bundle=intent.getBundleExtra("data");
        if(bundle!=null) {
            final LichHoc lichHoc = new LichHoc();
            lichHoc.ngayDuong = bundle.getString("ngayDuong");
            lichHoc.ngayAm = bundle.getString("ngayAm");
            lichHoc.dayWeek = bundle.getString("dayWeek");
            lichHoc.thangAm = bundle.getInt("thangAm");
            lichHoc.thang = bundle.getInt("thang");
            lichHoc.nam = bundle.getInt("nam");
            lichHoc.namAM = bundle.getInt("namAm");
            viewPager.setAdapter(new PagerAdapter(lichHoc,this));
            viewPager.setCurrentItem(500);
        }else {
            Calendar c=Calendar.getInstance();
            LichHoc lh=new LichHoc(String.valueOf(c.get(Calendar.DATE)),c.get(Calendar.MONTH)+1,c.get(Calendar.YEAR));
            viewPager.setAdapter(new PagerAdapter(lh,context));
            viewPager.setCurrentItem(500);
        }

        ///
        getSupportActionBar().hide();
        ///


        //
        Button today=this.findViewById(R.id.today);

        Calendar c=Calendar.getInstance();
        today.setText(String.valueOf(c.get(Calendar.DATE)));
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c=Calendar.getInstance();
                LichHoc lh=new LichHoc(String.valueOf(c.get(Calendar.DATE)),c.get(Calendar.MONTH)+1,c.get(Calendar.YEAR));
                viewPager.setAdapter(new PagerAdapter(lh,context));
                viewPager.setCurrentItem(500);
            }
        });
        ///



    }
}
