package com.dija.mareu1.Service;

import com.dija.mareu1.Model.Meeting;

import java.util.List;

public interface MeetingApiService {
    void addMeeting(Meeting meeting);
    void deleteMeeting(Meeting Meeting);
    List<Meeting> getMeetings();
    List<String> getAllRooms();

}
