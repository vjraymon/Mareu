
package com.openclassrooms.mareu.meeting_list;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.app.AppCompatActivity;

import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.ui.meeting_list.ListMeetingActivity;
import com.openclassrooms.mareu.utils.DeleteViewAction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static com.openclassrooms.mareu.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.CoreMatchers.allOf;
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
    }

    /**
     * We ensure that our recyclerview is displaying at least on item
     */
    @Test
    public void myMeetingsList_shouldNotBeEmpty() {
        // Select the tab "MY NEIGHBOURS"
        onView(allOf(ViewMatchers.withText("MY NEIGHBOURS"),ViewMatchers.isDisplayed()))
                .perform(click());
        // First scroll to the position that needs to be matched and click on it.
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours),ViewMatchers.isDisplayed()))
                .check(matches(hasMinimumChildCount(1)));
    }

    /**
     * When we delete an item, the item is no more shown
     */
    @Test
    public void myMeetingsList_deleteAction_shouldRemoveItem() {
        // Select the tab "MY NEIGHBOURS"
        onView(allOf(ViewMatchers.withText("MY NEIGHBOURS"),ViewMatchers.isDisplayed()))
                .perform(click());
        // Given : We remove the element at position 1
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours),ViewMatchers.isDisplayed()))
                .check(withItemCount(ITEMS_COUNT));
        // When perform a click on a delete icon
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours),ViewMatchers.isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));
        // Then : the number of element is 11
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours),ViewMatchers.isDisplayed()))
                .check(withItemCount(ITEMS_COUNT-1));
    }

}