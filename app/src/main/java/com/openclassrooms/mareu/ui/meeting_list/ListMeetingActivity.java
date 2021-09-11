package com.openclassrooms.mareu.ui.meeting_list;

import android.app.DatePickerDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class ListMeetingActivity extends AppCompatActivity implements View.OnClickListener {

    // UI Components
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    List<String> array_spinner = new ArrayList<>();
    private MeetingApiService mApiService;
    private MeetingFragment fragment;
    private String roomFilter = DummyMeetingApiService.ALL_ROOMS;
    private String dateBeginFilter = DummyMeetingApiService.NO_DATE_FILTER;
    private String dateEndFilter = DummyMeetingApiService.NO_DATE_FILTER;
    @BindView(R.id.btn_dateFilter)
    Button btnDateFilterPicker;
    @BindView(R.id.btn_dateClear)
    Button btnDateClearPicker;
//    @BindView(R.id.in_dateFilter)
    TextView txtDateFilter;
    String txtDateString = DummyMeetingApiService.NO_DATE_FILTER;
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_list_meeting);
         ButterKnife.bind(this);

         setSupportActionBar(mToolbar);
         Log.i("neighbour","ListMeetingActivity.onCreate reinit getNewInstanceApiService");
         mApiService = DI.getMeetingApiService();
         initRoomSpinner();

         Log.i("neighbour","ListMeetingActivity.onCreate init txtDateFilter");

         txtDateFilter = findViewById(R.id.in_dateFilter);
         btnDateFilterPicker.setOnClickListener(this);
         btnDateClearPicker.setOnClickListener(this);
         initDateFilter(DummyMeetingApiService.NO_DATE_FILTER);
         txtDateFilter.setText(txtDateString);

         if (findViewById(R.id.container) != null) {
            if (savedInstanceState != null) {
                mApiService = DI.getNewInstanceApiService();
                roomFilter = DummyMeetingApiService.ALL_ROOMS;
                initRoomSpinner();
                initDateFilter(DummyMeetingApiService.NO_DATE_FILTER);
            }

            Log.i("neighbour","ListMeetingActivity.onCreate fragment");
            fragment = new MeetingFragment().newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
         }


    }

    @Override
    public void onClick(View v)
    {
        if (v == btnDateFilterPicker) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Log.i("neighbour","ListMeetingActivity.onDateSet DatePicker");
                            initDateFilter(year + "." + (monthOfYear+1) + "." + dayOfMonth);
                            txtDateFilter.setText(txtDateString);
                            fragment.initList();
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnDateClearPicker) {
            initDateFilter(DummyMeetingApiService.NO_DATE_FILTER);
            txtDateFilter.setText(txtDateString);
            fragment.initList();
         }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        Log.i("neighbour","ListMeetingActivity.onStart");
        initRoomSpinner();
        fragment.initList();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initDateFilter(String s) {
        Log.i("neighbour","ListMeetingActivity.initDateFilter txtDateFilter.setText " + s);
        if (s.equals(DummyMeetingApiService.NO_DATE_FILTER)) {
            Log.i("neighbour","ListMeetingActivity.initDateFilter dummy" + s);
            txtDateString = s;
            dateBeginFilter = s;
            dateEndFilter = s;
        } else {
            Log.i("neighbour","ListMeetingActivity.initDateFilter valid date " + s);
            txtDateString = s;
            dateBeginFilter = s + " 0.0";
            dateEndFilter = s + " 23.59";
        }
        mApiService.registerFilter(roomFilter, dateBeginFilter, dateEndFilter);
    }

    public void initRoomSpinner() {
        List<String> array_spinner_new = mApiService.getRooms();
        Log.i("neighbour","ListMeetingActivity.initRoomSpinner");
        if (array_spinner.size() != array_spinner_new.size()) {
            array_spinner = array_spinner_new;
            Spinner s = (Spinner) findViewById(R.id.spinnerRoom);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, array_spinner);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            s.setAdapter(adapter);
            roomFilter = DummyMeetingApiService.ALL_ROOMS;
        }

        mApiService.registerFilter(roomFilter, dateBeginFilter, dateEndFilter);
    }

    /**
     * Fired if the user clicks on a delete button
     * @param event
     */
    @Subscribe
    public void onDeleteNeighbour(DeleteMeetingEvent event) {
        Log.i("neighbour","onDeleteNeighbour " + event.meeting.getRoom());
        mApiService.deleteMeeting(event.meeting);
        initRoomSpinner();
        initDateFilter(txtDateString);
        fragment.initList();
    }

    @OnItemSelected(R.id.spinnerRoom)
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Log.i("neighbour","ListMeetingActivity.onItemSelected");
        TextView selectedText = (TextView)parent.getChildAt(0);
        if (selectedText != null) selectedText.setTextColor(Color.WHITE);
        roomFilter = array_spinner.get(pos);
        Log.i("neighbour","ListMeetingActivity.onItemSelected " + roomFilter + " " + dateBeginFilter + " " + dateEndFilter);
        mApiService.registerFilter(roomFilter, dateBeginFilter, dateEndFilter);
        fragment.initList();
    }

    @OnClick(R.id.add_meeting)
    void addMeeting() {
        AddMeetingActivity.navigate(this);
    }
}