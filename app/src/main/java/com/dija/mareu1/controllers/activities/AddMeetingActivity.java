package com.dija.mareu1.controllers.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dija.mareu1.DI.DI;
import com.dija.mareu1.R;
import com.dija.mareu1.databinding.ActivityAddBinding;
import com.dija.mareu1.model.Meeting;
import com.dija.mareu1.model.Room;
import com.dija.mareu1.service.MeetingApiService;
import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

public class AddMeetingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ActivityAddBinding binding;
    private MeetingApiService service;

    private String occupiedRoomName;
    private DatePickerDialog.OnDateSetListener mOnDateSetListener;
    private TimePickerDialog.OnTimeSetListener mOnTimeSetListener;
    private long longBeginningDateTime;
    private long calculatedLongEndDateTime;
    private int year, month, day, hour, minute;
    private int yearInput, monthInput, dayInput, hourInput, minuteInput;

    //--------------------------------
    //ON CREATE
    //--------------------------------
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.participantTextview.setVisibility(View.GONE);
        service = DI.getMeetingApiService();

        //---------------SETTING TOOLBAR----------
        setSupportActionBar(binding.toolbarAddActivity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //---------SETTING SPINNER--------------
        setRoomSpinner();

        // ---------SETTING SUBJECTINPUT-----------
        String subjectInput = binding.subject.getText().toString();

        //-------------SETTING DATE TIME PICKER BUTTON--------------
        binding.dateTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });
        mOnDateSetListener = (view1, year, month, dayOfMonth) -> {
            yearInput = year;
            monthInput = month;
            dayInput = dayOfMonth;

            pickTime();
        };
        mOnTimeSetListener = (view12, hourOfDay, minute) -> {
            hourInput = hourOfDay;
            minuteInput = minute;

            getDateTimeLong();
            getEndDateTimeLong();
            binding.dateTimeBtn.setText(getDateTimeString());
            binding.dateTimeBtn.setTextColor(getResources().getColor(R.color.black));
        };
        //-------------SETTING PARTICIPANT ADDING--------------
        binding.participant.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        binding.addAddFab.setOnClickListener(v -> addParticipantFabActions());

        //-------------ENABLING MEETING CREATION-------------
        binding.createMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setErrorsIfFieldsAreEmpty();
                createMeetingOnclick();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.occupiedRoomName = parent.getItemAtPosition(position).toString();

        //--------------------SETTING IMAGE ACCORDING TO ROOM----------------
        for (Room room : service.getAllRooms()) {
            if (occupiedRoomName.contains(room.getRoomName())) {
                binding.roomImage.setImageResource(getImageId(getApplicationContext(), room.getRoomImage()));
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        binding.errorSpinner.setVisibility(View.VISIBLE);
    }

    //------------------
    // ACTIONS
    //------------------
    private void setRoomSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, service.getAllRoomNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.roomSpinner.setAdapter(adapter);
        binding.roomSpinner.setOnItemSelectedListener(this);
    }

    public static int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }

    private void getCurrentDateTime() {
        Calendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);
        Date date = calendar.getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - kk:mm");
        sdf.format(date);
    }

    private void pickDate() {
        getCurrentDateTime();
        DatePickerDialog dialog = new DatePickerDialog(this, mOnDateSetListener, year, month, day);
        dialog.show();
    }

    private void pickTime() {
        getCurrentDateTime();
        TimePickerDialog dialog = new TimePickerDialog(this, mOnTimeSetListener, hour, minute, true);
        dialog.show();
    }

    private void getDateTimeLong() {
        Calendar cal = new GregorianCalendar(yearInput, monthInput, dayInput, hourInput, minuteInput);
        longBeginningDateTime = cal.getTimeInMillis();
    }

    private String getDateTimeString() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - kk:mm");
        return sdf.format(longBeginningDateTime);
    }

    private void getEndDateTimeLong() {
        int DURATION = 2700000; //<= 45 minutes in milliseconds
        calculatedLongEndDateTime = longBeginningDateTime + DURATION;
    }

    private void addParticipantFabActions() {
        String participantInput = binding.participant.getText().toString();
        String participants = binding.participantTextview.getText().toString();
        if (isValidEmail(participantInput)) {
            if (!participants.contains(participantInput)) {
                binding.addActivityChipgroup.addView(addParticipantChip(participantInput));
                binding.participantTextview.append(participantInput + ", ");
                binding.participant.setText("");
            } else {
                Toast.makeText(this, "Cette adresse e-mail est déjà enregistrée.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Veuillez entrer une adresse e-mail valide.", Toast.LENGTH_LONG).show();
        }
    }

    private View addParticipantChip(String participantInput) {
        Chip chip = new Chip(this);
        chip.setText(participantInput);
        return chip;
    }

    public static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private boolean roomIsChosen() {
        return !Objects.equals(occupiedRoomName, "");
    }

    private boolean subjectIsChosen() {
        return (binding.subject.getText().toString().length() > 1);
    }

    private boolean dateIsChosen() {
        return longBeginningDateTime != 0;
    }

    private boolean participantsAreChosen() {
        return binding.addActivityChipgroup.getChildCount() >= 2;
    }

    private void createMeetingOnclick() {
        MeetingApiService service = DI.getMeetingApiService();
        if (roomIsChosen() && subjectIsChosen() && dateIsChosen() && participantsAreChosen()) {
            Meeting meeting = new Meeting(
                    longBeginningDateTime,
                    calculatedLongEndDateTime,
                    occupiedRoomName,
                    binding.subject.getText().toString(),
                    binding.participantTextview.getText().toString());
            service.addMeeting(meeting);
            navigateToParentActivity();
        } else {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_LONG).show();
        }
    }

    private void setErrorsIfFieldsAreEmpty() {
        if (!roomIsChosen()) {
            binding.errorSpinner.setVisibility(View.VISIBLE);
        }
        if (!subjectIsChosen()) {
            binding.subjectLyt.setError("Veuillez entrez un sujet de réunion");
        }
        if (!dateIsChosen()) {
            binding.dateTimeBtn.setText(R.string.error_time);
            binding.dateTimeBtn.setTextColor(getResources().getColor(R.color.red));
            binding.underlining.setBackgroundColor(getResources().getColor(R.color.red));

        }
        if (!participantsAreChosen()) {
            binding.participantLyt.setError("Veuillez entrez au moins deux participants");
        }
    }

    private void navigateToParentActivity() {
        Intent mainActivity = new Intent(AddMeetingActivity.this, MainActivity.class);
        startActivity(mainActivity);
    }


}







