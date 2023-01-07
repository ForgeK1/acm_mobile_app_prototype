package com.example.myapplication;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CalendarEventAdapter extends RecyclerView.Adapter<CalendarEventAdapter.ViewHolder> {
    // creating variables for our ArrayList and context
    private ArrayList<CalendarEvent> eventArrayList;
    private Context context;

    // creating constructor for our adapter class
    public CalendarEventAdapter(ArrayList<CalendarEvent> eventArrayList, Context context) {
        this.eventArrayList = eventArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CalendarEventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.event_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarEventAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        CalendarEvent calendarEvent = eventArrayList.get(position);
        holder.eventNameTV.setText(calendarEvent.getName());
        holder.eventDescTV.setText(calendarEvent.getDescription());
        holder.eventTimeTV.setText(calendarEvent.getTime());
        holder.eventDateTV.setText(calendarEvent.getDate());
    }

    @Override
    public int getItemCount() {
        // returning the size of our array list.
        return eventArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final TextView eventNameTV;
        private final TextView eventDescTV;
        private final TextView eventTimeTV;
        private final TextView eventDateTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            eventNameTV = itemView.findViewById(R.id.idTVName);
            eventDescTV = itemView.findViewById(R.id.idTVDescription);
            eventTimeTV = itemView.findViewById(R.id.idTVTime);
            eventDateTV = itemView.findViewById(R.id.idTVDate);
        }
    }
}