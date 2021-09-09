
package com.openclassrooms.mareu.meeting_list;

import android.content.pm.ActivityInfo;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.app.AppCompatActivity;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.ui.meeting_list.ListMeetingActivity;
import com.openclassrooms.mareu.utils.DeleteViewAction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static com.openclassrooms.mareu.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsNull.notNullValue;



/**
 * Test class for list of neighbours
 */

@RunWith(AndroidJUnit4.class)
public class MeetingsListTest {

    // This is fixed
    private static int ITEMS_COUNT = 2;

    private AppCompatActivity mActivity;

    @Rule
    public ActivityTestRule<AppCompatActivity> mActivityRule =
            new ActivityTestRule(ListMeetingActivity.class);

    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
/*
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        try {
            Thread.sleep(50);
        } catch (Exception e) {
            return;
        }
*/
    }

    void addItem (String room, int year, int month, int day, int hour, int minute) {
        // When perform a click on a add icon
        onView(allOf(ViewMatchers.withId(R.id.add_meeting)))
                .perform(click());
        // Enter room
        onView(ViewMatchers.withId(R.id.room))
                .perform(typeText(room), closeSoftKeyboard());
        // When perform a click on the date button
        onView(allOf(ViewMatchers.withId(R.id.btn_date), ViewMatchers.isDisplayed()))
                .perform(click());
        // When we enter a date
        onView(isAssignableFrom(DatePicker.class))
                .perform(PickerActions.setDate(year, month, day));
        onView(ViewMatchers.withId(android.R.id.button1))
                .perform(click());
        // When perform a click on the time button
        onView(allOf(ViewMatchers.withId(R.id.btn_time), ViewMatchers.isDisplayed()))
                .perform(click());
        // When we enter a time
        onView(isAssignableFrom(TimePicker.class))
                .perform(PickerActions.setTime(hour, minute));
        onView(ViewMatchers.withId(android.R.id.button1))
                .perform(click());
        // Click on the button add
        onView(ViewMatchers.withId(R.id.create))
                .perform(click());
        onView(ViewMatchers.withId(R.id.list_meetings))
                .check(matches(ViewMatchers.isDisplayed()));
        // Then : the number of element is incremented by 1
        onView(allOf(ViewMatchers.withId(R.id.list_meetings), ViewMatchers.isDisplayed()))
                .check(withItemCount(ITEMS_COUNT + 1));
        ITEMS_COUNT = ITEMS_COUNT + 1;
    }

    /**
     * We ensure that our recyclerview is displaying at least on item
     */
    @Test
    public void myMeetingsList_shouldNotBeEmpty() {
        // First scroll to the position that needs to be matched and click on it.
        onView(allOf(ViewMatchers.withId(R.id.list_meetings),ViewMatchers.isDisplayed()))
                .check(matches(hasMinimumChildCount(1)));
    }

    /**
     * When we delete an item, the item is no more shown
     */
    @Test
    public void myMeetingsList_deleteAction_shouldRemoveItem() {
        // Given : We remove the element at position 1
        onView(allOf(ViewMatchers.withId(R.id.list_meetings),ViewMatchers.isDisplayed()))
                .check(withItemCount(ITEMS_COUNT));
        // When perform a click on a delete icon
        onView(allOf(ViewMatchers.withId(R.id.list_meetings),ViewMatchers.isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));
        // Then : the number of element is decremented by 1
        onView(allOf(ViewMatchers.withId(R.id.list_meetings),ViewMatchers.isDisplayed()))
                .check(withItemCount(ITEMS_COUNT-1));
        ITEMS_COUNT = ITEMS_COUNT - 1;
    }

    /**
     * When we add an item, a new activity is shown
     */
    @Test
    public void myMeetingsList_addItem() {
        // Given : We start on the first activity
        onView(ViewMatchers.withId(R.id.list_meetings))
                .check(matches(ViewMatchers.isDisplayed()));
        // When perform a click on a delete icon
        onView(allOf(ViewMatchers.withId(R.id.add_meeting),ViewMatchers.isDisplayed()))
                .perform(click());
        // Then : the avatar is displayed
        onView(ViewMatchers.withId(R.id.avatar))
                .check(matches(ViewMatchers.isDisplayed()));
        // Check the button add is still disabled
        onView(ViewMatchers.withId(R.id.create))
                .check(matches(not(ViewMatchers.isEnabled())));
        // Click on the button return
 //       mActivity.finish();
    }

    /**
     * When we add an item and input a room the button add is still disabled
     */
    @Test
    public void myMeetingsList_addItemInputRoom() {
        // Given : We start on the first activity
        onView(ViewMatchers.withId(R.id.list_meetings))
                .check(matches(ViewMatchers.isDisplayed()));
        // When perform a click on a add icon
        onView(allOf(ViewMatchers.withId(R.id.add_meeting),ViewMatchers.isDisplayed()))
                .perform(click());
        // Then : enter "SALLE C"
        onView(ViewMatchers.withId(R.id.room))
                .perform(typeText("SALLE C"), closeSoftKeyboard());
        // check "SALLE C"
        onView(ViewMatchers.withId(R.id.room))
                .check(matches(ViewMatchers.withText("SALLE C")));
        // Check the button add is still disabled
        onView(ViewMatchers.withId(R.id.create))
                .check(matches(not(ViewMatchers.isEnabled())));
        // Click on the button return
//        mActivity.finish();
    }

    /**
     * When we add an item and input the date the button add is still disabled
     * */
    @Test
    public void myMeetingsList_addItemInputDate() {
        // Given : We start on the first activity
        onView(ViewMatchers.withId(R.id.list_meetings))
                .check(matches(ViewMatchers.isDisplayed()));
        // When perform a click on a add icon
        onView(allOf(ViewMatchers.withId(R.id.add_meeting),ViewMatchers.isDisplayed()))
                .perform(click());
        // When perform a click on the date button
        onView(allOf(ViewMatchers.withId(R.id.btn_date),ViewMatchers.isDisplayed()))
                .perform(click());
        // When we enter a date
        onView(isAssignableFrom(DatePicker.class))
                .perform(PickerActions.setDate(2021, 9, 6));
        onView(ViewMatchers.withId(android.R.id.button1))
                .perform(click());
        // check date
        onView(ViewMatchers.withId(R.id.in_date))
                .check(matches(ViewMatchers.withText("2021.9.6")));
        // Check the button add is still disabled
        onView(ViewMatchers.withId(R.id.create))
                .check(matches(not(ViewMatchers.isEnabled())));
        // Click on the button return
//        mActivity.finish();
    }

    /**
     * When we add an item and input the time the button add is still disabled
     */
    @Test
    public void myMeetingsList_addItemInputTime() {
        // Given : We start on the first activity
        onView(ViewMatchers.withId(R.id.list_meetings))
                .check(matches(ViewMatchers.isDisplayed()));
        // When perform a click on a add icon
        onView(allOf(ViewMatchers.withId(R.id.add_meeting),ViewMatchers.isDisplayed()))
                .perform(click());
        // When perform a click on the time button
        onView(allOf(ViewMatchers.withId(R.id.btn_time),ViewMatchers.isDisplayed()))
                .perform(click());
        // When we enter a time
        onView(isAssignableFrom(TimePicker.class))
                .perform(PickerActions.setTime(14,0));
        onView(ViewMatchers.withId(android.R.id.button1))
                .perform(click());
        // check time
        onView(ViewMatchers.withId(R.id.in_time))
                .check(matches(ViewMatchers.withText("14.0")));
        // Check the button add is still disabled
        onView(ViewMatchers.withId(R.id.create))
                .check(matches(not(ViewMatchers.isEnabled())));
        // Click on the button return
//        mActivity.finish();
    }

    /**
     * When we add an item and input the room, date and time the button add becomes enabled
     */
    @Test
    public void myMeetingsList_addItemWholeInput() {
        // Given : We start on the first activity
        onView(ViewMatchers.withId(R.id.list_meetings))
                .check(matches(ViewMatchers.isDisplayed()));
        // When perform a click on a add icon
        onView(allOf(ViewMatchers.withId(R.id.add_meeting),ViewMatchers.isDisplayed()))
                .perform(click());
        // Enter "SALLE C" (Move here as a workaround to refresh the button add)
        onView(ViewMatchers.withId(R.id.room))
                .perform(typeText("SALLE C"), closeSoftKeyboard());
        // check "SALLE C"
        onView(ViewMatchers.withId(R.id.room))
                .check(matches(ViewMatchers.withText("SALLE C")));
        // When perform a click on the date button
        onView(allOf(ViewMatchers.withId(R.id.btn_date),ViewMatchers.isDisplayed()))
                .perform(click());
        // When we enter a date
        onView(isAssignableFrom(DatePicker.class))
                .perform(PickerActions.setDate(2021, 9, 6));
        onView(ViewMatchers.withId(android.R.id.button1))
                .perform(click());
        // check date
        onView(ViewMatchers.withId(R.id.in_date))
                .check(matches(ViewMatchers.withText("2021.9.6")));
        // When perform a click on the time button
        onView(allOf(ViewMatchers.withId(R.id.btn_time),ViewMatchers.isDisplayed()))
                .perform(click());
        // When we enter a time
        onView(isAssignableFrom(TimePicker.class))
                .perform(PickerActions.setTime(14,0));
        onView(ViewMatchers.withId(android.R.id.button1))
                .perform(click());
        // check time
        onView(ViewMatchers.withId(R.id.in_time))
                .check(matches(ViewMatchers.withText("14.0")));
        // Check the button add is still disabled
        onView(ViewMatchers.withId(R.id.create))
                .check(matches(ViewMatchers.isEnabled()));
        // Click on the button return
//        mActivity.finish();
    }

    /**
     * When we add an item and input the room, date and time the button add becomes enabled
     */
    @Test
    public void myMeetingsList_addMeeting() {
        // Given : We start on the first activity
        onView(ViewMatchers.withId(R.id.list_meetings))
                .check(matches(ViewMatchers.isDisplayed()));
        // add a meeting
        addItem("SALLE C", 2021, 9, 6, 14, 0);
        // Check the return to the main activity
        onView(ViewMatchers.withId(R.id.list_meetings))
                .check(matches(ViewMatchers.isDisplayed()));
        // Then : the number of element is incremented by 1
        onView(allOf(ViewMatchers.withId(R.id.list_meetings),ViewMatchers.isDisplayed()))
                .check(withItemCount(ITEMS_COUNT));
        // Click on the button return
//        mActivity.finish();
    }

    /**
     * When we add an item and define a room filter All
     */
    @Test
    public void myMeetingsList_roomfilterAll() {
        // Given : We start on the first activity
        onView(ViewMatchers.withId(R.id.list_meetings))
                .check(matches(ViewMatchers.isDisplayed()));
        // add a meeting
        addItem("SALLE D", 2021, 9, 6, 14, 0);
        // Check the return to the main activity
        onView(ViewMatchers.withId(R.id.list_meetings))
                .check(matches(ViewMatchers.isDisplayed()));
        // When perform a click on the position 0 of the room filter spinner

        onView(ViewMatchers.withId(R.id.spinnerRoom))
                .perform(click());
        onData(allOf(is(instanceOf(String.class)), is("All rooms")))
                .inRoot(isPlatformPopup())
                .perform(click());

        onView(ViewMatchers.withId(R.id.spinnerRoom))
                .check(matches(ViewMatchers.withSpinnerText(containsString("All rooms"))));
        // Then : the number of element is the same
        onView(allOf(ViewMatchers.withId(R.id.list_meetings),ViewMatchers.isDisplayed()))
                .check(withItemCount(ITEMS_COUNT));
        // Click on the button return
//        mActivity.finish();
    }

    /**
     * When we add an item and define a room filter
     */
    @Test
    public void myMeetingsList_roomfilter() {
        // Given : We start on the first activity
        onView(ViewMatchers.withId(R.id.list_meetings))
                .check(matches(ViewMatchers.isDisplayed()));
        // add a meeting
        addItem("SALLE E", 2021, 9, 6, 14, 0);
        // Check the return to the main activity
        onView(ViewMatchers.withId(R.id.list_meetings))
                .check(matches(ViewMatchers.isDisplayed()));
        // When perform a click on the position 0 SALLE E of the room filter spinner
        onView(ViewMatchers.withId(R.id.spinnerRoom))
                .perform(click());
        onData(allOf(is(instanceOf(String.class)), is("SALLE E")))
                .inRoot(isPlatformPopup())
                .perform(click());

        onView(ViewMatchers.withId(R.id.spinnerRoom))
                .check(matches(ViewMatchers.withSpinnerText(containsString("SALLE E"))));
        // Then : the number of element is the same
        onView(allOf(ViewMatchers.withId(R.id.list_meetings),ViewMatchers.isDisplayed()))
                .check(withItemCount(1));
        // Click on the button return
//        mActivity.finish();
    }

    /**
     * When we add an item and define a date filter
     */
    @Test
    public void myMeetingsList_datefilter() {
        // Given : We start on the first activity
        onView(ViewMatchers.withId(R.id.list_meetings))
                .check(matches(ViewMatchers.isDisplayed()));
        // add a meeting
        addItem("SALLE F", 2021, 9, 6, 23, 59);
        addItem("SALLE F", 2021, 9, 7, 0, 0);
        addItem("SALLE F", 2021, 9, 7, 23, 59);
        addItem("SALLE F", 2021, 9, 8, 0, 0);
        // Check the return to the main activity
        onView(ViewMatchers.withId(R.id.list_meetings))
                .check(matches(ViewMatchers.isDisplayed()));
        // When perform a click on the date button
        onView(allOf(ViewMatchers.withId(R.id.btn_dateFilter), ViewMatchers.isDisplayed()))
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
        onView(allOf(ViewMatchers.withId(R.id.list_meetings),ViewMatchers.isDisplayed()))
                .check(withItemCount(2));
        // Click on the button return
//        mActivity.finish();
    }

    /**
     * When we add an item and define a date filter
     */
    @Test
    public void myMeetingsList_cleardatefilter() {
        // Given : We start on the first activity
        onView(ViewMatchers.withId(R.id.list_meetings))
                .check(matches(ViewMatchers.isDisplayed()));
        // add a meeting
        addItem("SALLE G", 2021, 9, 6, 23, 59);
        addItem("SALLE G", 2021, 9, 7, 0, 0);
        addItem("SALLE G", 2021, 9, 7, 23, 59);
        addItem("SALLE G", 2021, 9, 8, 0, 0);
        // Check the return to the main activity
        onView(ViewMatchers.withId(R.id.list_meetings))
                .check(matches(ViewMatchers.isDisplayed()));
        // When perform a click on the date button
        onView(allOf(ViewMatchers.withId(R.id.btn_dateFilter), ViewMatchers.isDisplayed()))
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
        onView(allOf(ViewMatchers.withId(R.id.btn_dateClear), ViewMatchers.isDisplayed()))
                .perform(click());
        // check date
        onView(ViewMatchers.withId(R.id.in_dateFilter))
                .check(matches(ViewMatchers.withText("All dates")));
        // Then : the number of element is the same
        onView(allOf(ViewMatchers.withId(R.id.list_meetings),ViewMatchers.isDisplayed()))
                .check(withItemCount(ITEMS_COUNT));
        // Click on the button return
//        mActivity.finish();
    }
    /**
     * When we add an item and define a date filter
     */
    @Test
    public void myMeetingsList_wholefilter() {
        // Given : We start on the first activity
        onView(ViewMatchers.withId(R.id.list_meetings))
                .check(matches(ViewMatchers.isDisplayed()));
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
                .inRoot(isPlatformPopup())
                .perform(click());

        onView(ViewMatchers.withId(R.id.spinnerRoom))
                .check(matches(ViewMatchers.withSpinnerText(containsString("SALLE I"))));
        // When perform a click on the date button
        onView(allOf(ViewMatchers.withId(R.id.btn_dateFilter), ViewMatchers.isDisplayed()))
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
        onView(allOf(ViewMatchers.withId(R.id.list_meetings),ViewMatchers.isDisplayed()))
                .check(withItemCount(2));
        // Click on the button return
//        mActivity.finish();
    }

}