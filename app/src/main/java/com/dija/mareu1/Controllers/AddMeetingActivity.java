package com.dija.mareu1.Controllers;

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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.dija.mareu1.DI.DI;
import com.dija.mareu1.Model.Meeting;
import com.dija.mareu1.Service.MeetingApiService;
import com.dija.mareu1.View.DatePickerFragment;
import com.dija.mareu1.databinding.ActivityAddBinding;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddMeetingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    private ActivityAddBinding binding;
    private MeetingApiService mMeetingApiService;
    private String occupiedRoom;
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

        // LIEN AVEC LE DATE PICKER
        binding.dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");

            }
        });

        // CREATION DU TIME PICKER - Beginning time //
        binding.beginningTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AddMeetingActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                String beginningHour = hourOfDay + ":" + minute;
                                binding.beginningTimeBtn.setText(beginningHour);
                            }
                        }, hour, minute, true);
                timePickerDialog.show();

            }
        });

        // CREATION DU TIME PICKER - End time // - considérons désormais que les réunions durent 45 minutes
        /**binding.endTimeBtn.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(AddMeetingActivity.this,
        new TimePickerDialog.OnTimeSetListener() {

        @Override public void onTimeSet(TimePicker view, int hourOfDay,
        int minute) {
        binding.endTimeBtn.setText(hourOfDay + ":" + minute);
        }
        }, hour, minute, true);
        availableRoomsAtInputTime();
        timePickerDialog.show();

        }
        });*/
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


    // CONFIGURATION DU DATE PICKER //

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance().format(c.getTime());
        binding.dateBtn.setText(currentDateString);
    }

    // HEURE DE FIN DE LA REUNION//
    private String getEndTime() {
        int DURATION = 45;
        DateTimeFormatter fmt = DateTimeFormat.forPattern("kk:mm");
        DateTime beginDateTime = fmt.parseDateTime((String) binding.beginningTimeBtn.getText());
        DateTime meetingEndDateTime = beginDateTime.plus(Minutes.minutes(DURATION));
        return meetingEndDateTime.toString();
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
                        binding.dateBtn.getText().toString(),
                        binding.beginningTimeBtn.getText().toString(),
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
    //TODO ne renvoyer dans le spinner que les salles libres
    /**
    private List<String> availableRoomsAtInputTime() {

        DateTimeFormatter fmt = DateTimeFormat.forPattern("kk:mm");
        DateTime beginDateTime = fmt.parseDateTime((String) binding.beginningTimeBtn.getText());

        for (Meeting meeting : mMeetingApiService.getMeetings()) {
            DateTime meetingBeginningDateTime = fmt.parseDateTime(meeting.getBeginningTime());
            DateTime meetingEndDateTime = meetingBeginningDateTime.plus(Minutes.minutes(45));
            Interval meetingInterval = new Interval(meetingBeginningDateTime, meetingEndDateTime);
            if (meeting.getDate() == binding.dateBtn.getText() && meetingInterval.contains(beginDateTime)) {
                //availableRooms.remove(meeting.getRoom()); <= ne proposer que les salles disponibles
                Toast toast = Toast.makeText(getApplicationContext(), "salle indisponible à cet horaire", Toast.LENGTH_LONG);
                toast.show();
            }
        }
        return availableRooms;
    }

    private List<String> availableRoomsAtInputTime2() {

        DateTimeFormatter fmt = DateTimeFormat.forPattern("kk:mm");
        DateTime beginDateTime = fmt.parseDateTime((String) binding.beginningTimeBtn.getText());
        filteredList = new ArrayList<>(availableRooms);

        for (Meeting meeting : mMeetingApiService.getMeetings()) {
            DateTime meetingBeginningDateTime = fmt.parseDateTime(meeting.getBeginningTime());
            DateTime meetingEndDateTime = meetingBeginningDateTime.plus(Minutes.minutes(45));
            Interval meetingInterval = new Interval(meetingBeginningDateTime, meetingEndDateTime);

            if (meeting.getDate().contains(binding.dateBtn.getText())) {
                if (!meetingInterval.contains(beginDateTime)) {
                    filteredList.remove(meeting.getRoom());
                    //Toast toast = Toast.makeText(getApplicationContext(), "salle indisponible à cet horaire", Toast.LENGTH_LONG);
                    //toast.show();
                }
            }
        }
        return filteredList;
    }*/


}







