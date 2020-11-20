package com.dija.mareu1.controllers.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.dija.mareu1.DI.DI;
import com.dija.mareu1.R;
import com.dija.mareu1.databinding.RoomFilterFragmentBinding;
import com.dija.mareu1.events.RoomFilterEvent;
import com.dija.mareu1.model.Room;
import com.dija.mareu1.service.MeetingApiService;
import com.google.android.material.chip.Chip;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class FragmentRoomFilter extends DialogFragment {
    private RoomFilterFragmentBinding binding;

    private List<String> selectedChipData;
    private MeetingApiService service;
    private List<String> roomsNames;

    //-------------------------------
    //ON CREATE VIEW
    //-------------------------------
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RoomFilterFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        selectedChipData = new ArrayList<>();
        service = DI.getMeetingApiService();

        populateChipGroup();

        //--------NEGATIVE BUTTON-----------------
        binding.negativeActionButton.setOnClickListener(v -> getDialog().dismiss());

        //--------NEUTRAL BUTTON-----------------
        binding.neutralActionButton.setOnClickListener(v -> neutralButtonActions());

        //--------POSITIVE BUTTON-----------------
        binding.positiveActionButton.setOnClickListener(v -> positiveButtonActions());

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

    //----------------------------
    //ACTIONS
    //----------------------------
    private void populateChipGroup() {
        List<Room> rooms = service.getAllRooms();
        for (Room room : rooms) {
            View view = getLayoutInflater().inflate(R.layout.chip_layout, null);
            Chip chip = (Chip) view;
            chip.setText(room.getRoomName());
            chip.setChecked(room.getRoomTag());
            chip.setOnCheckedChangeListener(chipsOnCheckedChangeListener());
            binding.chipGroupRooms.addView(chip);
        }
    }

    private Chip.OnCheckedChangeListener chipsOnCheckedChangeListener() {
        CompoundButton.OnCheckedChangeListener checkedChangeListener = (buttonView, isChecked) -> {
            List<Room> rooms = service.getAllRooms();
            if (isChecked) {
                selectedChipData.add(buttonView.getText().toString());
                for (Room room : rooms) {
                    if (buttonView.getText().toString().contains(room.getRoomName())) {
                        room.setRoomTag(true);
                    }
                }
            } else {
                selectedChipData.remove(buttonView.getText().toString());
                for (Room room : rooms) {
                    if (buttonView.getText().toString().contains(room.getRoomName())) {
                        room.setRoomTag(false);
                    }
                }
            }
        };
        return checkedChangeListener;
    }

    private void neutralButtonActions() {
        roomsNames = service.getAllRoomNames();
        EventBus.getDefault().post(new RoomFilterEvent(roomsNames.toString()));
        getDialog().dismiss();
    }

    private void positiveButtonActions() {
        EventBus.getDefault().post(new RoomFilterEvent(selectedChipData.toString()));
        getDialog().dismiss();
    }
}
