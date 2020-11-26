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
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MeetingServiceTest {
    private MeetingApiService service;
    private Meeting testMeeting = new Meeting (parser("31/10/2020 - 08:00"), parser("31/10/2020 - 08:45"), "Mario", "Réunion B", "paul@lamzone.com, viviane@lamzone.com");

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
    @DisplayName("Getting all rooms")
    public void getAllRoomsWithSuccess() {
        List<Room> actual = service.getAllRooms();
        List<Room> expected = RoomGenerator.ROOMS;
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Getting all room names")
    public void getAllRoomNamesWithSuccess() {
        List<String> actual = service.getAllRoomNames();
        List<String> expected = RoomGenerator.ROOM_NAMES;
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Deleting a meeting from the meetings list")
    public void deleteMeetingWithSuccess() {
        service.deleteMeeting(testMeeting);
        assertFalse(MeetingsGenerator.MEETINGS.contains(testMeeting));
    }

    @Test
    @DisplayName ("Adding a meeting to the meetings list")
    public void addMeetingsWithSuccess() {
        service.addMeeting(testMeeting);
        assertEquals(4, service.getMeetings().size());
    }

    @Test
    @DisplayName("Room filtering on name 'Mario'")
    public void roomFilteringWithSuccess() {
        List<Meeting> actual = service.roomFilter("Mario");
        assertEquals(1, actual.size());
    }

    @Test
    @DisplayName("Room filtering on a blank constraint")
    public void roomFilteringFailureCase() {
        List<Meeting> actual = service.roomFilter("");
        assertEquals(3, actual.size());
    }

    @Test
    @DisplayName("Filtering on time between 30th november 2020 8:00 and 14h30")
    public void timeFilteringWithSuccess()  {
        long firstDate = parser("30/11/2020 - 08:00");
        long secondDate = parser("30/11/2020 - 14:30");
        List<Meeting> actual = service.timeFilterService(firstDate, secondDate);
        assertEquals(2, actual.size());
    }

    @Test
    @DisplayName("Filtering on null tags")
    public void timeFilteringFailureCase() {
        long firstDate = 0;
        long secondDate = 0;
        List<Meeting> actual = service.timeFilterService(firstDate, secondDate);
        assertEquals(3, actual.size());
    }

    static long parser(String string) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - kk:mm");
        long milliseconds = 0;
        try {
            milliseconds = Objects.requireNonNull(sdf.parse(string)).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return milliseconds;
    }
}