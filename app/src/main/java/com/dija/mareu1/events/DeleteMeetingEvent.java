package com.dija.mareu1.events;

import com.dija.mareu1.model.Meeting;

public class DeleteMeetingEvent {
    public Meeting meeting;
    public DeleteMeetingEvent (Meeting meeting) {
        this.meeting = meeting;
    }
}
