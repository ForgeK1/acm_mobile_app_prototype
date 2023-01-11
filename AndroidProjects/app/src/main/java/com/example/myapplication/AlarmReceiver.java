package com.example.myapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmReceiver<DatabaseReference> extends BroadcastReceiver {

    String title;
    String body;
    private FirebaseFirestore db;
    private DatabaseReference mDatabase;
    @Override
    public void onReceive(Context context, Intent intent) {
        db = FirebaseFirestore.getInstance();

        //checks if today a event is scheduled
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d yyyy");
        String date = sdf.format(Calendar.getInstance().getTime());
        db.collection("events")
                .whereEqualTo("Date", date)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    }
                });


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