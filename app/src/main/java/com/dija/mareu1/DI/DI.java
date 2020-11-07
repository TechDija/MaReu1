package com.dija.mareu1.DI;

import com.dija.mareu1.service.MeetingApiService;
import com.dija.mareu1.service.MeetingService;

public class DI {

    private static MeetingApiService service = new MeetingService();

    public static MeetingApiService getMeetingApiService() {
        return service;
    }
    public static MeetingApiService getNewInstanceMeetingApiService() {
        return new MeetingService();
    }
}
