package com.dija.mareu1.service;

import com.dija.mareu1.model.Room;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoomGenerator {

    public final static List<Room> ROOMS = Arrays.asList(
            new Room("Mario", "pastille_rouge"),
            new Room("Luigi", "pastille_vert"),
            new Room("Peach", "pastel_rose"),
            new Room("Bowser", "pastille_noire"),
            new Room("Daisy", "pastille_mauve"),
            new Room("Koopa", "pastel_vert"),
            new Room("Donkey-Kong", "pastille_marron"),
            new Room("Wario", "pastille_jaune"),
            new Room("Toad", "pastille_orange"),
            new Room("Yoshi", "pastel_bleu"));


    public final static List<String> ROOM_NAMES = Arrays.asList(
            "Mario", "Luigi", "Bowser", "Peach", "Daisy", "Koopa", "Donkey-Kong", "Wario", "Toad", "Yoshi");

    static List<Room> generateRooms() {
        return new ArrayList<>(ROOMS);
    }

    static List<String> generateRoomNames() {
        return new ArrayList<>(ROOM_NAMES);
    }


}
