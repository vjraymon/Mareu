
package com.openclassrooms.mareu.meeting_list;

import android.app.UiAutomation;
import android.content.pm.ActivityInfo;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.appcompat.app.AppCompatActivity;
import androidx.test.rule.ActivityTestRule;

import android.widget.DatePicker;
import android.widget.TimePicker;

import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.ui.meeting_list.ListMeetingActivity;
import com.openclassrooms.mareu.utils.DeleteViewAction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.openclassrooms.mareu.utils.AddMeeting.ITEMS_COUNT;
import static com.openclassrooms.mareu.utils.AddMeeting.addItem;
import static com.openclassrooms.mareu.utils.AddMeeting.addMenu;
import static com.openclassrooms.mareu.utils.AddMeeting.resetFilters;
import static com.openclassrooms.mareu.utils.AddMeeting.startTest;
import static com.openclassrooms.mareu.utils.AddMeeting.timeWait;
import static com.openclassrooms.mareu.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

/**
 * Test class for list of neighbours
 */

@RunWith(AndroidJUnit4.class)
public class AddDeleteMeetingTest {

    private AppCompatActivity mActivity;

    @Rule
    public ActivityScenarioRule<ListMeetingActivity> mActivityRule =
            new ActivityScenarioRule(ListMeetingActivity.class);

    @Before
    public void setUp() {
        ITEMS_COUNT = 0;
    }

    /**
     * We ensure that our recyclerview is displaying at least on item
     */
    @Test
    public void myMeetingsList_shouldBeDisplayed() {
        startTest();
        // First scroll to the position that needs to be matched and click on it.
        onView(allOf(withId(R.id.list_meetings),ViewMatchers.isDisplayed()))
                .check(withItemCount(ITEMS_COUNT));
    }

    /**
     * When we delete an item, the item is no more shown
     */
    @Test
    public void myMeetingsList_deleteAction_shouldRemoveItem() {
        startTest();
        // Given : We check the current number of item
        onView(allOf(withId(R.id.list_meetings),ViewMatchers.isDisplayed()))
                .check(withItemCount(ITEMS_COUNT));
        // add a meeting
        addItem("SALLE B", 2021, 9, 6, 14, 0);
        // When perform a click on a delete icon of the latest position
        onView(allOf(withId(R.id.list_meetings),ViewMatchers.isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(ITEMS_COUNT - 1, new DeleteViewAction()));
        // Then : the number of element is decremented by 1
        onView(allOf(withId(R.id.list_meetings),ViewMatchers.isDisplayed()))
                .check(withItemCount(ITEMS_COUNT-1));
        ITEMS_COUNT = ITEMS_COUNT - 1;
    }

    /**
     * When we add an item, a new activity is shown
     */
    @Test
    public void myMeetingsList_addItem() {
        startTest();
        // Go to Add meeting menu
        addMenu();
        // Then : the avatar is displayed
        onView(withId(R.id.avatar))
                .check(matches(ViewMatchers.isDisplayed()));
        // Check the button add is still disabled
        onView(withId(R.id.create))
                .check(matches(not(ViewMatchers.isEnabled())));
    }

    /**
     * When we add an item and input a room the button add is still disabled
     */
    @Test
    public void myMeetingsList_addItemInputRoom() {
        startTest();
        // Go to Add meeting menu
        addMenu();
        // Then : enter "SALLE C"
        onView(withId(R.id.spinnerRoomInput))
                .perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is("SALLE C")))
                .perform(scrollTo(), click());
        // check "SALLE C"
        onView(withId(R.id.spinnerRoomInput))
                .check(matches(ViewMatchers.withSpinnerText(containsString("SALLE C"))));
        // Check the button add is still disabled
        onView(withId(R.id.create))
                .check(matches(not(ViewMatchers.isEnabled())));
    }

    /**
     * When we add an item and input the date the button add is still disabled
     * */
    @Test
    public void myMeetingsList_addItemInputDate() {
        startTest();
        // Go to Add meeting menu
        addMenu();
        // When perform a click on the date button
        onView(withId(R.id.btn_date))
                .perform(click());
        // When we enter a date
        onView(isAssignableFrom(DatePicker.class))
                .perform(PickerActions.setDate(2021, 9, 6));
        onView(withId(android.R.id.button1))
                .perform(click());
        // check date
        onView(withId(R.id.in_date))
                .check(matches(ViewMatchers.withText("2021.9.6")));
        // Check the button add is still disabled
        onView(withId(R.id.create))
                .check(matches(not(ViewMatchers.isEnabled())));
    }

    /**
     * When we add an item and input the time the button add is still disabled
     */
    @Test
    public void myMeetingsList_addItemInputTime() {
        startTest();
        // Go to Add meeting menu
        addMenu();
        // When perform a click on the time button
        onView(allOf(withId(R.id.btn_time)))
                .perform(scrollTo(), click());
        // When we enter a time
        onView(isAssignableFrom(TimePicker.class))
                .perform(PickerActions.setTime(14,0));
        onView(withId(android.R.id.button1))
                .perform(click());
        // check time
        onView(withId(R.id.in_time))
                .check(matches(ViewMatchers.withText("14:0")));
        // Check the button add is still disabled
        onView(withId(R.id.create))
                .check(matches(not(ViewMatchers.isEnabled())));
    }

    /**
     * When we add an item and input the room, date and time the button add becomes enabled
     */
    @Test
    public void myMeetingsList_addItemWholeInput() {
        startTest();
        // Go to Add meeting menu
        addMenu();
        // Then : enter Subject
        onView(withId(R.id.subject))
                .perform(typeText("Essai SALLE C"), closeSoftKeyboard());
        // check "SALLE C"
        onView(withId(R.id.subject))
                .check(matches(ViewMatchers.withText("Essai SALLE C")));
        timeWait();
        // Then : enter "SALLE C"
        onView(withId(R.id.spinnerRoomInput))
                .perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), containsString("SALLE C")))
                .perform(scrollTo(),
                        click());
        // check "SALLE C"
        onView(withId(R.id.spinnerRoomInput))
                .check(matches(ViewMatchers.withSpinnerText(containsString("SALLE C"))));
        // When perform a click on the date button
        onView(allOf(withId(R.id.btn_date),ViewMatchers.isDisplayed()))
                .perform(click());
        // When we enter a date
        onView(isAssignableFrom(DatePicker.class))
                .perform(PickerActions.setDate(2021, 9, 6));
        onView(withId(android.R.id.button1))
                .perform(click());
        // check date
        onView(withId(R.id.in_date))
                .check(matches(ViewMatchers.withText("2021.9.6")));
        // When perform a click on the time button
        onView(allOf(withId(R.id.btn_time),ViewMatchers.isDisplayed()))
                .perform(click());
        // When we enter a time
        onView(isAssignableFrom(TimePicker.class))
                .perform(PickerActions.setTime(14,0));
        onView(withId(android.R.id.button1))
                .perform(click());
        // check time
        onView(withId(R.id.in_time))
                .check(matches(ViewMatchers.withText("14:0")));
        // Check the button add is still disabled
        onView(withId(R.id.create))
                .check(matches(ViewMatchers.isEnabled()));
    }

    /**
     * When we add an item and input the room, date and time the button add becomes enabled
     */
    @Test
    public void myMeetingsList_addMeeting() {
        startTest();
        // Given : We start on the first activity
        onView(withId(R.id.list_meetings))
                .check(matches(ViewMatchers.isDisplayed()));
        // add a meeting
        addItem("SALLE C", 2021, 9, 6, 14, 0);
        // Check the return to the main activity
        onView(withId(R.id.list_meetings))
                .check(matches(ViewMatchers.isDisplayed()));
        // Then : the number of element is incremented by 1
        onView(allOf(withId(R.id.list_meetings),ViewMatchers.isDisplayed()))
                .check(withItemCount(ITEMS_COUNT));
    }
}