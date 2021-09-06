package com.openclassrooms.mareu.ui.meeting_list;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.events.DeleteMeetingEvent;
import com.openclassrooms.mareu.model.Meeting;

import org.greenrobot.eventbus.EventBus;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import butterknife.BindView;
import butterknife.ButterKnife;

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

 //       Glide.with(holder.mMeetingAvatar.getContext())
 //               .load(meeting.getAvatarUrl())
 //               .apply(RequestOptions.circleCropTransform())
 //               .into(holder.mMeetingAvatar);
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
        @BindView(R.id.item_list_avatar)
        public ImageView mMeetingAvatar;
        @BindView(R.id.item_list_name)
        public TextView mMeetingName;
        @BindView(R.id.item_list_date)
        public TextView mMeetingDate;
        @BindView(R.id.item_list_email)
        public TextView mMeetingEmails;
        @BindView(R.id.item_list_delete_button)
        public ImageButton mDeleteButton;

        private Meeting neighbour;

        public void setNeighbour(Meeting neighbour) {
            this.neighbour = neighbour;
        }

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("neighbour", "click sur un element");
                    v.setEnabled(false);
//                    EventBus.getDefault().post(new DisplayNeighbourEvent(neighbour));
                }
            });
        }
    }
}
