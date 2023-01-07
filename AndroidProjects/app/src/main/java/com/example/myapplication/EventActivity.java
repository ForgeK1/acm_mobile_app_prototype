package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalTime;

public class EventActivity extends AppCompatActivity
{
    private EditText eventNameET;
    private TextView eventDateTV, eventTimeTV;

    private LocalTime time;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();
        time = LocalTime.now();
        eventDateTV.setText(CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        eventTimeTV.setText(CalendarUtils.formattedTime(time));
    }

    private void initWidgets()
    {
        eventNameET = findViewById(R.id.eventNameET);
        eventDateTV = findViewById(R.id.eventDateET);
        eventTimeTV = findViewById(R.id.eventTimeET);
    }

    public void saveEventAction(View view)
    {
        String eventName = eventNameET.getText().toString();
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("eventdatabases.csv"));
            bw.write(eventName + ", " + eventTimeTV.toString() + ", " + eventDateTV.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Event newEvent = new Event(eventName, CalendarUtils.selectedDate, time);
        Event.eventsList.add(newEvent);
        finish();
    }
}