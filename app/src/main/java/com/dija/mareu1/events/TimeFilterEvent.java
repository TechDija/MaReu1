package com.dija.mareu1.events;

public class TimeFilterEvent {
    public long firstDate;
    public long secondDate;

    public TimeFilterEvent(long firstDate, long secondDate) {
        this.firstDate = firstDate;
        this.secondDate = secondDate;
    }
}
