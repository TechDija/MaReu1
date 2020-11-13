package com.dija.mareu1.controllers.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.dija.mareu1.databinding.RoomFilterFragmentBinding;

import java.util.ArrayList;

public class FragmentRoomFilter extends DialogFragment {

    private RoomFilterFragmentBinding binding;
    public OnInputSelected mOnInputSelected;

    private ArrayList<String> selectedChipData;

    // INTERFACE

    public interface OnInputSelected{
        void sendInput(String filteredData);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RoomFilterFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        selectedChipData = new ArrayList<>();

        chipsOnCheckedChangeListener();

        binding.negativeActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { getDialog().dismiss(); }});

        binding.neutralActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { neutralButtonActions(); }});

        binding.positiveActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { positiveButtonActions(); }});

        return view;
    }

    //----------------------------
    //ACTIONS
    //----------------------------

    private void chipsOnCheckedChangeListener() {
        CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedChipData.add(buttonView.getText().toString());
                } else {
                    selectedChipData.remove(buttonView.getText().toString());
                }
            }
        } ;
        binding.MarioChip.setOnCheckedChangeListener(checkedChangeListener);
        binding.LuigiChip.setOnCheckedChangeListener(checkedChangeListener);
        binding.BowserChip.setOnCheckedChangeListener(checkedChangeListener);
        binding.BowserChip.setOnCheckedChangeListener(checkedChangeListener);
        binding.WarioChip.setOnCheckedChangeListener(checkedChangeListener);
        binding.KoopaChip.setOnCheckedChangeListener(checkedChangeListener);
        binding.DKChip.setOnCheckedChangeListener(checkedChangeListener);
        binding.DaisyChip.setOnCheckedChangeListener(checkedChangeListener);
        binding.PeachChip.setOnCheckedChangeListener(checkedChangeListener);
        binding.ToadChip.setOnCheckedChangeListener(checkedChangeListener);
    }

    private void neutralButtonActions() {
        if (mOnInputSelected != null) {
            mOnInputSelected.sendInput("Mario, Luigi, Bowser, Peach, Daisy, Koopa, Donkey-Kong, Wario, Toad, Yoshi");
        }
        getDialog().dismiss();
    }

    private void positiveButtonActions(){
        if (mOnInputSelected != null) {
            mOnInputSelected.sendInput(selectedChipData.toString());
        }
        getDialog().dismiss();
    }


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
            mOnInputSelected = (OnInputSelected) getActivity();
        } catch (ClassCastException e){
            Log.e("TAG", "onAttach ClassCastException: "+ e.getMessage());
        }
    }
}
