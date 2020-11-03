package com.dija.mareu1.Controllers;

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

import com.dija.mareu1.DI.DI;
import com.dija.mareu1.R;
import com.dija.mareu1.Service.MeetingApiService;
import com.dija.mareu1.databinding.FilterFragmentBinding;
import com.dija.mareu1.databinding.FilterFragmentBinding;

import java.util.ArrayList;

public class FilterFragment extends DialogFragment {

    // INTERFACE

    public interface OnInputSelected{
       void sendInput(String data);
   }

   // DECLARATIONS

    private FilterFragmentBinding binding;
    public OnInputSelected mOnInputSelected;
    private ArrayList<String> selectedChipData;

    // OVERRIDING INTERFACE METHOD

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mOnInputSelected = (OnInputSelected) getActivity();
        } catch (ClassCastException e){
            Log.e("TAG", "onAttach ClassCastException: "+ e.getMessage());
        }
    }

    // ONCREATEVIEW
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FilterFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
         selectedChipData = new ArrayList<>();

         // OnCheckedChange Listeners for each chip

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
        binding.DaisyChip.setOnCheckedChangeListener(checkedChangeListener);
        binding.PeachChip.setOnCheckedChangeListener(checkedChangeListener);
        binding.ToadChip.setOnCheckedChangeListener(checkedChangeListener);
        binding.BowserChip.setOnCheckedChangeListener(checkedChangeListener);
        binding.YoshiChip.setOnCheckedChangeListener(checkedChangeListener);
        binding.KoopaChip.setOnCheckedChangeListener(checkedChangeListener);
        binding.WarioChip.setOnCheckedChangeListener(checkedChangeListener);
        binding.DKChip.setOnCheckedChangeListener(checkedChangeListener);

        // configuring behavior of the three bottom buttons
        binding.negativeActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        binding.neutralActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnInputSelected != null) {
                    mOnInputSelected.sendInput("");
                }
                getDialog().dismiss();
            }
        });

        binding.positiveActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnInputSelected != null) {
                    mOnInputSelected.sendInput(selectedChipData.toString());
                }
                getDialog().dismiss();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ConstraintLayout.LayoutParams.MATCH_PARENT;
        params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }
}
