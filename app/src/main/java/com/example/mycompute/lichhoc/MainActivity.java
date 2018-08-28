package com.example.mycompute.lichhoc;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Calender calender=new Calender(this);
    int day,month,tmonth,year,tyear;
    ArrayList<LichHoc> data;
    CustomGrid customGrid;
    Context context;
    TextView time;
    GridView gridView;
    String suser,spaas,sfullname;
    PendingIntent pendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setTitle("");

        context=this;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /////
        Calendar cal=Calendar.getInstance();
        day=cal.get(Calendar.DATE);
        month=tmonth=cal.get(Calendar.MONTH);
        year=tyear=cal.get(Calendar.YEAR);
        ////chayj sevice thong bao
        SharedPreferences sharedPreferences1=getSharedPreferences("temp",MODE_PRIVATE);
        String maSV=sharedPreferences1.getString("user","");
        String fullName=sharedPreferences1.getString("fullName","");
        SharedPreferences sharedPreferences=getSharedPreferences("setting",MODE_PRIVATE);
        boolean notifi_state=sharedPreferences.getBoolean("notifystate",true);
        if(notifi_state) {
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putBoolean("notifystate",true);
            editor.apply();
            Intent intent = new Intent(this, SeviceNotifycation.class);
            pendingIntent = PendingIntent.getBroadcast(this, 122, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR, 0);
            c.set(Calendar.MINUTE, 0);
            // alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(),0,pendingIntent);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        //
        TextView masv= navigationView.getHeaderView(0).findViewById(R.id.navMaSV);
        masv.setText(maSV);
        TextView fullname= navigationView.getHeaderView(0).findViewById(R.id.navFullName);
        fullname.setText(fullName);
        ////
        data = calender.getCalender(month,year);
        gridView=this.findViewById(R.id.calender);
        customGrid=new CustomGrid(this,data);
        gridView.setAdapter(customGrid);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(context,InfoCalender.class);
                Bundle bundle=new Bundle();
                bundle.putString("ngayDuong",data.get(position).ngayDuong);
                bundle.putString("ngayAm",data.get(position).ngayAm);
                bundle.putString("dayWeek",data.get(position).dayWeek);
                bundle.putInt("thangAm",data.get(position).thangAm);
                bundle.putInt("thang",data.get(position).thang);
                bundle.putInt("namAm",data.get(position).namAM);
                bundle.putInt("nam",data.get(position).nam);
                intent.putExtra("data",bundle);
                startActivity(intent);
            }
        });
        ////
        time=this.findViewById(R.id.time);
        ImageView next=this.findViewById(R.id.nextMonth);
        ImageView prew=this.findViewById(R.id.prewMonth);
        ImageView backTd=this.findViewById(R.id.backToday);
        ImageView btn_update=this.findViewById(R.id.btn_update);
        ///
        time.setText("Tháng "+(tmonth+1)+" - "+tyear);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmonth=tmonth+1;
                if(tmonth>11) {
                    tmonth=0;
                    tyear=tyear+1;
                }
                data=calender.getCalender(tmonth,tyear);
                customGrid=new CustomGrid(context,data);
                gridView.setAdapter(customGrid);
                time.setText("Tháng "+(tmonth+1)+" - "+tyear);
            }
        });
        prew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmonth=tmonth-1;
                if(tmonth<0){
                    tmonth=11;
                    tyear=tyear-1;
                }
                data=calender.getCalender(tmonth,tyear);
                customGrid=new CustomGrid(context,data);
                gridView.setAdapter(customGrid);
                time.setText("Tháng "+(tmonth+1)+" - "+tyear);
            }
        });
        backTd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmonth=month;
                tyear=year;
                data=calender.getCalender(tmonth,tyear);
                customGrid=new CustomGrid(context,data);
                gridView.setAdapter(customGrid);
                time.setText("Tháng "+(tmonth+1)+" - "+tyear);
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    if (suser != "" && spaas != "") {
                        Intent intent = new Intent(context, UpdateCalender.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, Login.class);
                        startActivity(intent);
                    }
                }else{
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Không có kết nối internet ");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.create().show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_capNhatLH) {
            if(isConnected()){
            if(suser!=""&& spaas!=""){
                Intent intent=new Intent(context,UpdateCalender.class);
                startActivity(intent);
            }else
            {
                Intent intent=new Intent(context,Login.class);
                startActivity(intent);
            }}
            else {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Thông báo");
                builder.setMessage("Không có kết nối internet ");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        } else if (id == R.id.nav_capNhatLT) {

            if(isConnected()){
            if(suser!=""&& spaas!=""){
                Intent intent=new Intent(context,UpdateLichThi.class);
                startActivity(intent);
            }else
            {
                Intent intent=new Intent(context,Login.class);
                startActivity(intent);
            }}
            else {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Thông báo");
                builder.setMessage("Không có kết nối internet ");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }

        } else if (id == R.id.nav_timeRaVaoLop) {
            Intent intent=new Intent(this,TimeRaVaoLop.class);
            startActivity(intent);

        } else if (id == R.id.nav_setting) {
            Intent intent=new Intent(this,Setting.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            SharedPreferences sharedPreferences=getSharedPreferences("temp",MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent=new Intent(this,Login.class);
            startActivity(intent);

        }else if(id==R.id.nav_Login){
            Intent intent=new Intent(this,Login.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void test(View view) {
        DatePickerDialog datePickerDialog;
        int year;
        int month;
        int dayOfMonth;
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        data = calender.getCalender(month,year);
                        customGrid=new CustomGrid(context,data);
                        gridView.setAdapter(customGrid);
                        time.setText("Tháng "+(month+1)+" - "+year);
                        tmonth=month;
                        tyear=year;
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }
    public boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}
