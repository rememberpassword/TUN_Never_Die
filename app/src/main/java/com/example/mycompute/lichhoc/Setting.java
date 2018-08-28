package com.example.mycompute.lichhoc;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Calendar;

public class Setting extends AppCompatActivity {
    Context context;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        context=this;
        getSupportActionBar().hide();
        Switch thongBao=this.findViewById(R.id.swThongbao);
        //
        SharedPreferences sharedPreferences=getSharedPreferences("setting",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        boolean check=sharedPreferences.getBoolean("notifystate",true);
        thongBao.setChecked(check);
        //
        thongBao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("notifystate",isChecked);
                editor.commit();
                PendingIntent pendingIntent;
                AlarmManager alarmManager;
                Intent intent = new Intent(context, SeviceNotifycation.class);
                pendingIntent = PendingIntent.getBroadcast(context, 122, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                if(isChecked){
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.HOUR, 0);
                    c.set(Calendar.MINUTE, 0);
                    // alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(),0,pendingIntent);
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY, pendingIntent);
                }else {
                    pendingIntent.cancel();
                }
            }
        });

    }

    public void XoaLH(View view) {
        MySQLite mySQLite=new MySQLite(this);
        if(mySQLite.deleteLichHoc()) Toast.makeText(this,"Đã xoá toàn bộ lịch học",Toast.LENGTH_LONG).show();
    }

    public void XoaLT(View view) {
        MySQLite mySQLite=new MySQLite(this);
        if(mySQLite.deleteLichThi()) Toast.makeText(this,"Đã xoá toàn bộ lịch thi",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
