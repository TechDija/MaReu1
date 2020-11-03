package com.dija.mareu1.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.dija.mareu1.DI.DI;
import com.dija.mareu1.Model.Meeting;
import com.dija.mareu1.R;
import com.dija.mareu1.Service.MeetingApiService;
import com.dija.mareu1.View.MeetingAdapter;
import com.dija.mareu1.databinding.ActivityMainBinding;
import com.dija.mareu1.events.DeleteMeetingEvent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FilterFragment.OnInputSelected {
    ActivityMainBinding binding;
    private MeetingAdapter mAdapter;
    private MeetingApiService service;
    private List<Meeting> mMeetings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        service = DI.getMeetingApiService();
        configureRecyclerView();

        // FAB click listener towards AddMeeting_Activity
        binding.addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addMeetingActivity = new Intent(MainActivity.this, com.dija.mareu1.Controllers.AddMeetingActivity.class);
                startActivity(addMeetingActivity);
            }
        });

        // Widget click listener towards filterFragment
        binding.filterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterFragment dialog = new FilterFragment();
                dialog.show(getSupportFragmentManager(), "FilterFragment");
            }
        });
    }

    private void configureRecyclerView() {
        this.mMeetings = service.getMeetings();
        this.mAdapter = new MeetingAdapter(mMeetings);
        this.binding.recyclerView.setAdapter(this.mAdapter);
        this.binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_filter1) {
            Toast.makeText(this, "coucou", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
    @Override
    public void onResume() {
        super.onResume();
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

    @Subscribe
    public void onDeleteMeeting(DeleteMeetingEvent event) {
        service.deleteMeeting(event.meeting);
    }

    @Override
    public void sendInput(String data) {
    mAdapter.getFilter().filter(data);
}
}