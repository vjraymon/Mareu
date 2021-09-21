package com.openclassrooms.mareu.ui.meeting_list;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.di.DI;
import com.openclassrooms.mareu.events.DeleteMeetingEvent;
import com.openclassrooms.mareu.model.Meeting;
import com.openclassrooms.mareu.service.DummyMeetingApiService;
import com.openclassrooms.mareu.service.DummyMeetingGenerator;
import com.openclassrooms.mareu.service.MeetingApiService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;


public class MeetingFragment extends Fragment {

    private MeetingApiService mApiService;
    private List<Meeting> mMeetings;
    private RecyclerView mRecyclerView;

    /**
     * Create and return a new instance
     * @return @{@link MeetingFragment}
     */
    public static MeetingFragment newInstance()
    {
        MeetingFragment fragment = new MeetingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("neighbour","MeetingFragment.onCreate");
        mApiService = DI.getMeetingApiService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_list, container, false);
        Log.i("neighbour","MeetingFragment.onCreateView");
        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        initList();
        return view;
    }

    /**
     * Init the List of meetings
     */
    public void initList() {
        Log.i("neighbour","MeetingFragment.initlist");
        mApiService = DI.getMeetingApiService();
        mMeetings = mApiService.getFilteredMeetings();
        mRecyclerView.setAdapter(new MyMeetingRecyclerViewAdapter(mMeetings));
    }

    @Override
    public void onStart() {
        super.onStart();
        initList();
    }
}
