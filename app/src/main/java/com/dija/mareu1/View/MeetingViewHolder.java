package com.dija.mareu1.View;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dija.mareu1.Model.Meeting;
import com.dija.mareu1.R;
import com.dija.mareu1.databinding.ItemMeetingBinding;

import java.text.SimpleDateFormat;

public class MeetingViewHolder extends RecyclerView.ViewHolder {
    ItemMeetingBinding binding;


    public MeetingViewHolder(ItemMeetingBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
    public void updateWithMeeting(Meeting meeting){
        binding.imageColorItem.setImageResource(getColouredImage.getImage());
        binding.firstLineItem.setText(meeting.getSubject()+" - "+ meeting.getBeginningTime() +" - "+meeting.getRoom());
        binding.secondLineItem.setText(meeting.getPeople());
    }
}
