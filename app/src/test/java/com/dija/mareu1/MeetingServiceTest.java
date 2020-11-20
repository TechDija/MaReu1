package com.dija.mareu1;

import com.dija.mareu1.DI.DI;
import com.dija.mareu1.model.Meeting;
import com.dija.mareu1.model.Room;
import com.dija.mareu1.service.MeetingApiService;
import com.dija.mareu1.service.MeetingsGenerator;
import com.dija.mareu1.service.RoomGenerator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MeetingServiceTest {
    private MeetingApiService service;

    @BeforeEach
    public void setup() {
        service = DI.getNewInstanceMeetingApiService();
    }

    @Test
    @DisplayName("get meetings")
    public void getMeetingsWithSuccess() {
        List<Meeting> actual = service.getMeetings();
        List<Meeting> expected = MeetingsGenerator.MEETINGS;
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("get rooms")
    public void getAllRoomsWithSuccess() {
        List<Room> actual = service.getAllRooms();
        List<Room> expected = RoomGenerator.ROOMS;
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("get room names")
    public void getAllRoomNamesWithSuccess() {
        List<String> actual = service.getAllRoomNames();
        List<String> expected = RoomGenerator.ROOM_NAMES;
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("deleting a meeting from the meetings list")
    public void deleteMeetingWithSuccess() {
        Meeting meetingToDelete = service.getMeetings().get(0);
        service.deleteMeeting(meetingToDelete);
        assertFalse(service.getMeetings().contains(meetingToDelete));
    }

    @Test
    @DisplayName("adding a meeting to the meetings list")
    public void addMeetingsWithSuccess() {
        Meeting meetingToCopy = service.getMeetings().get(0);
        Meeting meetingToAdd = meetingToCopy;
        service.addMeeting(meetingToAdd);
        assertEquals(4, service.getMeetings().size());
    }

    @Test
    @DisplayName("filtering on room name 'Mario'")
    public void roomFilteringWithSuccess() {
        List<Meeting> actual = service.roomFilter("Mario");
        assertEquals(1, actual.size());
    }

    @Test
    @DisplayName("filtering on a blank constraint")
    public void roomFilteringFailureCase() {
        List<Meeting> actual = service.roomFilter("");
        assertEquals(3, actual.size());
    }

    @Test
    @DisplayName("filtering on time between 31th october 2020 8:00 and 14h30")
    public void timeFilteringWithSuccess() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - kk:mm");
        long tag = sdf.parse("31/10/2020 - 08:00").getTime();
        long tag1 = sdf.parse("31/10/2020 - 14:30").getTime();
        List<Meeting> actual = service.timeFilterService(tag, tag1);
        assertEquals(2, actual.size());
    }

    @Test
    @DisplayName("filtering on null tags")
    public void timeFilteringFailureCase() {
        long tag = 0;
        long tag1 = 0;
        List<Meeting> actual = service.timeFilterService(tag, tag1);
        assertEquals(3, actual.size());
    }
}