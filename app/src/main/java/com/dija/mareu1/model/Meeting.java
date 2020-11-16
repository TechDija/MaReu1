package com.dija.mareu1.model;

public class Meeting {
    private Long beginningDateTime;
    private Long endDateTime;
    private String room;
    private String subject;
    private String people;

    public Long getBeginningDateTime() {
        return beginningDateTime;
    }

    public void setBeginningDateTime(Long beginningTime) {
        this.beginningDateTime = beginningTime;
    }

    public Long getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Long endTime) {
        this.endDateTime = endTime;
    }

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

    public Meeting(Long beginningDateTime, Long endDateTime, String room, String subject, String people) {
        this.beginningDateTime = beginningDateTime;
        this.endDateTime = endDateTime;
        this.room = room;
        this.subject = subject;
        this.people = people;


    }
}
