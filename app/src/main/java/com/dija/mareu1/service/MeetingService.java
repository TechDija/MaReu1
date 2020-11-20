package com.dija.mareu1.service;

import com.dija.mareu1.model.Meeting;
import com.dija.mareu1.model.Room;

import java.util.ArrayList;
import java.util.List;

public class MeetingService implements MeetingApiService {
    private final List<Meeting> meetings = MeetingsGenerator.generateMeetings();
    private final List<Room> rooms = RoomGenerator.generateRooms();
    private final List<String> roomNames = RoomGenerator.generateRoomNames();

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
    public List<Room> getAllRooms() {
        return rooms;
    }

    @Override
    public List<String> getAllRoomNames() {
        return roomNames;
    }

    @Override
    public List<Meeting> roomFilter(CharSequence constraint) {
        List<Meeting> filteredList = new ArrayList<>();
        if (constraint == null || constraint.length() == 0) {
            filteredList.addAll(meetings);
        } else {
            for (Meeting meeting : meetings) {
                if (constraint.toString().contains(meeting.getRoom())) {
                    filteredList.add(meeting);
                }
            }
        }
        return filteredList;
    }

    @Override
    public List<Meeting> timeFilterService(long tag, long tag1) {
        List<Meeting> filteredList = new ArrayList<>();
        if (tag == 0 && tag1 == 0) {
            filteredList.addAll(meetings);
        } else {
            for (Meeting meeting : meetings) {
                if ((tag < meeting.getBeginningDateTime() && meeting.getBeginningDateTime() < tag1)
                        || (tag < meeting.getEndDateTime() && meeting.getEndDateTime() < tag1)) {
                    filteredList.add(meeting);
                }
            }
        }
        return filteredList;
    }
}
