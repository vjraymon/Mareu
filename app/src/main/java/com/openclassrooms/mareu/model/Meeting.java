package com.openclassrooms.mareu.model;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.openclassrooms.mareu.service.DummyMeetingGenerator;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Model object representing a Neighbour
 */
public class Meeting {

    /** Identifier */
    private long id;

    private int color;
    /** Full name */
    private String room;

    /** Avatar */
    private Date date;

    private long duration;

    /** Adress */
    private List<String> emails;

    /**
     * Constructor
     * @param id
     * @param color
     * @param room
     * @param date
     * @param emails
     */
    public Meeting(int color, String room, Date date, List<String> emails) {
        this.id = 0; // should be updated when added to the list of meetings
        this.color = color;
        this.room = room;
        this.date = date;
        this.emails = emails;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) { this.color = color; }

    public String getRoom() { return room; }

    public void setRoom(String room) {
        this.room = room;
    }

    public Date getDate() { return date; }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<String> getEmails() { return emails; }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meeting meeting = (Meeting) o;
        return (Objects.equals(room, meeting.room) && Objects.equals(date, meeting.date));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
