
package com.openclassrooms.mareu.utils;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import android.widget.DatePicker;
import android.widget.TimePicker;

import com.openclassrooms.mareu.R;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.openclassrooms.mareu.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;


/**
 * Test class for list of neighbours
 */

public class AddMeeting {

    public static int ITEMS_COUNT = 0;

    public static void timeWait() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            return;
        }
    }

    public static void timeWait(int i) {
        try {
            Thread.sleep(i);
        } catch (Exception e) {
            return;
        }
    }

    public static void startTest() {
        timeWait(2000);
    }
    public static void resetFilters(){
        // Given : We start on the first activity
        onView(ViewMatchers.withId(R.id.list_meetings))
                .check(matches(ViewMatchers.isDisplayed()));
        // When perform a click on the position 0 of the room filter spinner
        onView(ViewMatchers.withId(R.id.spinnerRoom))
                .perform(click());
        onData(allOf(is(instanceOf(String.class)), is("All rooms")))
                .perform(scrollTo(), click());
        // When perform a click on the date button
        onView(ViewMatchers.withId(R.id.btn_dateClear))
                .perform(click());
        // Then : the number of element is the same
        onView(ViewMatchers.withId(R.id.list_meetings))
                .check(withItemCount(ITEMS_COUNT));
    }

    public static void addMenu() {
        // Given : We start on the first activity
        onView(withId(R.id.list_meetings))
                .check(matches(ViewMatchers.isDisplayed()));
        // When perform a click on a add icon
        onView(allOf(withId(R.id.add_meeting),ViewMatchers.isDisplayed()))
                .perform(click());
        timeWait(2000);
    }

    public static void addItem (String room, int year, int month, int day, int hour, int minute) {
        // Go to Add meeting menu
        addMenu() ;
        // Then : the avatar is displayed
        onView(withId(R.id.avatar))
                .check(matches(ViewMatchers.isDisplayed()));
        // When perform a click on the position "room"" of the room spinner
        onView(allOf(ViewMatchers.withId(R.id.spinnerRoomInput), ViewMatchers.isDisplayed()))
                .perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is(room)))
                .perform(scrollTo(), click());
        // check "SALLE C"
        onView(withId(R.id.spinnerRoomInput))
                .check(matches(ViewMatchers.withSpinnerText(containsString(room))));
        // When perform a click on the date button
        onView(ViewMatchers.withId(R.id.btn_date))
                .perform(scrollTo(), click());
        // When we enter a date
        onView(isAssignableFrom(DatePicker.class))
                .perform(PickerActions.setDate(year, month, day));
        onView(ViewMatchers.withId(android.R.id.button1))
                .perform(click());
        // When perform a click on the time button
        onView(ViewMatchers.withId(R.id.btn_time))
                .perform(scrollTo(), click());
        // When we enter a time
        onView(isAssignableFrom(TimePicker.class))
                .perform(PickerActions.setTime(hour, minute));
        onView(ViewMatchers.withId(android.R.id.button1))
                .perform(click());
        // Click on the button save
        onView(ViewMatchers.withId(R.id.create))
                .perform(scrollTo(), click());
        timeWait(2000);
        onView(ViewMatchers.withId(R.id.list_meetings))
                .check(matches(ViewMatchers.isDisplayed()));
        // Then : the number of element is incremented by 1
        onView(ViewMatchers.withId(R.id.list_meetings))
                .check(withItemCount(ITEMS_COUNT + 1));
        ITEMS_COUNT = ITEMS_COUNT + 1;
    }
}