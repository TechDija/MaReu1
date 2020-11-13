package com.dija.mareu1.model;

public class Room {
    private String roomName;
    private String roomImage;

    public Room(String roomName, String roomImage) {
        this.roomName = roomName;
        this.roomImage = roomImage;

    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomImage() {
        return roomImage;
    }

    public void setRoomImage(String roomImage) {
        this.roomImage = roomImage;
    }
}
