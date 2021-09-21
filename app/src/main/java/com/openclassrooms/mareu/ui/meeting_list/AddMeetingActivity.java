package com.openclassrooms.mareu.ui.meeting_list;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.di.DI;
import com.openclassrooms.mareu.model.Meeting;
import com.openclassrooms.mareu.service.DummyMeetingApiService;
import com.openclassrooms.mareu.service.DummyMeetingGenerator;
import com.openclassrooms.mareu.service.MeetingApiService;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddMeetingActivity extends AppCompatActivity {

    private MeetingApiService mApiService;

    private int colorInput = Color.WHITE;
    private String roomInput;
    private TextView txtDateInput;
    private TextView txtTimeInput;
    private Date dateInput;

    private Button btnCreateMeeting;
    private Button btnTimePicker;
    private Button btnDatePicker;
    private Spinner spinnerRoomInput;

    /**
     * Used to navigate to this activity
     * @param activity
     */
    public static void navigate(Activity activity) {
        Log.i("neighbour","AddMeetingActivity.navigate");
        Intent intent = new Intent(activity, AddMeetingActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);
        // authorizes the return arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // takes into account the initial rotation made in main activity
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        Log.i("neighbour","AddMeetingActivity.onCreate");
        mApiService = DI.getMeetingApiService();
        initMeetingInput();
        createRoomInput();
        createDateInput();
        createTimeInput();
        createBtnCreateMeeting();
    }

    private void initMeetingInput() {
        colorInput = DummyMeetingGenerator.generateColor();
        ImageView avatar = findViewById(R.id.avatar);
        avatar.setColorFilter(colorInput);

        btnCreateMeeting = findViewById(R.id.create);
        btnTimePicker = findViewById(R.id.btn_time);
        btnDatePicker = findViewById(R.id.btn_date);
        spinnerRoomInput = findViewById(R.id.spinnerRoomInput);

        btnCreateMeeting.setEnabled(false);
        btnTimePicker.setEnabled(false);
        btnDatePicker.setEnabled(false);
        spinnerRoomInput.setEnabled(false);
    }

    private void createBtnCreateMeeting() {
        btnCreateMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("neighbour","AddMeetingActivity.onClick btnCreateMeeting");
                // inputMeeting is recomputed to take into account the latest subject and emails input
                // (which were out of the scope of checkInputsValid)
                mApiService.createMeeting(createMeetingInput());
                Log.i("neighbour","AddMeetingActivity.onClick btnCreateMeeting finish");
                finish();
            }
        });
        btnTimePicker.setEnabled(true);
        btnDatePicker.setEnabled(true);
        spinnerRoomInput.setEnabled(true);
    }

    private void checkInputsValid() {
        Log.i("neighbour","AddMeetingActivity.checkInputsValid");
        boolean dateValid = !txtDateInput.getText().toString().isEmpty();
        boolean timeValid = !txtTimeInput.getText().toString().isEmpty();
        Log.i("neighbour","AddMeetingActivity.checkInputsValid " + dateValid + " " + timeValid);
        if ((!dateValid) || (!timeValid)) {
            btnCreateMeeting.setEnabled(false);
            return;
        }
        try {
            String stringDate = DummyMeetingApiService.convertDateAndTimeToString(txtDateInput.getText().toString(), txtTimeInput.getText().toString());
            dateInput = DummyMeetingApiService.simpleDateFormat.parse(stringDate);
            btnCreateMeeting.setEnabled(!mApiService.isMeetingAlreadyExists(createMeetingInput()));
        } catch (Exception e) {
            btnCreateMeeting.setEnabled(false);
            Log.i("neighbour","AddMeetingActivity.checkInputsValid exception " + e);
        }
    }

    private Meeting createMeetingInput() {
        TextInputLayout subjectInput = findViewById(R.id.subjectLyt);
        TextInputLayout emailsInput = findViewById(R.id.emailsLyt);
        Log.i("neighbour","subject = " + subjectInput.getEditText().getText().toString() + " emails = " + emailsInput.getEditText().getText().toString());
        return new Meeting(
            colorInput,
            roomInput,
            subjectInput.getEditText().getText().toString(),
            dateInput,
            Arrays.asList(emailsInput.getEditText().getText().toString())
        );
    }
    private void createDateInput() {
        txtDateInput = findViewById(R.id.in_date);
        txtDateInput.setFocusable(false);
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                    v.getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            txtDateInput.setText(DummyMeetingApiService.convertyyyyMMddToString(year, monthOfYear + 1, dayOfMonth));
                            checkInputsValid();
                        }
                    },
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.show();
            }
        });
    }

    private void createTimeInput() {
        txtTimeInput = findViewById(R.id.in_time);
        txtTimeInput.setFocusable(false);
        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                    v.getContext(),
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            txtTimeInput.setText(DummyMeetingApiService.convertHHmmToString(hourOfDay, minute));
                            checkInputsValid();
                        }
                    },
                    c.get(Calendar.HOUR_OF_DAY),
                    c.get(Calendar.MINUTE),
                    true
                );
                timePickerDialog.show();
            }
        });
    }

    private void createRoomInput() {
        final List<String> arraySpinnerRoomInput = DummyMeetingGenerator.generateRoomNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.input_spinner_item, arraySpinnerRoomInput);
        spinnerRoomInput.setAdapter(adapter);
        spinnerRoomInput.setSelection(0); // Default selection
        roomInput = arraySpinnerRoomInput.get(0);
        spinnerRoomInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("neighbour","AddMeetingActivity.onItemSelected");
                // To set the selected text in black
                TextView selectedText = (TextView)parent.getChildAt(0);
                if (selectedText != null) selectedText.setTextColor(Color.BLACK);
                roomInput = arraySpinnerRoomInput.get(position);
                Log.i("neighbour","AddMeetingActivity.onItemSelected stringRoom " + roomInput);
                checkInputsValid();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                TextView selectedText = (TextView)parent.getChildAt(0);
                if (selectedText != null) selectedText.setTextColor(Color.BLACK);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Here can be introduce the treatment of the screen rotation
    }

    /*
    Treat the return button
     */
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
}
