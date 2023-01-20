package com.example.myapplication;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
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
    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filepath; //uniform resource identifier object we create to make it so can get the unique path of each image uploaded
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
        eventDescET = findViewById(R.id.eventDescriptionET);
    }

    //function to give filepath a value after choosing an img to upload
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filepath = data.getData();
        }
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
        }
        //Redirects user back to main activity
        finish();
    }
    public void UploadImg(View view){
        //creating instance of the img database
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://acm-application.appspot.com/");
        //creating a reference of the storage instance
        StorageReference storageRef = storage.getReference(); // this pulls up the storage link ("gs://acm-application.appspot.com/")
        StorageReference flyerRef = storageRef.child("flyers"); // this puts us into the flyers folder in the firebase storage
        //creating new intent so we can choose the file we want to upload
        Intent intent = new Intent();
        intent.setType("image/*"); //this sets the type of files that can be uploaded, in our case we can only choose images like jpegs or pngs
        intent.setAction(Intent.ACTION_GET_CONTENT); // setting the intent to getting content in this case choosing an img
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST); //opens up google drive so we can choose the files we want to upload

        if (filepath != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);

            flyerRef.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot aVoid) {
                            progressDialog.setTitle("Uploading Image");
                            progressDialog.show();
                            progressDialog.dismiss();

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EventActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                        }
                    });

            System.out.println("--------------" + filepath.getPath() + "--------------");

        }
    }
}