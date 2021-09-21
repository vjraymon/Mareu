package com.openclassrooms.mareu.ui.meeting_list;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.di.DI;
import com.openclassrooms.mareu.events.DeleteMeetingEvent;
import com.openclassrooms.mareu.service.DummyMeetingApiService;
import com.openclassrooms.mareu.service.MeetingApiService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListMeetingActivity extends AppCompatActivity {
    // General
    private Toolbar mToolbar;
    private MeetingApiService mApiService;
    private MeetingFragment fragment;
    private int lastOrientation;
    // Room filtering
    private Spinner spinnerRoomFilter;
    private List<String> arraySpinnerRoomFilter = new ArrayList<>();
    private String roomFilter = DummyMeetingApiService.ALL_ROOMS;
    // Date filtering
    private Button btnDateFilterPicker;
    private Button btnDateClearPicker;
    private String dateBeginFilter = DummyMeetingApiService.NO_DATE_FILTER;
    private String dateEndFilter = DummyMeetingApiService.NO_DATE_FILTER;
    private String dateFilter = DummyMeetingApiService.NO_DATE_FILTER;
    // Button to the activity defining a new meeting
    private Button btnAddMeeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_meeting);
 //       mToolbar = findViewById(R.id.toolbar);
 //       setSupportActionBar(mToolbar);
        Log.i("neighbour","ListMeetingActivity.onCreate reinit getNewInstanceApiService");

        initService(savedInstanceState);
        createDateFilter();
        createRoomFilter();
        createAddMeetingButton();
        createFragment(savedInstanceState);
    }

    private void initService(Bundle savedInstanceState) {
        fragment = null;

        spinnerRoomFilter = findViewById(R.id.spinnerRoom);
        btnDateFilterPicker = findViewById(R.id.btn_dateFilter);
        btnDateClearPicker = findViewById(R.id.btn_dateClear);
        btnAddMeeting = findViewById(R.id.add_meeting);

        spinnerRoomFilter.setEnabled(false);
        btnDateFilterPicker.setEnabled(false);
        btnDateClearPicker.setEnabled(false);
        btnAddMeeting.setEnabled(false);

        if (savedInstanceState != null) {
            mApiService = DI.getMeetingApiService();
        } else {
            lastOrientation = getResources().getConfiguration().orientation;
            resetOnOrientationChanged();
        }
    }

    private void resetOnOrientationChanged() {
        mApiService = DI.getNewInstanceApiService();
        initRoomFilter(DummyMeetingApiService.ALL_ROOMS);
        initDateFilter(DummyMeetingApiService.NO_DATE_FILTER);
    }

    private void createFragment(Bundle savedInstanceState) {
        if (findViewById(R.id.container) != null) {
            Log.i("neighbour", "ListMeetingActivity.onCreate fragment");
            mApiService.registerFilter(roomFilter, dateBeginFilter, dateEndFilter);
            fragment = new MeetingFragment().newInstance();
            if (fragment == null) return;

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();

            spinnerRoomFilter.setEnabled(true);
            btnDateFilterPicker.setEnabled(true);
            btnDateClearPicker.setEnabled(true);
            btnAddMeeting.setEnabled(true);
        }
    }

    private void createDateFilter() {
        Log.i("neighbour","ListMeetingActivity.onCreate init txtDateFilter");
        initDateFilter(DummyMeetingApiService.NO_DATE_FILTER);
        btnDateFilterPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                Context context = v.getContext();
                context.setTheme(R.style.AppTheme_NoActionBar); // set white text to black for visible dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                    context,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Log.i("neighbour","ListMeetingActivity.onDateSet DatePicker");
                            initDateFilter(DummyMeetingApiService.convertyyyyMMddToString(year, monthOfYear+1, dayOfMonth));
                            mApiService.registerFilter(roomFilter, dateBeginFilter, dateEndFilter);
                            fragment.initList();
                        }
                    },
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        btnDateClearPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDateFilter(DummyMeetingApiService.NO_DATE_FILTER);
                mApiService.registerFilter(roomFilter, dateBeginFilter, dateEndFilter);
                fragment.initList();
            }
        });
    }

    private void initDateFilter(String s) {
        Log.i("neighbour","ListMeetingActivity.initDateFilter txtDateFilter.setText " + s);
        if (s.equals(DummyMeetingApiService.NO_DATE_FILTER)) {
            Log.i("neighbour","ListMeetingActivity.initDateFilter dummy" + s);
            dateFilter = s;
            dateBeginFilter = s;
            dateEndFilter = s;
        } else {
            Log.i("neighbour","ListMeetingActivity.initDateFilter valid date " + s);
            dateFilter = s;
            dateBeginFilter = DummyMeetingApiService.convertDateAndTimeToString(s, DummyMeetingApiService.convertHHmmToString(0,0));
            dateEndFilter = DummyMeetingApiService.convertDateAndTimeToString(s, DummyMeetingApiService.convertHHmmToString(23,59));
        }
        TextView txtDateFilter = findViewById(R.id.in_dateFilter);
        txtDateFilter.setText(dateFilter);
    }

    private void createRoomFilter() {
        initRoomFilter(roomFilter);
        spinnerRoomFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("neighbour","ListMeetingActivity.onItemSelected");
                TextView selectedText = (TextView)parent.getChildAt(0);
                if (selectedText != null) selectedText.setTextColor(Color.WHITE);
                roomFilter = arraySpinnerRoomFilter.get(position);
                Log.i("neighbour","ListMeetingActivity.onItemSelected " + roomFilter + " " + dateBeginFilter + " " + dateEndFilter);
                mApiService.registerFilter(roomFilter, dateBeginFilter, dateEndFilter);
                fragment.initList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initRoomFilter(String room) {
        roomFilter = room;
        if (room.equals(DummyMeetingApiService.ALL_ROOMS)) {
            initArraySpinnerRoomFilter(mApiService.getRooms());
        } else {
            List<String> array_spinner_new = mApiService.getRooms();
            Log.i("neighbour", "ListMeetingActivity.initRoomSpinner");
            if (arraySpinnerRoomFilter.size() != array_spinner_new.size()) {
                initArraySpinnerRoomFilter(array_spinner_new);
                roomFilter = DummyMeetingApiService.ALL_ROOMS;
            }
        }
    }

    private void initArraySpinnerRoomFilter(List<String> array_spinner_new) {
        arraySpinnerRoomFilter = array_spinner_new;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.room_filter_spinner_item, arraySpinnerRoomFilter);
        spinnerRoomFilter.setAdapter(adapter);
    }

    private void createAddMeetingButton() {
        btnAddMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMeetingActivity.navigate((Activity) v.getContext());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this); // to resgister the delete meeting treatment

        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == lastOrientation) {
            Log.i("neighbour", "ListMeetingActivity.onStart");
            initRoomFilter(roomFilter); // case of new room meeting
        } else {
            resetOnOrientationChanged();
            lastOrientation = currentOrientation;
        }

        mApiService.registerFilter(roomFilter, dateBeginFilter, dateEndFilter);
        fragment.initList();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * Fired if the user clicks on a delete button
     * @param event
     */
    @Subscribe
    public void onDeleteMeeting(DeleteMeetingEvent event) {
        Log.i("neighbour","onDeleteMeeting " + event.meeting.getRoom());
        mApiService.deleteMeeting(event.meeting);

        initRoomFilter(roomFilter);
        initDateFilter(dateFilter);
        mApiService.registerFilter(roomFilter, dateBeginFilter, dateEndFilter);
        fragment.initList();
    }
}