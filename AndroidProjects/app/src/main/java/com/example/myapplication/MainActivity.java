package com.example.myapplication;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.Debug;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.example.myapplication.Event;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

public class MainActivity extends AppCompatActivity {


    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private CompactCalendarView compactCalendar;
    private Button button;
    private ListView listView;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM - yyyy", Locale.getDefault());

    // creating variables for our recycler view,
    // array list, adapter, firebase firestore
    // and our progress bar.
    private RecyclerView recyclerView;
    private ArrayList<CalendarEvent> calendarEventArrayList;
    private CalendarEventAdapter calendarEventAdapter;
    private FirebaseFirestore db;
    private String eventDate;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        try {
//            checkTodaysDate(4,05);
            checkTodaysDate(1,52);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        //actionBar.setTitle(dateFormatMonth.format(compactCalendar.));

        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);

        // initializing our variables for the Calendar
        recyclerView = findViewById(R.id.eventRecyclerView);
        db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        calendarEventArrayList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        button= (Button)findViewById(R.id.new_event);
        listView = findViewById(R.id.eventListView);



        calendarEventAdapter = new CalendarEventAdapter(calendarEventArrayList, this, new CalendarEventAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int eventArrayList) {
               // Toast.makeText(MainActivity.this, "Item Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, Flyer.class);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(calendarEventAdapter);

        //Set an event for Teachers' Professional Day 2016 which is 21st of October
//        Event ev1 = new Event(Color.rgb(84,191,171), 1672036846000L, "Christmas");
//        Event ev2 = new Event(Color.rgb(84,191,171), 1672563600000L, "New Years");
//        Event ev3 = new Event(Color.rgb(84,191,171), 1672736400000L, "Martin Luther King’s Birthday");
//        Event ev4 = new Event(Color.rgb(84,191,171), 1674464400000L, "Back To School");
//        compactCalendar.addEvent(ev1);
//        compactCalendar.addEvent(ev2);
//        compactCalendar.addEvent(ev3);
//        compactCalendar.addEvent(ev4);
//        setEventAdpater();

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();
                Log.d("calendar date", dateClicked.toString());
                // *
                // db.collection("events").whereEqualTo("Date",dateClicked).get()
                // *//
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d yyyy");
                String date = simpleDateFormat.format(dateClicked);
                //Toast.makeText(MainActivity.this, date, Toast.LENGTH_SHORT).show();
//                Date clickedDate = null;
//                try {
//                    clickedDate= simpleDateFormat.parse(date);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }

                db.collection("events")
                        .whereEqualTo("Date", date)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                // after getting the data we are calling on success method
                                // and inside this method we are checking if the received
                                // query snapshot is empty or not.
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    // if the snapshot is not empty we are
                                    // hiding our progress bar and adding
                                    // our data in a list.
                                    calendarEventArrayList.clear();

                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                    for (DocumentSnapshot d : list) {

                                        // after getting this list we are passing
                                        // that list to our object class.

                                        CalendarEvent c = d.toObject(CalendarEvent.class);
                                        // and we will pass this object class
                                        // inside our arraylist which we have
                                        // created for recycler view.
                                        calendarEventArrayList.add(c);

                                        System.out.println("adding to the event list!");
                                    }
                                    // after adding the data to recycler view.
                                    // we are calling recycler view notifyDataSetChanged
                                    // method to notify that data has been changed in recycler view.
                                    calendarEventAdapter.notifyDataSetChanged();

                                } else{
                                    // if the snapshot is empty we are displaying a toast message.
                                    calendarEventAdapter.recyclerClear();
                                    Toast.makeText(MainActivity.this, "Nothing planned today", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // if we do not get any data or any error we are displaying
                                // a toast message that we do not get any data
                                Toast.makeText(MainActivity.this, "Fail to get the data.", Toast.LENGTH_SHORT).show();
                            }
                        });
                     }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                actionBar.setTitle(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEventActivity();
            }
        });
    }

    private void parseCSVFile(){
        InputStream path = getResources().openRawResource(R.raw.eventdatabases);
        String line = "";
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> descp = new ArrayList<>();
        ArrayList<String> date = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(path, Charset.forName("UTF-8")));
            //skipping the titles
            br.readLine();
            while((line = br.readLine()) != null){
                String[] parser = line.split(",");
                names.add(parser[0]);
                descp.add(parser[1]);
                date.add(parser[3]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < names.size(); i++) {
            System.out.println("Name: " + names.get(i));
            System.out.println("description: " + descp.get(i));
            System.out.println("date: " +date.get(i));
        }

    }

    private void createNotificationChannel() {

            CharSequence name = "ACMReminderChannel";
            String description = "Channel For Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("ACM",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

    }
    //needs to be in 24 format
    private void checkTodaysDate(int hour, int min) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d yyyy");
        String today = sdf.format(Calendar.getInstance().getTime());

        FirebaseFirestore Notifs = FirebaseFirestore.getInstance();
        Notifs.collection("events")
                .whereEqualTo("Date", today)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                    setTime(hour,min);
                            }
                        } else{
                            // if the snapshot is empty we are displaying a toast message.
                            //Toast.makeText(MainActivity.this, "Yo", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void setTime(int hour, int min) {
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        String currentTime = sdf.format(Calendar.getInstance().getTime());

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR, hour);
        c.set(Calendar.MINUTE, min);

        Calendar three = Calendar.getInstance();
        three.set(Calendar.HOUR, 3);

        Date current = null;
        try {
            current = new SimpleDateFormat("h:mm a").parse(currentTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date pastThree = null;
        try {
            pastThree = new SimpleDateFormat("h:mm a").parse(String.valueOf(three));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //uncomment this later
//        if (!current.after(pastThree)) {
//            setAlarm(c);
//        }
        setAlarm(c);
    }

    private void setAlarm(Calendar c) {

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this,AlarmReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(this,1,intent,PendingIntent.FLAG_IMMUTABLE);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);

        Toast.makeText(this, "Alarm set Successfully", Toast.LENGTH_SHORT).show();
    }

    public void openEventActivity() {
        parseCSVFile();
        Intent intent = new Intent(this, EventActivity.class);
        startActivity(intent);
    }

//    private void setEventAdpater()
//    {
//        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
//        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
//        listView.setAdapter(eventAdapter);
//    }
}