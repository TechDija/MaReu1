package com.dija.mareu1.view;

import android.text.format.DateFormat;

import androidx.recyclerview.widget.RecyclerView;

import com.dija.mareu1.model.Meeting;
import com.dija.mareu1.databinding.ItemMeetingBinding;
import com.dija.mareu1.utils.getColouredImage;

public class MeetingViewHolder extends RecyclerView.ViewHolder {
    ItemMeetingBinding binding;


    public MeetingViewHolder(ItemMeetingBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
    public void updateWithMeeting(Meeting meeting){
        binding.imageColorItem.setImageResource(getColouredImage.getImage());
        binding.firstLineItem.setText(meeting.getSubject()+" - "+ convertDate(meeting.getBeginningDateTime().toString(), "kk:mm") +" - "+meeting.getRoom());
        binding.secondLineItem.setText(meeting.getPeople());
    }
    public static String convertDate(String dateInMilliseconds,String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }
}
