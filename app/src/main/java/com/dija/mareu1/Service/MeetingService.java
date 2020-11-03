package com.dija.mareu1.Service;

import com.dija.mareu1.Model.Meeting;

import java.util.List;

public class MeetingService implements MeetingApiService {
    private List<Meeting> meetings = MeetingsGenerator.generateMeetings();
    private List<String> rooms = MeetingsGenerator.generateRooms();

    @Override
    public void addMeeting(Meeting meeting) {
        meetings.add(meeting);
    }

    @Override
    public void deleteMeeting(Meeting meeting) {
        meetings.remove(meeting);
    }

    @Override
    public List<Meeting> getMeetings() {
        return meetings;
    }

    @Override
    public List<String> getAllRooms() {
        return rooms ;
    }
}
