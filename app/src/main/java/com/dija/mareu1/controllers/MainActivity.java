package com.dija.mareu1.controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.dija.mareu1.DI.DI;
import com.dija.mareu1.model.Meeting;
import com.dija.mareu1.R;
import com.dija.mareu1.service.MeetingApiService;
import com.dija.mareu1.view.MeetingAdapter;
import com.dija.mareu1.databinding.ActivityMainBinding;
import com.dija.mareu1.events.DeleteMeetingEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private MeetingAdapter mAdapter;
    private MeetingApiService service;
    private List<Meeting> mMeetings;
    private List<Meeting> mfilteredMeetings;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setSupportActionBar(binding.toolbarMainActivity);
        initList();


        binding.addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToAddActivity();
            }
        });
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE || newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            service.getMeetings();
        }

    }

    private void initList() {
        service = DI.getMeetingApiService();
        this.mMeetings = service.getMeetings();
        this.mfilteredMeetings = new ArrayList<>(mMeetings);
        this.mAdapter = new MeetingAdapter(mfilteredMeetings);
        this.binding.recyclerView.setAdapter(this.mAdapter);
        this.binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filter_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_filter1);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Entrez une salle ou un horaire");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        initList();
    }

    @Override
    public void onStart() {
        super.onStart();
        initList();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onDeleteMeeting(DeleteMeetingEvent event) {
        service.deleteMeeting(event.meeting);
        initList();
    }

    private void navigateToAddActivity() {
        Intent addMeetingActivity = new Intent(MainActivity.this, com.dija.mareu1.controllers.AddMeetingActivity.class);
        startActivity(addMeetingActivity);
    }

}