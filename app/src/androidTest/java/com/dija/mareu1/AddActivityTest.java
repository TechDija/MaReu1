package com.dija.mareu1;

import android.widget.DatePicker;

import androidx.test.rule.ActivityTestRule;

import com.dija.mareu1.controllers.activities.AddMeetingActivity;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertNotNull;

public class AddActivityTest {
    AddMeetingActivity mActivity;

    @Rule
    public ActivityTestRule<AddMeetingActivity> mActivityRule =
            new ActivityTestRule(AddMeetingActivity.class);

    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();
        assertNotNull(mActivity);
    }

    @Test
    public void AddMeetingActivity_fragments_areEntirelyDisplayed(){
        onView(withId(R.id.first_add_fragment_container))
                .check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.second_add_fragment_container))
                .check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void AddMeetingActivity_calendarButton_onClick_displaysDatePicker(){
        onView(withId(R.id.dateTimeBtn))
                .perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void AddMeetingActivity_createMeetingButton_createMeetings_ifAllFieldsAreFilled(){
        // TEST TO BE EXECUTED OUT OF POC MODE
        onView(withId(R.id.room_spinner)).perform(click());
        onView(withText("Mario")).perform(click());

        onView(withId(R.id.subject)).perform(clearText(), typeText("Java or Kotlin"));

        onView(withId(R.id.dateTimeBtn)).perform(click());
        onView(withText("OK")).perform(click());
        onView(withText("OK")).perform(click());

        onView(withId(R.id.participant))
                .perform(clearText(), typeText("lamzone@lamzone.com"));
        onView(withId(R.id.add_add_fab))
                .perform(click());

        onView(withId(R.id.participant))
                .perform(clearText(), typeText("francis@lamzone.com"));
        onView(withId(R.id.add_add_fab))
                .perform(click());

        onView(withId(R.id.create_meeting))
                .perform(scrollTo(), click());

        onView(withId(R.id.recycler_view))
                .check(matches(hasChildCount(4)));

    }

    @Test
    public void AddMeetingActivity_createMeetingButton_doesNotCreateMeetings_ifParticipantFieldIsEmpty(){
        // TEST TO BE EXECUTED OUT OF POC MODE
        onView(withId(R.id.room_spinner)).perform(click());
        onView(withText("Mario")).perform(click());

        onView(withId(R.id.subject)).perform(clearText(), typeText("Java or Kotlin"));

        onView(withId(R.id.dateTimeBtn)).perform(click());
        onView(withText("OK")).perform(click());
        onView(withText("OK")).perform(click());

        onView(withId(R.id.create_meeting))
                .perform(scrollTo(), click());

        onView(withId(R.id.add_container_scrollview))
                .check(matches(isDisplayed()));

    }
}
