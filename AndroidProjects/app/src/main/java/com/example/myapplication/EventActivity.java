package com.example.myapplication;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
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

    public void timePicker(View v) {
        View pickTimeBtn = null;
        final Calendar c = Calendar.getInstance();

        // on below line we are getting our hour, minute.
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);

        // on below line we are initializing our Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(EventActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");

                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        cal.set(Calendar.MINUTE, minute);
                        Date date = cal.getTime();
                        String time = sdf.format(date);

                        // on below line we are setting selected time
                        // in our text view.
                        eventTimeTV.setText(time);

                    }
                }, hour, minute, false);
        // at last we are calling show to
        // display our time picker dialog.
        timePickerDialog.show();
    };

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

    public void UploadImg(View view){

    }

}