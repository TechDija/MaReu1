package com.dija.mareu1.controllers.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dija.mareu1.DI.DI;
import com.dija.mareu1.R;
import com.dija.mareu1.databinding.AddRoomFragmentBinding;
import com.dija.mareu1.model.Room;
import com.dija.mareu1.service.MeetingApiService;

import java.util.ArrayList;
import java.util.List;

public class FragmentAddRoom extends Fragment implements AdapterView.OnItemSelectedListener {
    private AddRoomFragmentBinding binding;
    private FirstFragmentListener mListener;

    private MeetingApiService service;
    private List<Room> availableRooms;
    private String occupiedRoomName;

    //INTERFACE
    public interface FirstFragmentListener{
        void onFirstFragmentSent(String occupiedRoomName);
    }

    // CONSTRUCTOR

    public FragmentAddRoom() {
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AddRoomFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    // RECOVERING DATA
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = DI.getMeetingApiService();
        availableRooms = service.getAllRooms();
        getAvailableRoomNames();
    }
        // DISPLAYING DATA
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRoomSpinner();

    }

        @Override
        public void onItemSelected (AdapterView < ? > parent, View view,int position, long id){
        occupiedRoomName = parent.getItemAtPosition(position).toString();
        mListener.onFirstFragmentSent(occupiedRoomName);

            for (Room room : availableRooms) {
                if (occupiedRoomName.contains(room.getRoomName())) {
                    binding.roomImage.setImageResource(getImageId(getContext(), room.getRoomImage()));
                }
            }
    }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FirstFragmentListener) {
            mListener = (FirstFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement FirstFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //------------------
    // ACTIONS
    //------------------
    private void setRoomSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, getAvailableRoomNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.roomSpinner.setAdapter(adapter);
        binding.roomSpinner.setOnItemSelectedListener(this);
    }


    private List<String> getAvailableRoomNames() {
        List<String> availableRoomNames = new ArrayList<>();
        for (Room room : availableRooms) {
            String name = room.getRoomName();
            availableRoomNames.add(name);
        }
        return availableRoomNames;
    }

    public static int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }
}
