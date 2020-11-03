package com.dija.mareu1.Service;

import com.dija.mareu1.Model.Meeting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

public abstract class MeetingsGenerator {

    static List<Meeting> MEETINGS = Arrays.asList(
            new Meeting("oct 31, 2020", "14:00", "14:45", "Peach", "Réunion A", "maxime@lamzone.com, alex@lamzone.com"),
            new Meeting("oct 31, 2020", "8:00", "8:45",  "Mario", "Réunion B", "paul@lamzone.com, viviane@lamzone.com"),
            new Meeting("oct 31, 2020", "16:00", "16:45", "Luigi", "Réunion C", "amandine@lamzone.com, luc@lamzone.com") );


    static List<String> ROOMS = Arrays.asList(
            "Choix de la salle", "Mario", "Luigi", "Bowser", "Peach", "Daisy", "Koopa", "Donkey-Kong", "Wario", "Toad", "Yoshi");


    static List<Meeting> generateMeetings(){
        return new ArrayList<>(MEETINGS);
    }
    static List<String> generateRooms() {return new ArrayList<>(ROOMS); }
}
