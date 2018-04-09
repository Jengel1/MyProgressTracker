package com.jdevelopment.myprogresstracker;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;


public class Alerts extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        //implement what you want to happen when alarm is triggered
        //banner over screen that says...

        Bundle b = intent.getExtras();

        //variables to hold title, message, and time
        String alertTitle = b.getString("title");
        String alertMessage = b.getString("message");
        long alertTime = b.getLong("time");

        Intent notIntent = new Intent();
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notIntent, 0);
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);

        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.bigText(alertMessage);

        //extend alert to wearable devices
        NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender()
                .setBackground(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_alert));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_alert)
                .setContentTitle(alertTitle)
                .setContentText(alertMessage)
                .setStyle(style)
                .setWhen(alertTime)
                .setAutoCancel(true)
                .extend(wearableExtender);  //may not need.

        Notification notification = builder.build();
        manager.notify(0, notification);

    }
}
