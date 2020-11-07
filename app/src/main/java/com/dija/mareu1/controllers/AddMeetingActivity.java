package com.dija.mareu1.controllers;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.dija.mareu1.DI.DI;
import com.dija.mareu1.R;
import com.dija.mareu1.model.Meeting;
import com.dija.mareu1.service.MeetingApiService;
import com.dija.mareu1.view.DatePickerFragment;
import com.dija.mareu1.databinding.ActivityAddBinding;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class AddMeetingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ActivityAddBinding binding;
    private MeetingApiService mMeetingApiService;
    private String occupiedRoom;
    private Long inputBeginningDateTime;
    private Long calculatedEndDateTime;
    private List<String> availableRooms;
    List<String> filteredList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mMeetingApiService = DI.getMeetingApiService();
        availableRooms = mMeetingApiService.getAllRooms();
        setSupportActionBar(binding.toolbarAddActivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeButton();
        createMeetingOnclick();


        // LIEN AVEC LE DATE & TIME PICKER

        binding.beginningTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkWithCustomDateTime();
            }
        });


        // APPEL DU CLAVIER "@" //
        binding.participant.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS );
        //TODO introduire une autocomplétion "@lamzone.com"

        // CREATION DU SPINNER //
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, availableRooms);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.rooms.setAdapter(adapter);
        binding.rooms.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        occupiedRoom = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    // HEURE DE FIN DE LA REUNION//
    private long getEndTime() {
        int DURATION = 2700000; //<= 45 minutes in milliseconds
        calculatedEndDateTime = inputBeginningDateTime + DURATION;
        return calculatedEndDateTime;
    }

    //ACTIVATION DU BOUTON D'ENREGISTREMENT //
    private void initializeButton() {
        binding.subjectLyt.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.createMeeting.setEnabled(s.length() > 0);
            }
        });
    }

    private void createMeetingOnclick() {
        binding.createMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Meeting meeting = new Meeting(
                        inputBeginningDateTime,
                        getEndTime(),
                        occupiedRoom,
                        binding.subjectLyt.getEditText().getText().toString(),
                        binding.participantLyt.getEditText().getText().toString());

                mMeetingApiService.addMeeting(meeting);

                Intent mainActivity = new Intent(AddMeetingActivity.this, MainActivity.class);
                startActivity(mainActivity);
            }
        });
    }

    public void linkWithCustomDateTime() {
        final View dialogView = View.inflate(AddMeetingActivity.this, R.layout.date_time_picker_dialog, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(AddMeetingActivity.this).create();

        dialogView.findViewById(R.id.date_time_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

                Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());

                inputBeginningDateTime = calendar.getTimeInMillis();
                binding.beginningTimeBtn.setText(timePicker.getCurrentHour().toString() + ":"+ timePicker.getCurrentMinute().toString());
                alertDialog.dismiss();
            }
        });

        alertDialog.setView(dialogView);
        alertDialog.show();
    }




    //TODO ne renvoyer dans le spinner que les salles libres

}







