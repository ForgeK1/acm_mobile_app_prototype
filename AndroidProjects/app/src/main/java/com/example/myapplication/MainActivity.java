package com.example.myapplication;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.Debug;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    CompactCalendarView compactCalendar;
    Button button;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM - yyyy", Locale.getDefault());

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(null);

        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);

         button= (Button)findViewById(R.id.new_event);

        //Set an event for Teachers' Professional Day 2016 which is 21st of October

        Event ev1 = new Event(Color.rgb(84,191,171), 1672036846000L, "Christmas");
        Event ev2 = new Event(Color.rgb(84,191,171), 1672563600000L, "New Years");
        Event ev3 = new Event(Color.rgb(84,191,171), 1672736400000L, "Martin Luther King’s Birthday");
        Event ev4 = new Event(Color.rgb(84,191,171), 1674464400000L, "Back To School");
        compactCalendar.addEvent(ev1);
        compactCalendar.addEvent(ev2);
        compactCalendar.addEvent(ev3);
        compactCalendar.addEvent(ev4);

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();

                if (dateClicked.toString().compareTo("Sun Dec 25 00:00:00 PST 2022") == 0) {
                    ((TextView)findViewById(R.id.textbox_forevents)).setText("Merry Christmas!");
                }else if (dateClicked.toString().compareTo("Sun Jan 01 00:00:00 PST 2023") == 0){
                    ((TextView)findViewById(R.id.textbox_forevents)).setText("New Years!");
                }else if (dateClicked.toString().compareTo("Tue Jan 03 00:00:00 PST 2023") == 0){
                    ((TextView)findViewById(R.id.textbox_forevents)).setText("Martin Luther King’s Birthday");
                }else if(dateClicked.toString().compareTo("Mon Jan 23 00:00:00 PST 2023") == 0){
                    ((TextView)findViewById(R.id.textbox_forevents)).setText("Back To School!");
                }else{
                    ((TextView)findViewById(R.id.textbox_forevents)).setText("No Events Planned For Today :(");
                }
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

    public void openEventActivity() {
        Intent intent = new Intent(this, EventActivity.class);
        startActivity(intent);
    }
}