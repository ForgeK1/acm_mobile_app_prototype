package com.example.myapplication;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.Debug;


import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    private CompactCalendarView compactCalendar;
    private Button button;
    private ListView listView;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM - yyyy", Locale.getDefault());

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(null);

        //checks if today a event is scheduled
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String todayString = formatter.format(todayDate);



        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);

        button= (Button)findViewById(R.id.new_event);
        listView = findViewById(R.id.eventListView);
        //Set an event for Teachers' Professional Day 2016 which is 21st of October

//        Event ev1 = new Event(Color.rgb(84,191,171), 1672036846000L, "Christmas");
//        Event ev2 = new Event(Color.rgb(84,191,171), 1672563600000L, "New Years");
//        Event ev3 = new Event(Color.rgb(84,191,171), 1672736400000L, "Martin Luther King’s Birthday");
//        Event ev4 = new Event(Color.rgb(84,191,171), 1674464400000L, "Back To School");
//        compactCalendar.addEvent(ev1);
//        compactCalendar.addEvent(ev2);
//        compactCalendar.addEvent(ev3);
//        compactCalendar.addEvent(ev4);
        setEventAdpater();
        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();

//                if (dateClicked.toString().compareTo("Sun Dec 25 00:00:00 PST 2022") == 0) {
//                    ((TextView)findViewById(R.id.textbox_forevents)).setText("Merry Christmas!");
//                }else if (dateClicked.toString().compareTo("Sun Jan 01 00:00:00 PST 2023") == 0){
//                    ((TextView)findViewById(R.id.textbox_forevents)).setText("New Years!");
//                }else if (dateClicked.toString().compareTo("Tue Jan 03 00:00:00 PST 2023") == 0){
//                    ((TextView)findViewById(R.id.textbox_forevents)).setText("Martin Luther King’s Birthday");
//                }else if(dateClicked.toString().compareTo("Mon Jan 23 00:00:00 PST 2023") == 0){
//                    ((TextView)findViewById(R.id.textbox_forevents)).setText("Back To School!");
//                }else{
//                    ((TextView)findViewById(R.id.textbox_forevents)).setText("No Events Planned For Today :(");
//                }
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

    public void createNoti(String title, String body){

    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "ACMReminderChannel";
            String description = "Channel For Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("ACM",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }

    public void openEventActivity() {
        Intent intent = new Intent(this, EventActivity.class);
        startActivity(intent);
    }

    public void openNewActivity(){

    }

    private void setEventAdpater()
    {
        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
        listView.setAdapter(eventAdapter);
    }
}