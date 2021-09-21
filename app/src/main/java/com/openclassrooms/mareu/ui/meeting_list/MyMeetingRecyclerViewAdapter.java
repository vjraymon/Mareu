package com.openclassrooms.mareu.ui.meeting_list;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.events.DeleteMeetingEvent;
import com.openclassrooms.mareu.model.Meeting;

import org.greenrobot.eventbus.EventBus;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.List;

class MyMeetingRecyclerViewAdapter extends RecyclerView.Adapter<MyMeetingRecyclerViewAdapter.ViewHolder> {

    private final List<Meeting> mMeetings;

    public MyMeetingRecyclerViewAdapter(List<Meeting> items) {
        mMeetings = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_meeting, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Meeting meeting = mMeetings.get(position);
        holder.setNeighbour(meeting);

        holder.mMeetingName.setText(meeting.getRoom());

        holder.mMeetingSubject.setText(meeting.getSubject());

        holder.mMeetingAvatar.setColorFilter(meeting.getColor());

        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        holder.mMeetingDate.setText(simpleDateFormat.format(meeting.getDate()));

        String displayEmails = "";
        if (meeting.getEmails().size() > 0) displayEmails = meeting.getEmails().get(0);
        for (int i=1; i<meeting.getEmails().size(); i++) displayEmails = displayEmails + ", " + meeting.getEmails().get(i);
            holder.mMeetingEmails.setText(displayEmails);

        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new DeleteMeetingEvent(meeting));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMeetings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mMeetingAvatar;
        public TextView mMeetingSubject;
        public TextView mMeetingName;
        public TextView mMeetingDate;
        public TextView mMeetingEmails;
        public ImageButton mDeleteButton;

        private Meeting meeting;

        public void setNeighbour(Meeting meeting) {
            this.meeting = meeting;
        }

        public ViewHolder(View view) {
            super(view);
            mMeetingAvatar = view.findViewById(R.id.item_list_avatar);
            mMeetingSubject = view.findViewById(R.id.item_list_subject);
            mMeetingName = view.findViewById(R.id.item_list_name);
            mMeetingDate = view.findViewById(R.id.item_list_date);
            mMeetingEmails = view.findViewById(R.id.item_list_email);
            mDeleteButton = view.findViewById(R.id.item_list_delete_button);
            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("neighbour", "click sur un element");
                    v.setEnabled(false);
                    EventBus.getDefault().post(new DeleteMeetingEvent(meeting));
                }
            });
        }
    }
}
