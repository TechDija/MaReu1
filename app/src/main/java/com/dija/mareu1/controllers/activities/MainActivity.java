package com.dija.mareu1.controllers.activities;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity implements FragmentRoomFilter.OnInputSelected, FragmentTimeFilter.OnInputSelected, MeetingAdapter.OnItemClickListener {
    public static final String SUBJECT = "réunion X";
    public static final long TIME = 0;
    public static final String ROOM = "roomName";
    public static final String PARTICIPANT = "@lamzone.com";
    long tag;
    long tag1;

    ActivityMainBinding binding;
    private MeetingAdapter mAdapter;
    private MeetingApiService service;
    private List<Meeting> mMeetings;
    private List<Meeting> mfilteredMeetings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setSupportActionBar(binding.toolbarMainActivity);
        binding.toolbarMainActivity.setTitleTextColor(getResources().getColor(R.color.white));
        service = DI.getMeetingApiService(); // <= changing for POC mode
        initList();


        binding.addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToAddActivity();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filter_menu, menu);
        return true;
    }

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

    //---------------
    //ACTIONS
    //---------------

    private void initList() {
        this.mMeetings = service.getMeetings();
        this.mfilteredMeetings = new ArrayList<>(mMeetings);
        this.mAdapter = new MeetingAdapter(mfilteredMeetings);
        this.binding.recyclerView.setAdapter(this.mAdapter);
        this.binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ifDataSetIsEmpty();
        mAdapter.setOnItemClickListener(MainActivity.this);
    }

    private void navigateToAddActivity() {
        Intent addMeetingActivity = new Intent(MainActivity.this, AddMeetingActivity.class);
        startActivity(addMeetingActivity);
    }

    private void ifDataSetIsEmpty() {
        if (mMeetings.isEmpty()) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.emptyTextview.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.emptyTextview.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, EditActivity.class);
        Meeting clickedMeeting = mfilteredMeetings.get(position);

        detailIntent.putExtra(SUBJECT, clickedMeeting.getSubject());
        detailIntent.putExtra(String.valueOf(TIME), clickedMeeting.getBeginningDateTime());
        detailIntent.putExtra(ROOM, clickedMeeting.getRoom());
        detailIntent.putExtra(PARTICIPANT, clickedMeeting.getPeople());

        startActivity(detailIntent);
    }

    @Override
    public void sendInput(String filteredData) {
        mAdapter.getFilter().filter(filteredData);
    }

    @Override
    public void sendInput(long tag, long tag1) {
        this.tag = tag;
        this.tag1 = tag1;
        mAdapter.timeFilter(tag, tag1);
    }
}