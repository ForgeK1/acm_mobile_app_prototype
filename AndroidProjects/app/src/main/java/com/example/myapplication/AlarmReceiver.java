package com.example.myapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {

    String title;
    String body;
    @Override
    public void onReceive(Context context, Intent intent) {
        Date date2 = null;
        Date current = null;


        //checks if today a event is scheduled
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String date = sdf.format(Calendar.getInstance().getTime());

        Date genMeeting1 = null;
        try {
            genMeeting1 = sdf.parse("01/05/2023");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date gameNight = null;
        try {
            gameNight = sdf.parse("01/03/2023");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            //current = sdf.parse("01/04/2023");
            current = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            date2 = sdf.parse("01/06/2023");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (current.equals(date2)) {
            title = "Hello";
            body = "Good day there";
        } else if (current.equals(genMeeting1)) {
            title = "General Meeting";
            body = "Meeting today at 3:00 Room 350";
        } else if (current.equals(gameNight)) {
            title = "Game Night";
            body = "Come to room ET:345 at 3:00 for some fun games with fellow ACM members with some awesome prizes";
        } else {
            title = "Goodbye";
            body = "Goodnight";
        }

        Intent i = new Intent(context, SecondActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, i, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "ACM")
                .setSmallIcon(R.drawable.ic_party)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);


        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123, builder.build());


    }
}