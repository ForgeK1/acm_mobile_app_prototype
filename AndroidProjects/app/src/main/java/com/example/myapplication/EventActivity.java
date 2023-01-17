package com.example.myapplication;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EventActivity extends AppCompatActivity
{
    private EditText eventNameET, eventDescET;
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
        eventDateTV = findViewById(R.id .eventDateET);
        eventTimeTV = findViewById(R.id.eventTimeET);
        eventDescET = findViewById(R.id.eventDescriptionET);
    }

    private void timePicker() {
        View pickTimeBtn = null;

        // on below line we are adding click
        // listener for our pick date button
        pickTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EventActivity.this, "Test", Toast.LENGTH_SHORT).show();

//                // on below line we are getting the
//                // instance of our calendar.
//                final Calendar c = Calendar.getInstance();
//
//                // on below line we are getting our hour, minute.
//                int hour = c.get(Calendar.HOUR_OF_DAY);
//                int minute = c.get(Calendar.MINUTE);
//                int timezoneInInt = c.get(Calendar.AM);
//
//                // on below line we are initializing our Time Picker Dialog
//                TimePickerDialog timePickerDialog = new TimePickerDialog(EventActivity.this,
//                        new TimePickerDialog.OnTimeSetListener() {
//                            @Override
//                            public void onTimeSet(TimePicker view, int hourOfDay,
//                                                  int minute) {
//                                String timezoneInString = null;
//
//                                if(timezoneInInt == 0){
//                                    timezoneInString = "AM";
//                                }
//                                else{
//                                    timezoneInString = "PM";
//                                }
//
//                                // on below line we are setting selected time
//                                // in our text view.
//                                eventTimeTV.setText(hourOfDay + ":" + minute + " " + timezoneInString);
//
//
//                            }
//                        }, hour, minute, false);
//                // at last we are calling show to
//                // display our time picker dialog.
//                timePickerDialog.show();
            }
        });
    }

    public void saveEventAction(View view)
    {

        if( TextUtils.isEmpty(eventNameET.getText()) ){
            eventNameET.setError( "Event name is required!" );
            //eventNameET.setHint("Event name is required!");
        }
        if (TextUtils.isEmpty(eventDescET.getText())){
            eventDescET.setError( "Event Description is required!" );
            //eventDescET.setHint("Event Description is required!");
        }
        if( !TextUtils.isEmpty(eventNameET.getText()) && !TextUtils.isEmpty(eventDescET.getText()) ){
        String name = eventNameET.getText().toString();
        String eventDesc = eventDescET.getText().toString();
        String eventTime = eventTimeTV.getText().toString();
        String eventDate = eventDateTV.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new user with a first and last name
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("Name", name);
        eventData.put("Description", eventDesc);
        eventData.put("Time", eventTime);
        eventData.put("Date", eventDate);

        // Add a new document with a generated ID
        db.collection("events")
                .add(eventData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Successful", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Error", "Error adding document", e);
                    }
                });

            //Redirects user back to main activity
            finish();
        }
    }
}