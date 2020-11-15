package com.dija.mareu1.controllers.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dija.mareu1.databinding.AddTextandbuttonFragmentBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class FragmentAddTextAndButton extends Fragment {
    private AddTextandbuttonFragmentBinding binding;
    private SecondFragmentListener mListener;

    private String subjectInput;
    private DatePickerDialog.OnDateSetListener mOnDateSetListener;
    private TimePickerDialog.OnTimeSetListener mOnTimeSetListener;
    private long longBeginningDateTime;
    private long calculatedLongEndDateTime;
    private boolean allIsFilled;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    private int yearInput;
    private int monthInput;
    private int dayInput;
    private int hourInput;
    private int minuteInput;

    //INTERFACE
    public interface SecondFragmentListener {
        void onSecondFragmentSent(String subjectInput, long longBeginningDateTime, long calculatedLongEndDateTime, String participantInput, boolean allIsFilled);
    }


    // CONSTRUCTOR
    public FragmentAddTextAndButton() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AddTextandbuttonFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // DISPLAYING DATA

        binding.dateTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });
        mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                yearInput = year;
                monthInput = month;
                dayInput = dayOfMonth;
                pickTime();
            }
        };
        mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hourInput = hourOfDay;
                minuteInput = minute;

                getDateTimeLong();
                binding.dateTimeBtn.setText(getDateTimeString());
                getEndDateTimeLong();
            }
        };

        binding.participant.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        binding.addAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addParticipantFabActions();
            }
        });
        activateCreateMeetingButton();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SecondFragmentListener) {
            mListener = (SecondFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement SecondFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //-----------------
    //ACTIONS
    //-----------------

    private String getCurrentDateTime() {
        Calendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);
        Date l = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - kk:mm");
        return sdf.format(l);
    }

    private void pickDate() {
        getCurrentDateTime();
        DatePickerDialog dialog = new DatePickerDialog(
                getContext(),
                mOnDateSetListener,
                year, month, day);
        dialog.show();
    }

    private void pickTime() {
        getCurrentDateTime();
        TimePickerDialog dialog = new TimePickerDialog(
                getContext(),
                mOnTimeSetListener,
                hour, minute, true);
        dialog.show();

    }

    private void getDateTimeLong() {
        Calendar cal = new GregorianCalendar(yearInput,
                monthInput,
                dayInput,
                hourInput,
                minuteInput);
        longBeginningDateTime = cal.getTimeInMillis();
    }

    private String getDateTimeString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - kk:mm");
        return sdf.format(longBeginningDateTime);
    }

    private void getEndDateTimeLong() {
        int DURATION = 2700000; //<= 45 minutes in milliseconds
        calculatedLongEndDateTime = longBeginningDateTime + DURATION;
    }

    private void addParticipantFabActions() {
        String participantInput = binding.participant.getText().toString();
        if (isValidEmail(participantInput)) {
            binding.participantTextview.append(participantInput + ", ");
            binding.participant.setText("");
        } else {
            Toast.makeText(getContext(), "Veuillez entrer une adresse e-mail valide.", Toast.LENGTH_LONG).show();
        }
    }

    private void activateCreateMeetingButton() {
        binding.participantTextview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 30 ) {
                    allIsFilled = true;

                    // SENDING DATA TO ACTIVITY
                    mListener.onSecondFragmentSent(
                            binding.subject.getText().toString(),
                            longBeginningDateTime,
                            calculatedLongEndDateTime,
                            binding.participantTextview.getText().toString(),
                            allIsFilled);
                }
            }

        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


}
