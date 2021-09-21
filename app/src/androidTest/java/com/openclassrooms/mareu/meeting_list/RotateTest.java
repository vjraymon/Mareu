
package com.openclassrooms.mareu.meeting_list;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.openclassrooms.mareu.utils.AddMeeting.ITEMS_COUNT;
import static com.openclassrooms.mareu.utils.AddMeeting.addItem;
import static com.openclassrooms.mareu.utils.AddMeeting.resetFilters;
import static com.openclassrooms.mareu.utils.AddMeeting.timeWait;
import static com.openclassrooms.mareu.utils.RecyclerViewItemCountAssertion.withItemCount;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;

import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.ui.meeting_list.ListMeetingActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test class for list of neighbours
 */

@RunWith(AndroidJUnit4.class)
public class RotateTest {

    private AppCompatActivity mActivity;

    @Rule
    public ActivityTestRule<ListMeetingActivity> mActivityRule =
            new ActivityTestRule(ListMeetingActivity.class);

    @Before
    public void setUp() {
        ITEMS_COUNT = 0;
        mActivity = mActivityRule.getActivity();
    }

    /**
     * When we rota te the screen, the informations are lost
     */
    @Test
    public void myMeetingsList_rotate() {
        // Given : We start on the first activity
        onView(withId(R.id.list_meetings))
                .check(matches(ViewMatchers.isDisplayed()));
        // Reset filters
        resetFilters();
        // add a meeting
        for (int i = 0; i < 4; i++) {
 //           try {
 //               UiDevice device = UiDevice.getInstance(getInstrumentation());
                // Given : We start on the first activity
                onView(withId(R.id.list_meetings))
                        .check(matches(ViewMatchers.isDisplayed()));
                addItem("SALLE J", 2021, 9, 9, 14, 30);
                // Check the return to the main activity
                onView(withId(R.id.list_meetings))
                        .check(matches(ViewMatchers.isDisplayed()));
                // Then : the number of element is greater than 0
                onView(withId(R.id.list_meetings))
                        .check(withItemCount(ITEMS_COUNT));
                assert (ITEMS_COUNT > 0);

                if (mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }

//                device.setOrientationLeft();
                timeWait(3000);
//            timeWait();
 //               getInstrumentation().waitForIdleSync();
 //           } catch (Exception e) {
 //               assert(false);
 //           }
            // Then : the number of element is reset to 0
            onView(withId(R.id.list_meetings))
                    .check(withItemCount(0));
            ITEMS_COUNT = 0;
        }

    }
}