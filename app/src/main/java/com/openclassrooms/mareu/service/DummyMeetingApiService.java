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
    // Meeting lists
    private List<Meeting> meetings = DummyMeetingGenerator.generateMeetings();
    private List<Meeting> filteredMeetings = new ArrayList<Meeting>();
    // Room filering
    static public String ALL_ROOMS = "All rooms";
    private String roomFilter = DummyMeetingApiService.ALL_ROOMS;
    // Date filtering
    static public String NO_DATE_FILTER = "All dates";
    private String dateBeginFilter = DummyMeetingApiService.NO_DATE_FILTER;
    private String dateEndFilter = DummyMeetingApiService.NO_DATE_FILTER;
    static public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    static public String convertyyyyMMddToString(int year, int month, int day) { return year + "." + month + "." + day; }
    static public String convertHHmmToString(int hour, int minute) { return hour + ":" + minute; }
    static public String convertDateAndTimeToString(String stringDate, String stringTime) { return stringDate + " " + stringTime; }
    /*
    Constructor: general list and filters reset
    */
    public DummyMeetingApiService() {
        Log.i("neighbour","DummyMeetingApiService.DummyMeetingApiService ");
        meetings = DummyMeetingGenerator.generateMeetings();
        for (Meeting meeting : meetings) {
            Log.i("neighbour","DummyMeetingApiService.DummyMeetingApiService = " + meeting.getRoom());
        }
        roomFilter = DummyMeetingApiService.ALL_ROOMS;
        dateBeginFilter = DummyMeetingApiService.NO_DATE_FILTER;
        dateEndFilter = DummyMeetingApiService.NO_DATE_FILTER;
        filteredMeetings = new ArrayList<Meeting>();
    }

    /*
    Generic method to compute a filtered list from the general meeting list and filters input
    It retrieves all the meetings which fits the room,
    and whose (date, duration = 45 minutes) partially covers an interval of date (begin, end)
    */
    private List<Meeting> getLocalFilteredMeetings(String roomFilter, String dateBeginFilter, String dateEndFilter) {
        List<Meeting> filteredMeetings = new ArrayList<Meeting>();
        Log.i("neighbour","DummyMeetingApiService.getFilteredMeetings " + roomFilter + " " + dateBeginFilter + " " + dateEndFilter + " " + filteredMeetings.size());
        // convert String dateBeginFilter into cBegin
        ConvertStringToDate cBegin = new ConvertStringToDate(dateBeginFilter);
        // convert String dateEndFilter into cEnd
        ConvertStringToDate cEnd = new ConvertStringToDate(dateEndFilter);
        // Initialization to a dummy value in order to call setTime
        ConvertStringToDate meetingDateEnd = new ConvertStringToDate("2021.9.21 5:45");
        // for all the meetings of the general list:
        for(Meeting meeting : meetings) {
            Log.i("neighbour","DummyMeetingApiService.getFilteredMeetings meeting.getRoom() " + meeting.getRoom() + " " + roomFilter + " " + dateBeginFilter + " " + dateEndFilter);
            // convert the date of begin of the current meeting + its duration into meetingDateEnd
            if (!cBegin.string.equals(NO_DATE_FILTER)) meetingDateEnd.date.setTime(meeting.getDate().getTime() + meeting.getDuration());
            // Check if the room of the current meeting fits the room filter
            if (((roomFilter.equals(ALL_ROOMS)) || (meeting.getRoom().equals(roomFilter)))
            // Check if the meetingDateEnd >= date begin filter
                    && ((cBegin.string.equals(NO_DATE_FILTER)) || (meetingDateEnd.date.compareTo(cBegin.date)>0))
            // Check if the Date begin of the current meeting <= date end filter
                    && ((cEnd.string.equals(NO_DATE_FILTER)) || (meeting.getDate().compareTo(cEnd.date)<=0)))
            {
                // if all the conditions are successfull, adds the current meeting to the filtered list
                filteredMeetings.add(meeting);
            }
        }
        return filteredMeetings;
    }

    // Conversion utility
    private class ConvertStringToDate {
        public String string;
        public Date date;
        public ConvertStringToDate(String s) {
            try {
                date = simpleDateFormat.parse(s);
                string = s;
            }
            catch (Exception e) {
                Log.i("neighbour", "DummyMeetingApiService.getFilteredMeetings() ConvertDate exception " + e);
                date = null;
                string = NO_DATE_FILTER;
            }
        }
    }

    @Override
    public boolean isMeetingAlreadyExists(Meeting meeting) {
        try {
            // compute the filtered list with the filers: (room, date begin, date begin + duration - 1 minute) from the meeting to check
            String stringDate = simpleDateFormat.format(meeting.getDate());
            Date dateEnd = simpleDateFormat.parse(stringDate);
            dateEnd.setTime(meeting.getDate().getTime() + meeting.getDuration() - 60000);
            List<Meeting> filteredMeetings = getLocalFilteredMeetings(meeting.getRoom(), stringDate, simpleDateFormat.format(dateEnd));
            // if the resulting list is empty, then the checked meeting have no collision with any already registered meeting
            return (filteredMeetings.size() > 0);
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Meeting> getFilteredMeetings() {
        return getLocalFilteredMeetings(roomFilter, dateBeginFilter, dateEndFilter);
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
        meeting.setId(generateId()); // automatic generation of an unique id from the "holes" in the general meeting list
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
        // Creation of the list of the already occupied rooms from the general meeting list
        // (the final list have at least "All rooms" tag and no duplicated items)
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
