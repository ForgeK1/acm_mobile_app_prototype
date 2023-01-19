package com.example.myapplication;

public class CalendarEvent {

    String Name;
    String Description;
    String Time;
    String Date;
    String URL;

    public CalendarEvent() {}

    public CalendarEvent(String Name, String Description, String Time, String Date) {
        this.Name = Name;
        this.Description = Description;
        this.Time = Time;
        this.Date = Date;
    }


    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String Time) {
        this.Time = Time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}