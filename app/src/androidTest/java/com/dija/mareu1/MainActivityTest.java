package com.dija.mareu1;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;

import com.dija.mareu1.controllers.activities.MainActivity;
import com.dija.mareu1.utils.DeleteViewAction;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertNotNull;

public class MainActivityTest {
    private static final int ITEM_COUNT = 3;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule(MainActivity.class);

    @Before
    public void setUp() {
        MainActivity activity = mActivityRule.getActivity();
        assertNotNull(activity);
    }

    @Test
    public void MeetingList_isNotEmpty() {
        onView(withId(R.id.recycler_view))
                .check(matches(hasMinimumChildCount(1)));
    }

    @Test
    public void MeetingList_deleteAction_shouldRemoveItem() {
        //checking the actual item count
        onView(withId(R.id.recycler_view))
                .check(matches(hasChildCount(ITEM_COUNT)));
        //deleting an item
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, new DeleteViewAction()));
        //checking the new item-count is minus 1
        onView(withId(R.id.recycler_view))
                .check(matches(hasChildCount(ITEM_COUNT - 1)));
    }


    @Test
    public void MeetingListActionFloatingButton_onClick_navigatesToAddActivity() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.add_fab), withContentDescription("Add"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction imageView = onView(
                allOf(withId(R.id.room_image), withContentDescription("pastille de la salle de réunion"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class))),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }


}