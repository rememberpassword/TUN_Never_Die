package com.example.mycompute.lichhoc;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Calendar;

public class SeviceNotifycation extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Calendar c1=Calendar.getInstance();
        MyDate date=new MyDate(c1.get(Calendar.DATE),c1.get(Calendar.MONTH)+1,c1.get(Calendar.YEAR));
        Intent intent1 = new Intent(context, InfoCalender.class);
        intent1.putExtra("time",date.toString());
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent1, 0);
        Notification noti;

        MySQLite mySQLite=new MySQLite(context);
        ArrayList<itemSQL> data1=mySQLite.getLichOfDay(date.toString());
        if(data1.size()>0){
            noti = new Notification.Builder(context)
                    .setContentTitle("Bạn có "+data1.size()+" lịch.")
                    .setContentText(data1.get(0).monHoc+" - "+data1.get(0).tiet+"-"+data1.get(0).diaDiem)
                    .setSmallIcon(R.drawable.ic_notify)
                    .setContentIntent(pIntent).build();
        }
        else{
            noti = new Notification.Builder(context)
                    .setContentTitle("Hôm nay bạn rảnh ")
                    .setContentText("")
                    .setSmallIcon(R.drawable.ic_notify)
                    .setContentIntent(pIntent).build();
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // hide the notification after its selected
        notificationManager.notify(0, noti);

    }

    }

