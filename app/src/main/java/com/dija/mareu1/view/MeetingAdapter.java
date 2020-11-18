package com.dija.mareu1.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dija.mareu1.DI.DI;
import com.dija.mareu1.R;
import com.dija.mareu1.controllers.fragments.FragmentAddRoom;
import com.dija.mareu1.databinding.ItemMeetingBinding;
import com.dija.mareu1.events.DeleteMeetingEvent;
import com.dija.mareu1.model.Meeting;
import com.dija.mareu1.model.Room;
import com.dija.mareu1.service.MeetingApiService;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MeetingViewHolder> implements Filterable {
    private final List<Meeting> mfilteredMeetings;
    private final MeetingApiService service;
    private final List<Meeting> mMeetings;

    //-------------------------
    //CONSTRUCTOR
    //-------------------------

    public MeetingAdapter(List<Meeting> meetings) {
        this.mfilteredMeetings = meetings;
        mMeetings = new ArrayList<>(mfilteredMeetings);
        service = DI.getMeetingApiService();
    }

    //-------------------------
    //ON CREATE
    //-------------------------
    @NonNull
    @Override
    public MeetingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_meeting, parent, false);
        return new MeetingViewHolder(ItemMeetingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    //-------------------------
    //ON BIND
    //-------------------------

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

    //-------------------------
    //ITEM COUNT
    //-------------------------
    @Override
    public int getItemCount() {
        return this.mfilteredMeetings.size();
    }

    //-------------------------
    //FILTERS
    //-------------------------
    @Override
    public Filter getFilter() {
        return Filter;
    }

    private final Filter Filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            results.values = service.roomFilter(constraint);
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mfilteredMeetings.clear();
            mfilteredMeetings.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public void timeFilter(long tag, long tag1) {
        mfilteredMeetings.clear();
        mfilteredMeetings.addAll(service.timeFilterService(tag, tag1));
        notifyDataSetChanged();
    }

    //-------------------------
    //VIEWHOLDER
    //-------------------------
    public class MeetingViewHolder extends RecyclerView.ViewHolder {
        ItemMeetingBinding binding;
        MeetingApiService service;

        public MeetingViewHolder(ItemMeetingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            service = DI.getMeetingApiService();
        }

        @SuppressLint("SetTextI18n")
        public void updateWithMeeting(Meeting meeting) {
            Context c = binding.firstLineItem.getContext();
            String date = convertDate(meeting.getBeginningDateTime().toString(), "kk:mm");
            for (Room room : service.getAllRooms()) {
                if (meeting.getRoom() == room.getRoomName()) {
                    binding.imageColorItem.setImageResource(FragmentAddRoom.getImageId(c, room.getRoomImage()));
                }
            }
            binding.firstLineItem.setText(meeting.getSubject() + " - " + date + " - " + meeting.getRoom());
            binding.secondLineItem.setText(meeting.getPeople());
        }

        private String convertDate(String dateInMilliseconds, String dateFormat) {
            return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
        }
    }
}





