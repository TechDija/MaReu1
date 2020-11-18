package com.dija.mareu1.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dija.mareu1.DI.DI;
import com.dija.mareu1.R;
import com.dija.mareu1.controllers.fragments.FragmentAddRoom;
import com.dija.mareu1.controllers.fragments.FragmentAddTextAndButton;
import com.dija.mareu1.databinding.ActivityAddBinding;
import com.dija.mareu1.model.Meeting;
import com.dija.mareu1.service.MeetingApiService;

import java.util.Objects;

public class AddMeetingActivity extends AppCompatActivity implements FragmentAddRoom.FirstFragmentListener, FragmentAddTextAndButton.SecondFragmentListener {
    private ActivityAddBinding binding;

    private String occupiedRoomName;
    private String subjectInput;
    private long longBeginningDateTime;
    private long calculatedLongEndDateTime;
    private String participantInput;
    private boolean allIsFilled;

    //--------------------------------
    //RECOVERING DATA FROM FRAGMENTS
    //--------------------------------

    @Override
    public void onFirstFragmentSent(String occupiedRoomName) {
        this.occupiedRoomName = occupiedRoomName;
    }

    @Override
    public void onSecondFragmentSent(String subjectInput, long longBeginningDateTime, long calculatedLongEndDateTime, String participantInput, boolean allIsFilled) {
        this.subjectInput = subjectInput;
        this.longBeginningDateTime = longBeginningDateTime;
        this.calculatedLongEndDateTime = calculatedLongEndDateTime;
        this.participantInput = participantInput.substring(0, participantInput.length() - 2);
        this.allIsFilled = allIsFilled;
    }

    //--------------------------------
    //ON CREATE
    //--------------------------------
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.toolbarAddActivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        configureFirstFragment();
        configureSecondFragment();

        initializeButton();

        binding.createMeeting.setOnClickListener(v -> createMeetingOnclick());
    }


    //------------------
    // ACTIONS
    //------------------

    private void initializeButton() {
        binding.createMeeting.setEnabled(true);
    }

    private void createMeetingOnclick() {
        if (allIsFilled
                && !Objects.equals(occupiedRoomName, "Salle de réunion")
                && longBeginningDateTime != 0
                && !Objects.equals(subjectInput, "")) {
            MeetingApiService service = DI.getMeetingApiService();
            Meeting meeting = new Meeting(
                    this.longBeginningDateTime,
                    this.calculatedLongEndDateTime,
                    this.occupiedRoomName,
                    this.subjectInput,
                    this.participantInput);
            service.addMeeting(meeting);
            navigateToParentActivity();
        } else {
            Toast.makeText(getApplication(), "Veuillez remplir tous les champs", Toast.LENGTH_LONG).show();
        }

    }

    private void navigateToParentActivity() {
        Intent mainActivity = new Intent(AddMeetingActivity.this, MainActivity.class);
        startActivity(mainActivity);
    }

    //----------------
    // FRAGMENTS
    //----------------

    private void configureFirstFragment() {
        FragmentAddRoom firstFragment = (FragmentAddRoom) getSupportFragmentManager().findFragmentById(R.id.first_add_fragment_container);
        if (firstFragment == null) {
            firstFragment = new FragmentAddRoom();
            getSupportFragmentManager().beginTransaction().replace(R.id.first_add_fragment_container, firstFragment).commit();
        }
    }

    private void configureSecondFragment() {
        FragmentAddTextAndButton secondFragment = (FragmentAddTextAndButton) getSupportFragmentManager().findFragmentById(R.id.second_add_fragment_container);
        if (secondFragment == null) {
            secondFragment = new FragmentAddTextAndButton();
            getSupportFragmentManager().beginTransaction().replace(R.id.second_add_fragment_container, secondFragment).commit();
        }
    }
}







