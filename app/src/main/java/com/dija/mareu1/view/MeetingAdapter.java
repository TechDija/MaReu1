package com.dija.mareu1.view;


import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dija.mareu1.model.Meeting;
import com.dija.mareu1.R;
import com.dija.mareu1.databinding.ItemMeetingBinding;
import com.dija.mareu1.events.DeleteMeetingEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MeetingAdapter extends RecyclerView.Adapter<MeetingViewHolder> implements Filterable {
    private List<Meeting> mfilteredMeetings;
    private List<Meeting> mMeetings;
    private Context context;
    long input1;
    long input2;

    public MeetingAdapter(List<Meeting> meetings) {
        this.mfilteredMeetings = meetings;
        mMeetings = new ArrayList<>(mfilteredMeetings);
    }

    @NonNull
    @Override
    public MeetingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_meeting, parent, false);
        return new MeetingViewHolder(ItemMeetingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MeetingViewHolder holder, int position) {
        holder.updateWithMeeting(this.mfilteredMeetings.get(position));

        holder.binding.imageBinItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new DeleteMeetingEvent(mfilteredMeetings.get(position)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mfilteredMeetings.size();
    }

    @Override
    public Filter getFilter() {
        return Filter;
    }

    private Filter Filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Meeting> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mMeetings);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Meeting meeting : mMeetings) {
                    CharSequence strDate = DateFormat.format("kk:mm", Long.parseLong(meeting.getBeginningDateTime().toString()));
                    if (strDate.toString().toLowerCase().contains(filterPattern) || meeting.getRoom().toLowerCase().contains(filterPattern)) {
                        filteredList.add(meeting);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mfilteredMeetings.clear();
            mfilteredMeetings.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}





