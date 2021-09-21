
package com.openclassrooms.mareu.meeting_list;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.DatePicker;

import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.ui.meeting_list.ListMeetingActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static com.openclassrooms.mareu.utils.AddMeeting.ITEMS_COUNT;
import static com.openclassrooms.mareu.utils.AddMeeting.addItem;
import static com.openclassrooms.mareu.utils.AddMeeting.resetFilters;
import static com.openclassrooms.mareu.utils.AddMeeting.startTest;
import static com.openclassrooms.mareu.utils.AddMeeting.timeWait;
import static com.openclassrooms.mareu.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;


/**
 * Test class for list of neighbours
 */

@RunWith(AndroidJUnit4.class)
public class FilterMeetingTest {

    private AppCompatActivity mActivity;

    @Rule
    public ActivityScenarioRule<ListMeetingActivity> mActivityRule =
            new ActivityScenarioRule(ListMeetingActivity.class);

    @Before
    public void setUp() {
        ITEMS_COUNT = 0;
    }

    /**
     * When we add an item and define a room filter All
     */
    @Test
    public void myMeetingsList_roomfilterAll() {
        startTest();
        // reset the filters
        resetFilters();
        // add a meeting
        addItem("SALLE D", 2021, 9, 6, 14, 0);
        // Check the return to the main activity
        onView(ViewMatchers.withId(R.id.list_meetings))
                .check(matches(ViewMatchers.isDisplayed()));
        // When perform a click on the position 0 of the room filter spinner
        onView(ViewMatchers.withId(R.id.spinnerRoom))
                .perform(click());
        onData(allOf(is(instanceOf(String.class)), is("All rooms")))
                .perform(scrollTo(), click());

        onView(ViewMatchers.withId(R.id.spinnerRoom))
                .check(matches(ViewMatchers.withSpinnerText(containsString("All rooms"))));
        // Then : the number of element is the same
        onView(ViewMatchers.withId(R.id.list_meetings))
                .check(withItemCount(ITEMS_COUNT));
    }

    /**
     * When we add an item and define a room filter
     */
    @Test
    public void myMeetingsList_roomfilter() {
        startTest();
        // reset the filters
        resetFilters();
        // add a meeting
        addItem("SALLE E", 2021, 9, 6, 14, 0);
        // Check the return to the main activity
        onView(ViewMatchers.withId(R.id.list_meetings))
                .check(matches(ViewMatchers.isDisplayed()));
        // When perform a click on the position 0 SALLE E of the room filter spinner
        onView(ViewMatchers.withId(R.id.spinnerRoom))
                .perform(click());
        onData(allOf(is(instanceOf(String.class)), is("SALLE E")))
                .perform(scrollTo(), click());

        onView(ViewMatchers.withId(R.id.spinnerRoom))
                .check(matches(ViewMatchers.withSpinnerText(containsString("SALLE E"))));
        // Then : the number of element is the same
        onView(ViewMatchers.withId(R.id.list_meetings))
                .check(withItemCount(1));
    }

    /**
     * When we add an item and define a date filter
     */
    @Test
    public void myMeetingsList_datefilter() {
        startTest();
        // reset the filters
        resetFilters();
        // add a meeting
        addItem("SALLE F", 2021, 9, 6, 23, 15);
        addItem("SALLE G", 2021, 9, 7, 0, 0);
        // duration 45 minutes, so it also belongs to next day
        addItem("SALLE H", 2021, 9, 7, 23, 59);
        addItem("SALLE I", 2021, 9, 8, 0, 44);
        // Check the return to the main activity
        onView(ViewMatchers.withId(R.id.list_meetings))
                .check(matches(ViewMatchers.isDisplayed()));
        // When perform a click on the date button
        onView(ViewMatchers.withId(R.id.btn_dateFilter))
                .perform(click());
        // When we enter a date
        onView(isAssignableFrom(DatePicker.class))
                .perform(PickerActions.setDate(2021, 9, 7));
        onView(ViewMatchers.withId(android.R.id.button1))
                .perform(click());
        // check date
        onView(ViewMatchers.withId(R.id.in_dateFilter))
                .check(matches(ViewMatchers.withText("2021.9.7")));
        // Then : the number of element is the same
        onView(ViewMatchers.withId(R.id.list_meetings))
                .check(withItemCount(2));
    }

    /**
     * When we add an item and define a date filter
     */
    @Test
    public void myMeetingsList_cleardatefilter() {
        startTest();
        // reset the filters
        resetFilters();
        // add a meeting
        addItem("SALLE G", 2021, 9, 6, 23, 15);
        addItem("SALLE G", 2021, 9, 7, 0, 0);
        addItem("SALLE G", 2021, 9, 7, 23, 59);
        addItem("SALLE G", 2021, 9, 8, 0, 44);
        // Check the return to the main activity
        onView(ViewMatchers.withId(R.id.list_meetings))
                .check(matches(ViewMatchers.isDisplayed()));
        // When perform a click on the date button
        onView(ViewMatchers.withId(R.id.btn_dateFilter))
                .perform(click());
        // When we enter a date
        onView(isAssignableFrom(DatePicker.class))
                .perform(PickerActions.setDate(2021, 9, 7));
        onView(ViewMatchers.withId(android.R.id.button1))
                .perform(click());
        // check date
        onView(ViewMatchers.withId(R.id.in_dateFilter))
                .check(matches(ViewMatchers.withText("2021.9.7")));
        // When perform a click on the date button
        onView(ViewMatchers.withId(R.id.btn_dateClear))
                .perform(click());
        // check date
        onView(ViewMatchers.withId(R.id.in_dateFilter))
                .check(matches(ViewMatchers.withText("All dates")));
        // Then : the number of element is the same
        onView(ViewMatchers.withId(R.id.list_meetings))
                .check(withItemCount(ITEMS_COUNT));
    }

    /**
     * When we add an item and define a date filter
     */
    @Test
    public void myMeetingsList_wholefilter() {
        startTest();
        // reset the filters
        resetFilters();
        // add a meeting
        addItem("SALLE H", 2021, 9, 9, 14, 30);
        addItem("SALLE I", 2021, 9, 8, 14, 0);
        addItem("SALLE I", 2021, 9, 9, 14, 0);
        addItem("SALLE I", 2021, 9, 9, 15, 0);
        // Check the return to the main activity
        onView(ViewMatchers.withId(R.id.list_meetings))
                .check(matches(ViewMatchers.isDisplayed()));
        // When perform a click on the position 0 SALLE I of the room filter spinner
        onView(ViewMatchers.withId(R.id.spinnerRoom))
                .perform(click());
        onData(allOf(is(instanceOf(String.class)), is("SALLE I")))
                .perform(scrollTo(), click());

        onView(ViewMatchers.withId(R.id.spinnerRoom))
                .check(matches(ViewMatchers.withSpinnerText(containsString("SALLE I"))));
        // When perform a click on the date button
        onView(ViewMatchers.withId(R.id.btn_dateFilter))
                .perform(click());
        // When we enter a date
        onView(isAssignableFrom(DatePicker.class))
                .perform(PickerActions.setDate(2021, 9, 9));
        onView(ViewMatchers.withId(android.R.id.button1))
                .perform(click());
        // check date
        onView(ViewMatchers.withId(R.id.in_dateFilter))
                .check(matches(ViewMatchers.withText("2021.9.9")));
        // Then : the number of element is 2
        onView(ViewMatchers.withId(R.id.list_meetings))
                .check(withItemCount(2));
    }
}