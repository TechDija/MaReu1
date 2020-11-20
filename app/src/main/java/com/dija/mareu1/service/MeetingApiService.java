package com.dija.mareu1.service;

import com.dija.mareu1.model.Meeting;
import com.dija.mareu1.model.Room;

import java.util.List;

public interface MeetingApiService {
    void addMeeting(Meeting meeting);

    void deleteMeeting(Meeting Meeting);

    List<Meeting> getMeetings();

    List<Room> getAllRooms();

    List<String> getAllRoomNames();

    List<Meeting> roomFilter(CharSequence constraint);

    List<Meeting> timeFilterService(long tag, long tag1);
}
