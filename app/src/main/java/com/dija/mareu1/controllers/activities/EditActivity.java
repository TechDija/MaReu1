package com.dija.mareu1.controllers.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dija.mareu1.databinding.ActivityEditBinding;

import java.text.SimpleDateFormat;

import static com.dija.mareu1.controllers.activities.MainActivity.PARTICIPANT;
import static com.dija.mareu1.controllers.activities.MainActivity.ROOM;
import static com.dija.mareu1.controllers.activities.MainActivity.SUBJECT;
import static com.dija.mareu1.controllers.activities.MainActivity.TIME;
import static java.lang.Long.parseLong;

public class EditActivity extends AppCompatActivity {
    ActivityEditBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setSupportActionBar(binding.toolbarAddActivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String subject = intent.getStringExtra(SUBJECT);
        String strTime = intent.getStringExtra(String.valueOf(TIME));
        String room = intent.getStringExtra(ROOM);
        String participant = intent.getStringExtra(PARTICIPANT);

        try {
            long time = Long.valueOf(strTime);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy kk:mm");
            String timeMeeting = sdf.format(time);
        } catch (Exception e) {
            Log.e("TAG", "exception while parsing long to string");
        }

        binding.subject.setText(subject);
        binding.beginningTimeBtn.setText(strTime);
        binding.displayCurrentRoom.setText(room);
        binding.participant.setText(participant);


    }
}