package com.dija.mareu1.controllers.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.dija.mareu1.databinding.TimeFilterFragmentBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class FragmentTimeFilter extends DialogFragment {
    private TimeFilterFragmentBinding binding;
    public FragmentTimeFilter.OnInputSelected mOnInputSelected;

    private DatePickerDialog.OnDateSetListener mOnDateSetListener, mOnDateSetListener1;
    private TimePickerDialog.OnTimeSetListener mOnTimeSetListener, mOnTimeSetListener1;

    private int year, month, day, hour, minute;
    private int yearInput, monthInput, dayInput, hourInput, minuteInput;
    private int yearInput1, monthInput1, dayInput1, hourInput1, minuteInput1;
    long longFirstDateTime, longSecondDateTime;

    //-------------------------------
    //INTERFACE
    //-------------------------------
    public interface OnInputSelected {
        void sendInput(long tag, long tag1);
    }

    //-------------------------------
    //ON CREATE VIEW
    //-------------------------------
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TimeFilterFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        //--------FIRST TAG BUTTON-----------------
        binding.timeFilterFirstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate(mOnDateSetListener);
            }
        });
        mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                yearInput = year;
                monthInput = month;
                dayInput = dayOfMonth;
                pickTime(mOnTimeSetListener);
            }
        };
        mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hourInput = hourOfDay;
                minuteInput = minute;

                getFirstDateTimeLong();
                binding.timeFilterFirstButton.setText(getDateTimeString(longFirstDateTime));
            }
        };

        //--------SECOND TAG BUTTON-----------------

        binding.timeFilterSecondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate(mOnDateSetListener1);
            }
        });
        mOnDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                yearInput1 = year;
                monthInput1 = month;
                dayInput1 = dayOfMonth;
                pickTime(mOnTimeSetListener1);
            }
        };
        mOnTimeSetListener1 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hourInput1 = hourOfDay;
                minuteInput1 = minute;

                getSecondDateTimeLong();
                binding.timeFilterSecondButton.setText(getDateTimeString(longSecondDateTime));
            }
        };

        //--------APPLY BUTTON-----------------
        binding.timeFilterApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyButtonActions();
            }
        });

        //--------NEUTRAL BUTTON-----------------
        binding.timeFilterNeutralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                neutralButtonActions();
            }
        });
        return view;
    }

    //--------------------------------
    //LIFECYCLE
    //--------------------------------
    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ConstraintLayout.LayoutParams.MATCH_PARENT;
        params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mOnInputSelected = (FragmentTimeFilter.OnInputSelected) getActivity();
        } catch (ClassCastException e) {
            Log.e("TAG", "onAttach ClassCastException: " + e.getMessage());
        }
    }

    //----------------------------
    //ACTIONS
    //----------------------------

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

    private void pickDate(DatePickerDialog.OnDateSetListener listener) {
        getCurrentDateTime();
        DatePickerDialog dialog = new DatePickerDialog(
                getContext(),
                listener,
                year, month, day);
        dialog.show();
    }

    private void pickTime(TimePickerDialog.OnTimeSetListener listener) {
        getCurrentDateTime();
        TimePickerDialog dialog = new TimePickerDialog(
                getContext(),
                listener,
                hour, minute, true);
        dialog.show();

    }

    private void getFirstDateTimeLong() {
        Calendar cal = new GregorianCalendar(yearInput,
                monthInput,
                dayInput,
                hourInput,
                minuteInput);
        longFirstDateTime = cal.getTimeInMillis();
    }

    private void getSecondDateTimeLong() {
        Calendar calen = new GregorianCalendar(yearInput1,
                monthInput1,
                dayInput1,
                hourInput1,
                minuteInput1);
        longSecondDateTime = calen.getTimeInMillis();
    }

    private String getDateTimeString(long filterDateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - kk:mm");
        return sdf.format(filterDateTime);
    }

    private void applyButtonActions() {
        if (mOnInputSelected != null) {
            mOnInputSelected.sendInput(longFirstDateTime, longSecondDateTime);
        }
        getDialog().dismiss();
    }

    private void neutralButtonActions() {
        if (mOnInputSelected != null) {
            mOnInputSelected.sendInput(0, Long.MAX_VALUE);
        }
        getDialog().dismiss();
    }
}
