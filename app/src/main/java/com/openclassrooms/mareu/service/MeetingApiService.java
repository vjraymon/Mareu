package com.openclassrooms.mareu.service;

import com.openclassrooms.mareu.model.Meeting;

import java.util.Date;
import java.util.List;


/**
 * Neighbour API client
 */
public interface MeetingApiService {

    /**
     * Get all my Meetings
     * @return {@link List}
     */
    List<Meeting> getFilteredMeetings();

    /**
     * Deletes a meeting
     * @param meeting
     */
    void deleteMeeting(Meeting meeting);

    /**
     * Create a meeting
     * @param meeting
     */
    void createMeeting(Meeting meeting);

    /**
     * Register filter
     * @param nameRoom
     */
    void registerFilter(String nameRoom, String dateBegin, String dateEnd);

    /**
     * {@inheritDoc}
     */
    List<String> getRooms();

    /**
     *
     */
    void restore();
}
