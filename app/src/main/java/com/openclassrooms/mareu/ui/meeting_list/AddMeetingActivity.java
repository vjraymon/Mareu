package com.openclassrooms.mareu.ui.meeting_list;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.di.DI;
import com.openclassrooms.mareu.model.Meeting;
import com.openclassrooms.mareu.service.DummyMeetingGenerator;
import com.openclassrooms.mareu.service.MeetingApiService;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddMeetingActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.roomLyt)
    TextInputLayout roomInput;
    @BindView(R.id.emailsLyt)
    TextInputLayout emailsInput;
    @BindView(R.id.create)
    MaterialButton addButton;
    @BindView(R.id.btn_date)
    Button btnDatePicker;
    @BindView(R.id.in_date)
    TextView txtDate;
    @BindView(R.id.btn_time)
    Button btnTimePicker;
    @BindView(R.id.in_time)
    TextView txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;

    private MeetingApiService mApiService;
    private int color = Color.WHITE;
    private Meeting inputMeeting;

    private void checkInputsValid() {
        boolean roomValid = !roomInput.getEditText().getText().equals("");
        boolean dateValid = !txtDate.getText().toString().isEmpty();
        boolean timeValid = !txtTime.getText().toString().isEmpty();
        if (!(roomValid && dateValid && timeValid)) {
            addButton.setEnabled(false);
            return;
        }

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH.mm");
            String stringDate = txtDate.getText().toString() + " " + txtTime.getText().toString();
            inputMeeting = new Meeting(
                    color,
                    roomInput.getEditText().getText().toString(),
                    simpleDateFormat.parse(stringDate),
                    Arrays.asList(emailsInput.getEditText().getText().toString())
            );
            addButton.setEnabled(!mApiService.isMeetingAlreadyExists(inputMeeting));
        } catch (Exception e) {
            addButton.setEnabled(false);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);
        ButterKnife.bind(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        txtDate.setFocusable(false);
        txtTime.setFocusable(false);
        Log.i("neighbour","AddMeetingActivity.onCreate");
        mApiService = DI.getMeetingApiService();
        init();
    }

    @Override
    public void onClick(View v)
    {
        final Calendar c = Calendar.getInstance();
        if (v == btnDatePicker) {
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            txtDate.setText(year + "." + (monthOfYear+1) + "." + dayOfMonth);
                            checkInputsValid();
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            txtTime.setText(hourOfDay + "." + minute);
                            checkInputsValid();
                        }
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home : {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        Log.i("neighbour","AddMeetingActivity.init");

        color = DummyMeetingGenerator.generateColor(1);
        avatar.setColorFilter(color);

        roomInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                checkInputsValid();
            }
        });

    }

    @OnClick(R.id.create)
    void addMeeting() {
        mApiService.createMeeting(inputMeeting);
        finish();
    }

    /**
     * Used to navigate to this activity
     * @param activity
     */
    public static void navigate(FragmentActivity activity) {
        Log.i("neighbour","AddMeetingActivity.navigate");
        Intent intent = new Intent(activity, AddMeetingActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
    }
}
