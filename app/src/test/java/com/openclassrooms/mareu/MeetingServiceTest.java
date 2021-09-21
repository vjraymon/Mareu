package com.openclassrooms.mareu;

import android.graphics.Color;
import android.util.Log;

import com.openclassrooms.mareu.di.DI;
import com.openclassrooms.mareu.model.Meeting;
import com.openclassrooms.mareu.service.DummyMeetingApiService;
import com.openclassrooms.mareu.service.DummyMeetingGenerator;
import com.openclassrooms.mareu.service.MeetingApiService;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

/**
 * Unit test on Neighbour service
 */
@RunWith(JUnit4.class)
public class
MeetingServiceTest {

    private MeetingApiService service;

    @Before
    public void setup() {
        service = DI.getNewInstanceApiService();
    }

    @Test
    public void getMeetingsWithSuccess() {
        List<Meeting> meetings = service.getFilteredMeetings();
        List<Meeting> expectedMeetings = DummyMeetingGenerator.DUMMY_MEETINGS;
        assertThat(meetings, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedMeetings.toArray()));
    }

    @Test
    public void deleteMeetingWithSuccess() {
        Meeting meetingToCreate;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH.mm");
            meetingToCreate = new Meeting(
                    Color.RED,
                    "SALLE B",
                    "Lyugi",
                    simpleDateFormat.parse("2021.9.6 8.0"),
                    Arrays.asList("jr@gmail.com")
            );
        } catch (Exception e) {
            Log.i("neighbour", "createMeetingWithSuccess exception " + e);
            meetingToCreate = null;
            Assert.fail();
        }
            service.createMeeting(meetingToCreate);
            List<Meeting> meetings = service.getFilteredMeetings();
            Meeting meetingToDelete = meetings.get(meetings.size()-1);
            service.deleteMeeting(meetingToDelete);
            assertFalse(service.getFilteredMeetings().contains(meetingToDelete));
    }

    @Test
    public void createMeetingWithSuccess() {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH.mm");
            Meeting meetingToCreate = new Meeting(
                    Color.RED,
                    "SALLE B",
                    "Lyugi",
                    simpleDateFormat.parse("2021.9.6 8.0"),
                    Arrays.asList("jr@gmail.com")
            );
            service.createMeeting(meetingToCreate);
            Meeting meetingRegistered = service.getFilteredMeetings().get(service.getFilteredMeetings().size()-1);
            assert(service.getFilteredMeetings().contains(meetingToCreate));
            assert(meetingToCreate.getRoom().equals(meetingRegistered.getRoom()));
            assert(meetingToCreate.getDate().equals(meetingRegistered.getDate()));
        } catch (Exception e) {
            Log.i("neighbour", "createMeetingWithSuccess exception " + e);
            Assert.fail();
        }
    }

    @Test
    public void getRoomsDefaultRoomSuccess() {
        List<String> roomsRegistered = service.getRooms();
        assert(roomsRegistered.contains(DummyMeetingApiService.ALL_ROOMS));
    }

    @Test
    public void getRoomsWithOneMoreRoomSuccess() {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH.mm");
            service.createMeeting(new Meeting(
                    Color.RED,
                    "SALLE C",
                    "Lyugi",
                    simpleDateFormat.parse("2021.9.6 8.0"),
                    Arrays.asList("jr@gmail.com")
            ));
            List<String> roomsRegistered = service.getRooms();
            assert(roomsRegistered.contains("SALLE C"));
        } catch (Exception e) {
            Log.i("neighbour", "getRoomsWithOneMoreRoomSuccess exception " + e);
            Assert.fail();
        }
    }

    @Test
    public void getRoomsWithTwoSameRoomSuccess() {
        try {
            int initialRoomsNumber = service.getRooms().size();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH.mm");
            service.createMeeting(new Meeting(
                    Color.RED,
                    "SALLE C",
                    "Lyugi",
                    simpleDateFormat.parse("2021.9.6 8.0"),
                    Arrays.asList("jr@gmail.com")
            ));
            service.createMeeting(new Meeting(
                    Color.RED,
                    "SALLE C",
                    "Lyugi",
                    simpleDateFormat.parse("2021.9.6 9.0"),
                    Arrays.asList("jr@gmail.com")
            ));
            List<String> roomsRegistered = service.getRooms();
            assert((initialRoomsNumber+1) == service.getRooms().size());
        } catch (Exception e) {
            Log.i("neighbour", "getRoomsWithOneMoreRoomSuccess exception " + e);
            Assert.fail();
        }
    }

    @Test
    public void roomFilteringWithSuccess() {
        try {
            String nameRoom = "SALLE B";
            String dateBegin = DummyMeetingApiService.NO_DATE_FILTER;
            String dateEnd = DummyMeetingApiService.NO_DATE_FILTER;
            service.registerFilter(nameRoom, dateBegin, dateEnd);
            int initialMeetingNumber = service.getFilteredMeetings().size();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH.mm");
            Meeting meetingToCreate = new Meeting(
                    Color.RED,
                    nameRoom,
                    "Lyugi",
                    simpleDateFormat.parse("2021.9.6 10.10"),
                    Arrays.asList("jr@gmail.com")
            );
            service.createMeeting(meetingToCreate);
            assert(service.getFilteredMeetings().contains(meetingToCreate));
            assert(service.getFilteredMeetings().size() == (initialMeetingNumber + 1));
        } catch (Exception e) {
            Log.i("neighbour", "getRoomsWithOneMoreRoomSuccess exception " + e);
            Assert.fail();
        }
    }

    @Test
    public void dateBeginFilteringWithSuccess() {
        try {
            String nameRoom = DummyMeetingApiService.ALL_ROOMS;
            String dateBegin = "2021.9.6 10.10";
            String dateEnd = DummyMeetingApiService.NO_DATE_FILTER;
            service.registerFilter(nameRoom, dateBegin, dateEnd);
            int initialMeetingNumber = service.getFilteredMeetings().size();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH.mm");
            //45 minutes duration by default
            Meeting meetingToCreate = new Meeting(
                    Color.RED,
                    "SALLE B",
                    "Lyugi",
                    simpleDateFormat.parse("2021.9.6 9.26"),
                    Arrays.asList("jr@gmail.com")
            );
            service.createMeeting(meetingToCreate);
            assert(service.getFilteredMeetings().contains(meetingToCreate));
            assert(service.getFilteredMeetings().size() == (initialMeetingNumber + 1));
        } catch (Exception e) {
            Log.i("neighbour", "getRoomsWithOneMoreRoomSuccess exception " + e);
            Assert.fail();
        }
    }

    @Test
    public void dateEndFilteringWithSuccess() {
        try {
            String nameRoom = DummyMeetingApiService.ALL_ROOMS;
            String dateBegin = DummyMeetingApiService.NO_DATE_FILTER;
            String dateEnd = "2021.9.6 10.10";
            service.registerFilter(nameRoom, dateBegin, dateEnd);
            int initialMeetingNumber = service.getFilteredMeetings().size();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH.mm");
            Meeting meetingToCreate = new Meeting(
                    Color.RED,
                    "SALLE B",
                    "Lyugi",
                    simpleDateFormat.parse(dateEnd),
                    Arrays.asList("jr@gmail.com")
            );
            service.createMeeting(meetingToCreate);
            assert(service.getFilteredMeetings().contains(meetingToCreate));
            assert(service.getFilteredMeetings().size() == (initialMeetingNumber + 1));
        } catch (Exception e) {
            Log.i("neighbour", "getRoomsWithOneMoreRoomSuccess exception " + e);
            Assert.fail();
        }
    }

    @Test
    public void wholeFilteringWithSuccess() {
        try {
            String nameRoom = "SALLE C";
            String dateBegin = "2021.9.6 10:10";
            String dateEnd = "2021.9.6 12:0";
            service.registerFilter(nameRoom, dateBegin, dateEnd);
            int initialMeetingNumber = service.getFilteredMeetings().size();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
            Meeting meetingRoomOutOfScope = new Meeting(
                    Color.RED,
                    "SALLE B",
                    "Lyugi",
                    simpleDateFormat.parse(dateBegin),
                    Arrays.asList("jr@gmail.com")
            );
            service.createMeeting(meetingRoomOutOfScope);
            //45 minutes duration by default
            Meeting meetingDateBeginOutOfScope = new Meeting(
                    Color.RED,
                    nameRoom,
                    "Lyugi",
                    simpleDateFormat.parse("2021.9.6 9:25"),
                    Arrays.asList("jr@gmail.com")
            );
            service.createMeeting(meetingDateBeginOutOfScope);
            Meeting meetingDateEndOutOfScope = new Meeting(
                    Color.RED,
                    nameRoom,
                    "Lyugi",
                    simpleDateFormat.parse("2021.9.6 12:01"),
                    Arrays.asList("jr@gmail.com")
            );
            service.createMeeting(meetingDateEndOutOfScope);
            Meeting meetingInScope1 = new Meeting(
                    Color.RED,
                    nameRoom,
                    "Lyugi",
                    simpleDateFormat.parse(dateBegin),
                    Arrays.asList("jr@gmail.com")
            );
            service.createMeeting(meetingInScope1);
            Meeting meetingInScope2 = new Meeting(
                    Color.RED,
                    nameRoom,
                    "Lyugi",
                    simpleDateFormat.parse(dateEnd),
                    Arrays.asList("jr@gmail.com")
            );
            service.createMeeting(meetingInScope2);
            assertFalse(service.getFilteredMeetings().contains(meetingRoomOutOfScope));
            assertFalse(service.getFilteredMeetings().contains(meetingDateBeginOutOfScope));
            assertFalse(service.getFilteredMeetings().contains(meetingDateEndOutOfScope));
            assert(service.getFilteredMeetings().contains(meetingInScope1));
            assert(service.getFilteredMeetings().contains(meetingInScope2));
            assert(service.getFilteredMeetings().size() == (initialMeetingNumber + 2));
        } catch (Exception e) {
            Log.i("neighbour", "getRoomsWithOneMoreRoomSuccess exception " + e);
            Assert.fail();
        }
    }

    @Test
    public void isMeetingAlreadyExistsReturnTrue() {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH.mm");
            Meeting meetingToCreate = new Meeting(
                    Color.RED,
                    "SALLE C",
                    "Lyugi",
                    simpleDateFormat.parse("2021.9.6 12.0"),
                    Arrays.asList("jr@gmail.com")
            );
            service.createMeeting(meetingToCreate);
            assert(service.isMeetingAlreadyExists(meetingToCreate));
        } catch (Exception e) {
            Log.i("neighbour", "createMeetingWithSuccess exception " + e);
            Assert.fail();
        }
    }

    @Test
    public void isMeetingAlreadyExistsReturnTrue_collisionBefore() {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH.mm");
            Meeting meetingToCreate = new Meeting(
                    Color.RED,
                    "SALLE C",
                    "Lyugi",
                    simpleDateFormat.parse("2021.9.6 12.0"),
                    Arrays.asList("jr@gmail.com")
            );
            service.createMeeting(meetingToCreate);
            Meeting meetingToCreateBefore = new Meeting(
                    Color.RED,
                    "SALLE C",
                    "Lyugi",
                    simpleDateFormat.parse("2021.9.6 11.16"),
                    Arrays.asList("jr@gmail.com")
            );
            assert(service.isMeetingAlreadyExists(meetingToCreateBefore));
        } catch (Exception e) {
            Log.i("neighbour", "createMeetingWithSuccess exception " + e);
            Assert.fail();
        }
    }

    @Test
    public void isMeetingAlreadyExistsReturnTrue_collisionAfter() {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH.mm");
            Meeting meetingToCreate = new Meeting(
                    Color.RED,
                    "SALLE C",
                    "Lyugi",
                    simpleDateFormat.parse("2021.9.6 12.0"),
                    Arrays.asList("jr@gmail.com")
            );
            service.createMeeting(meetingToCreate);
            Meeting meetingToCreateAfter = new Meeting(
                    Color.RED,
                    "SALLE C",
                    "Lyugi",
                    simpleDateFormat.parse("2021.9.6 12.44"),
                    Arrays.asList("jr@gmail.com")
            );
            assert(service.isMeetingAlreadyExists(meetingToCreateAfter
            ));
        } catch (Exception e) {
            Log.i("neighbour", "createMeetingWithSuccess exception " + e);
            Assert.fail();
        }
    }

    @Test
    public void isMeetingAlreadyExistsReturnErrorFalse() {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH.mm");
            Meeting meetingToCreate = new Meeting(
                    Color.RED,
                    "SALLE C",
                    "Lyugi",
                    simpleDateFormat.parse("2021.9.6 8.0"),
                    Arrays.asList("jr@gmail.com")
            );
            service.createMeeting(meetingToCreate);
            Meeting meetingToCreate2 = new Meeting(
                    Color.RED,
                    "SALLE D",
                    "Lyugi",
                    simpleDateFormat.parse("2021.9.6 8.0"),
                    Arrays.asList("jr@gmail.com")
            );
            assertFalse(service.isMeetingAlreadyExists(meetingToCreate2));
        } catch (Exception e) {
            Log.i("neighbour", "createMeetingWithSuccess exception " + e);
            Assert.fail();
        }
    }
}
