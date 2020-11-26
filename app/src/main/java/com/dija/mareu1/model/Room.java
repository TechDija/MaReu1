package com.dija.mareu1.model;

public class Room {
    private String roomName;
    private String roomImage;
    private boolean roomTag = false;

    public Room(String roomName, String roomImage) {
        this.roomName = roomName;
        this.roomImage = roomImage;
        this.roomTag = roomTag;
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

    public boolean getRoomTag() {
        return roomTag;
    }

    public void setRoomTag(boolean roomTag) {
        this.roomTag = roomTag;
    }
}
