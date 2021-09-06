package com.openclassrooms.mareu.service;

import android.graphics.Color;
import android.util.Log;

import com.openclassrooms.mareu.model.Meeting;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Dummy mock for the Api
 */
public class DummyMeetingApiService implements MeetingApiService {

    private List<Meeting> meetings = DummyMeetingGenerator.generateMeetings();
    private List<Meeting> filteredMeetings = new ArrayList<Meeting>();
    static public String ALL_ROOMS = "All rooms";
    private String roomFilter = DummyMeetingApiService.ALL_ROOMS;
    static public String NO_DATE_FILTER = "All dates";
    private String dateBeginFilter = DummyMeetingApiService.NO_DATE_FILTER;
    private String dateEndFilter = DummyMeetingApiService.NO_DATE_FILTER;

    @Override
    public void restore() {
        ALL_ROOMS = "All rooms";
        NO_DATE_FILTER = "All dates";
        meetings = DummyMeetingGenerator.generateMeetings();
        filteredMeetings = new ArrayList<Meeting>();
        roomFilter = DummyMeetingApiService.ALL_ROOMS;
        dateBeginFilter = DummyMeetingApiService.NO_DATE_FILTER;
        dateEndFilter = DummyMeetingApiService.NO_DATE_FILTER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Meeting> getFilteredMeetings() {
        Log.i("neighbour","DummyMeetingApiService.getFilteredMeetings " + roomFilter);
        filteredMeetings.clear();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH.mm");
        Date dateBegin = null;
        try {
            dateBegin = simpleDateFormat.parse(dateBeginFilter);
        }
        catch (Exception e) {
            Log.i("neighbour", "DummyMeetingApiService.getFilteredMeetings() exception " + e);
            dateBeginFilter = NO_DATE_FILTER;
        }
        Date dateEnd = null;
        try {
            dateEnd = simpleDateFormat.parse(dateEndFilter);
        }
        catch (Exception e) {
            Log.i("neighbour", "DummyMeetingApiService.getFilteredMeetings() exception " + e);
            dateEndFilter = NO_DATE_FILTER;
        }
        for(Meeting meeting : meetings) {
            Log.i("neighbour","DummyMeetingApiService.getFilteredMeetings meeting.getRoom() " + meeting.getRoom() + " " + meeting.getId());
            if ((roomFilter.equals(ALL_ROOMS) || roomFilter.equals(meeting.getRoom()))
               && (dateBeginFilter.equals(NO_DATE_FILTER) || (meeting.getDate().after(dateBegin)) || (meeting.getDate().equals(dateBegin)))
               && (dateEndFilter.equals(NO_DATE_FILTER) || (meeting.getDate().before(dateEnd))) || (meeting.getDate().equals(dateEnd)))
            {
                filteredMeetings.add(meeting);
            }
        }
        return filteredMeetings;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteMeeting(Meeting meeting) {
        meetings.remove(meeting);
    }

     /**
     * {@inheritDoc}
     * @param meeting
     */
    @Override
    public void createMeeting(Meeting meeting) {
        meeting.setId(generateId());
        Log.i("neighbour","DummyMeetingApiService.registerFilter create " + meeting.getRoom() + " " + meeting.getId());

        meetings.add(meeting);
    }
    private Boolean isIdFound(long id) {
        for (Meeting m : meetings) {
            if (id == m.getId()) return true;
        }
        return false;
    }
    private long generateId() {
        for (long i=0; ; i++) {
            if (!isIdFound(i)) return i;
        }
    }
    /**
     * {@inheritDoc}
     * @param nameRoom
     */
    @Override
    public void registerFilter(String nameRoom, String dateBegin, String dateEnd) {
        roomFilter = nameRoom;
        dateBeginFilter = dateBegin;
        dateEndFilter = dateEnd;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getRooms()
    {
        List<String> rooms = new ArrayList<>();
        rooms.add(ALL_ROOMS);
        for (Meeting meeting : meetings) {
            if (!isRoomFound(meeting.getRoom(), rooms)) rooms.add(meeting.getRoom());
        }
        return rooms;
    }
    private Boolean isRoomFound(String room, List<String> rooms) {
        for (String r : rooms) {
            if (r.equals(room)) return true;
        }
        return false;
    }


}
