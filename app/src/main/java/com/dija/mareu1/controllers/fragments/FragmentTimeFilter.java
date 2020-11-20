package com.dija.mareu1.controllers.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.dija.mareu1.R;
import com.dija.mareu1.databinding.TimeFilterFragmentBinding;
import com.dija.mareu1.events.TimeFilterEvent;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class FragmentTimeFilter extends DialogFragment {
    private TimeFilterFragmentBinding binding;
    private SharedPreferences mSharedPreferences;

    private DatePickerDialog.OnDateSetListener mOnDateSetListener, mOnDateSetListener1;
    private TimePickerDialog.OnTimeSetListener mOnTimeSetListener, mOnTimeSetListener1;

    private int year, month, day, hour, minute;
    private int yearInput, monthInput, dayInput, hourInput, minuteInput;
    private int yearInput1, monthInput1, dayInput1, hourInput1, minuteInput1;
    long longFirstDateTime, longSecondDateTime;

    //-------------------------------
    //ON CREATE VIEW
    //-------------------------------
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TimeFilterFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());

        setButtonsText();

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
            mSharedPreferences.edit().putString("tag", getDateTimeString(longFirstDateTime)).apply();
            binding.timeFilterFirstButton.setText(getDateTimeString(longFirstDateTime));

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
            //mRunTimeData.setTag1(longSecondDateTime);
            mSharedPreferences.edit().putString("tag1", getDateTimeString(longSecondDateTime)).apply();
            binding.timeFilterSecondButton.setText(getDateTimeString(longSecondDateTime));
        };

        //--------APPLY BUTTON-----------------
        binding.timeFilterApplyButton.setOnClickListener(v -> applyButtonActions());

        //--------NEUTRAL BUTTON-----------------
        binding.timeFilterNeutralButton.setOnClickListener(v -> neutralButtonActions());
        return view;
    }

    //----------------------------
    //ACTIONS
    //----------------------------
    public void setButtonsText() {
        if (!mSharedPreferences.getString("tag", "").equals("")) {
            binding.timeFilterFirstButton.setText(mSharedPreferences.getString("tag", ""));
        } else {
            binding.timeFilterFirstButton.setText(R.string.horaire_de_début);
        }
        if (!mSharedPreferences.getString("tag1", "").equals("")) {
            binding.timeFilterSecondButton.setText(mSharedPreferences.getString("tag1", ""));
        } else {
            binding.timeFilterSecondButton.setText(R.string.horaire_de_fin);
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - kk:mm");
        return sdf.format(filterDateTime);
    }

    private void applyButtonActions() {
        EventBus.getDefault().post(new TimeFilterEvent(longFirstDateTime, longSecondDateTime));
        getDialog().dismiss();
    }

    private void neutralButtonActions() {
        EventBus.getDefault().post(new TimeFilterEvent(0, Long.MAX_VALUE));
        getDialog().dismiss();
    }
}
