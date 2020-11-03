package com.dija.mareu1.Model;

import org.joda.time.Interval;

import java.util.Date;
import java.util.GregorianCalendar;

public class Meeting {
    private String date;
    private String beginningTime;
    private String endTime;
    private String room;
    private String subject;
    private String people;

    public String getDate() { return date; }

    public void setDate (String date) { this.date = date; }

    public String getBeginningTime() {
        return beginningTime;
    }

    public void setBeginningTime(String beginningTime) {
        this.beginningTime = beginningTime;
    }

    public String getEndTime(){ return endTime; }

    public void setEndTime(String endTime) {this.endTime = endTime; }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public Meeting(String date, String beginningTime, String endTime, String room, String subject, String people) {
        this.date = date;
        this.beginningTime = beginningTime;
        this.endTime = endTime;
        this.room = room;
        this.subject = subject;
        this.people = people;


    }
}
