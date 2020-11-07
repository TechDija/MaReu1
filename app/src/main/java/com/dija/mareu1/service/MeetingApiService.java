package com.dija.mareu1.service;

import com.dija.mareu1.model.Meeting;

import java.util.List;

public interface MeetingApiService {
    void addMeeting(Meeting meeting);
    void deleteMeeting(Meeting Meeting);
    void reloadMeetings();
    List<Meeting> getMeetings();
    List<String> getAllRooms();

}
