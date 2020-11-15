package com.dija.mareu1.utils;

import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import com.dija.mareu1.R;

import org.hamcrest.Matcher;

public class DeleteViewAction implements ViewAction {
        @Override
        public Matcher<View> getConstraints() {
            return null;
        }

        @Override
        public String getDescription() {
            return "Click on specific button";
        }

        @Override
        public void perform(UiController uiController, View view) {
            View button = view.findViewById(R.id.image_bin_item);
            button.performClick();
        }
}
