package com.dija.mareu1.controllers.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.dija.mareu1.R;
import com.dija.mareu1.databinding.TimeFilterFragmentBinding;
import com.dija.mareu1.events.TimeFilterEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

public class FragmentTimeFilter extends DialogFragment {
    private TimeFilterFragmentBinding binding;

    private DatePickerDialog.OnDateSetListener mOnDateSetListener, mOnDateSetListener1;
    private TimePickerDialog.OnTimeSetListener mOnTimeSetListener, mOnTimeSetListener1;

    private int year, month, day, hour, minute;
    private int yearInput, monthInput, dayInput, hourInput, minuteInput;
    private int yearInput1, monthInput1, dayInput1, hourInput1, minuteInput1;
    long longFirstDateTime, longSecondDateTime;

    //--------------------------------
    //CONSTRUCTOR
    //--------------------------------
    public FragmentTimeFilter(long longFirstDateTime, long longSecondDateTime) {
        this.longFirstDateTime = longFirstDateTime;
        this.longSecondDateTime = longSecondDateTime;
    }

    //-------------------------------
    //ON CREATE VIEW
    //-------------------------------
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TimeFilterFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setButtonText();

        //--------FIRST TAG BUTTON-----------------
        binding.timeFilterFirstButton.setOnClickListener(v -> pickDate(mOnDateSetListener));
        mOnDateSetListener = (view1, year, month, dayOfMonth) -> {
            yearInput = year;
            monthInput = month;
            dayInput = dayOfMonth;
            pickTime(mOnTimeSetListener);
        };
        mOnTimeSetListener = (view12, hourOfDay, minute) -> {
            hourInput = hourOfDay;
            minuteInput = minute;

            getFirstDateTimeLong();
            binding.timeFilterFirstButton.setText("De :   " + getDateTimeString(longFirstDateTime));
            binding.timeFilterFirstButton.setTextAppearance(getContext(), android.R.style.TextAppearance_Medium);
        };

        //--------SECOND TAG BUTTON-----------------
        binding.timeFilterSecondButton.setOnClickListener(v -> pickDate(mOnDateSetListener1));
        mOnDateSetListener1 = (view13, year, month, dayOfMonth) -> {
            yearInput1 = year;
            monthInput1 = month;
            dayInput1 = dayOfMonth;
            pickTime(mOnTimeSetListener1);
        };
        mOnTimeSetListener1 = (view14, hourOfDay, minute) -> {
            hourInput1 = hourOfDay;
            minuteInput1 = minute;

            getSecondDateTimeLong();
            binding.timeFilterSecondButton.setText("A :   " + getDateTimeString(longSecondDateTime));
            binding.timeFilterSecondButton.setTextAppearance(getContext(), android.R.style.TextAppearance_Medium);
        };

        //--------APPLY BUTTON-----------------
        binding.timeFilterApplyButton.setOnClickListener(v -> applyButtonActions());

        //--------NEUTRAL BUTTON-----------------
        binding.timeFilterNeutralButton.setOnClickListener(v -> neutralButtonActions());
        return view;
    }

    //----------------------------
    //LIFECYCLE
    //----------------------------
    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ConstraintLayout.LayoutParams.MATCH_PARENT;
        params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes(params);
    }

    //-----------------------
    //ACTIONS
    //---------------------------
    public void setButtonText() {
        if (longFirstDateTime != 0) {
            binding.timeFilterFirstButton.setText("De :   " + getDateTimeString(longFirstDateTime));
        } else {
            binding.timeFilterFirstButton.setText(R.string.horaire_de_début);
            binding.timeFilterFirstButton.setTextAppearance(getContext(), android.R.style.TextAppearance_Medium);
        }
        if (longSecondDateTime != 0 && longSecondDateTime != Long.MAX_VALUE) {
            binding.timeFilterSecondButton.setText("A :   " + getDateTimeString(longSecondDateTime));
        } else {
            binding.timeFilterSecondButton.setText(R.string.horaire_de_fin);
            binding.timeFilterSecondButton.setTextAppearance(getContext(), android.R.style.TextAppearance_Medium);
        }
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

    private void pickDate(DatePickerDialog.OnDateSetListener listener) {
        getCurrentDateTime();
        DatePickerDialog dialog = new DatePickerDialog(getContext(), listener, year, month, day);
        dialog.show();
    }

    private void pickTime(TimePickerDialog.OnTimeSetListener listener) {
        getCurrentDateTime();
        TimePickerDialog dialog = new TimePickerDialog(getContext(), listener, hour, minute, true);
        dialog.show();

    }

    private void getFirstDateTimeLong() {
        Calendar cal = new GregorianCalendar(yearInput, monthInput, dayInput, hourInput, minuteInput);
        longFirstDateTime = cal.getTimeInMillis();
    }

    private void getSecondDateTimeLong() {
        Calendar calendar = new GregorianCalendar(yearInput1, monthInput1, dayInput1, hourInput1, minuteInput1);
        longSecondDateTime = calendar.getTimeInMillis();
    }

    private String getDateTimeString(long filterDateTime) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - kk:mm");
        return sdf.format(filterDateTime);
    }

    private void applyButtonActions() {
        EventBus.getDefault().post(new TimeFilterEvent(longFirstDateTime, longSecondDateTime));
        Objects.requireNonNull(getDialog()).dismiss();
    }

    private void neutralButtonActions() {
        EventBus.getDefault().post(new TimeFilterEvent(0, Long.MAX_VALUE));
        Objects.requireNonNull(getDialog()).dismiss();
    }
}
