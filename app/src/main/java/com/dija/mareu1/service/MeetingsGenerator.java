package com.dija.mareu1.service;

import android.annotation.SuppressLint;

import com.dija.mareu1.model.Meeting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class MeetingsGenerator {

    @SuppressLint("SimpleDateFormat")
    static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - kk:mm");


    public final static List<Meeting> MEETINGS = Arrays.asList(
            new Meeting(parser("30/11/2020 - 14:00"), parser("30/11/2020 - 14:45"), "Peach", "Réunion A", "maxime@lamzone.com, alex@lamzone.com, khadidjatou@lamzone.com"),
            new Meeting(parser("30/11/2020 - 08:00"), parser("30/11/2020 - 08:45"), "Mario", "Réunion B", "paul@lamzone.com, viviane@lamzone.com"),
            new Meeting(parser("30/11/2020 - 16:00"), parser("30/11/2020 - 16:45"), "Luigi", "Réunion C", "amandine@lamzone.com, luc@lamzone.com"));


    static List<Meeting> generateMeetings() {
        return new ArrayList<>(MEETINGS);
    }

    static long parser(String string) {
        long milliseconds = 0;
        try {
            milliseconds = Objects.requireNonNull(sdf.parse(string)).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return milliseconds;
    }
}
