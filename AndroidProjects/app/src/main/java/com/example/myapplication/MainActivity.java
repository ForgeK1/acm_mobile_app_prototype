package com.example.myapplication;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.Debug;


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
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
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

        try {
            //needs to be in a 24 hour format
            checkTodaysDate(23,42);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(null);

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
    //needs to be in 24 format
    private void checkTodaysDate(int hour,int min) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String date = sdf.format(Calendar.getInstance().getTime());
        Date current = sdf.parse(date);
        Date date2 = sdf.parse("01/02/2023");

        if (current.equals(date2)) {
            setTime(hour,min);
        }
    }


    private void setTime(int hour, int min){
        Calendar c= Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hour);
        c.set(Calendar.MINUTE,min);
        c.set(Calendar.SECOND,0);

        setAlarm(c);

    }

    private void setAlarm(Calendar c) {

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this,AlarmReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(this,1,intent,0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);

        Toast.makeText(this, "Alarm set Successfully", Toast.LENGTH_SHORT).show();
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