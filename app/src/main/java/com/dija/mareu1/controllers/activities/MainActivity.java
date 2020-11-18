package com.dija.mareu1.controllers.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dija.mareu1.DI.DI;
import com.dija.mareu1.R;
import com.dija.mareu1.controllers.fragments.FragmentRoomFilter;
import com.dija.mareu1.controllers.fragments.FragmentTimeFilter;
import com.dija.mareu1.databinding.ActivityMainBinding;
import com.dija.mareu1.events.DeleteMeetingEvent;
import com.dija.mareu1.model.Meeting;
import com.dija.mareu1.service.MeetingApiService;
import com.dija.mareu1.view.MeetingAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FragmentRoomFilter.OnInputSelected, FragmentTimeFilter.OnInputSelected {

    ActivityMainBinding binding;
    private MeetingAdapter mAdapter;
    private MeetingApiService service;
    private List<Meeting> mMeetings;

    //----------------------------
    //RECEIVING DATA FROM INTERFACES
    //-----------------------------
    @Override
    public void sendInput(String filteredData) {
        mAdapter.getFilter().filter(filteredData);
    }

    @Override
    public void sendInput(long tag, long tag1) {
        mAdapter.timeFilter(tag, tag1);
    }

    //---------------------------
    //ON CREATE
    //---------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.toolbarMainActivity);

        service = DI.getMeetingApiService();
        initList();


        binding.addFab.setOnClickListener(v -> navigateToAddActivity());
    }


    //---------------------------
    //MENU CONFIGURATION
    //---------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filter_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter1:
                return true;
            case R.id.menu_filter2:
                FragmentRoomFilter dialog = new FragmentRoomFilter();
                dialog.show(getSupportFragmentManager(), "FragmentRoomFilter");
                return true;
            case R.id.menu_filter3:
                FragmentTimeFilter dial = new FragmentTimeFilter();
                dial.show(getSupportFragmentManager(), "FragmentTimeFilter");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //---------------------------
    //LIFECYCLE
    //---------------------------
    @Override
    public void onResume() {
        super.onResume();
        initList();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    //---------------------------
    //EVENT BUS SUBSCRIPTION
    //---------------------------
    @Subscribe
    public void onDeleteMeeting(DeleteMeetingEvent event) {
        service.deleteMeeting(event.meeting);
        initList();
    }

    //---------------
    //ACTIONS
    //---------------
    private void initList() {
        this.mMeetings = service.getMeetings();
        List<Meeting> mfilteredMeetings = new ArrayList<>(mMeetings);
        this.mAdapter = new MeetingAdapter(mfilteredMeetings);
        this.binding.recyclerView.setAdapter(this.mAdapter);
        this.binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ifDataSetIsEmpty();
    }

    private void navigateToAddActivity() {
        Intent addMeetingActivity = new Intent(MainActivity.this, AddMeetingActivity.class);
        startActivity(addMeetingActivity);
    }

    private void ifDataSetIsEmpty() {
        if (mMeetings.isEmpty()) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.emptyTextview.setVisibility(View.VISIBLE);
            binding.emptyImageview.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.emptyTextview.setVisibility(View.GONE);
            binding.emptyImageview.setVisibility(View.GONE);
        }
    }

}