package com.openclassrooms.mareu.service;

import android.graphics.Color;

import com.openclassrooms.mareu.model.Meeting;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public abstract class

DummyMeetingGenerator {
 static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd hh.mm");
 static ParsePosition pos1 = new ParsePosition(0);
    static ParsePosition pos2 = new ParsePosition(0);

    public static int DUMMY_COLORS[] = {Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.WHITE, Color.BLACK};
    public static List<Meeting> DUMMY_MEETINGS = Arrays.asList(
        new Meeting(DUMMY_COLORS[0],"SALLE A", simpleDateFormat.parse("2021.08.23 05.30", pos1), Arrays.asList(new String("caroline@gmail.com"))),
        new Meeting(DUMMY_COLORS[3],"SALLE B", simpleDateFormat.parse("2021.08.23 06.30", pos2), Arrays.asList(new String("jack@gmail.com"),
                new String("helene@gmail.com"), new String("lea@gmail.com")))
    );

    static List<Meeting> generateMeetings() {
        ArrayList<Meeting> meetings = new ArrayList<Meeting>(DUMMY_MEETINGS);
        return meetings;
    }

    public static int generateColor(int i) {
        return DUMMY_COLORS[new Random().nextInt(DUMMY_COLORS.length)];
    }
}
