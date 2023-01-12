package com.example.myapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

        Intent i = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, i, PendingIntent.FLAG_IMMUTABLE);

        db.collection("events")
                .whereEqualTo("Date", date)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                CalendarEvent c = d.toObject(CalendarEvent.class);

                                title = c.getName();
                                body = c.getDescription();

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
                    }
                });

    }
}