package cs407.cs407_homework2;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

public class NewEventActivity extends AppCompatActivity {

    /**
     * DatabaseHandler instance for adding Event data
     */
    private final DatabaseHandler db = new DatabaseHandler(this);

    /**
     *  String for selecting a query date
     */
    private String queryDate = "";

    private RelativeLayout eventView;

    private TextView eventStart;
    private TextView eventEnd;

    private long eventStartTime;
    private long eventEndTime;

    private int currMonth;
    private int currDay;
    private int currYear;

    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        Intent intent = getIntent();
        currMonth = intent.getIntExtra("CURR_MONTH",0);
        currDay = intent.getIntExtra("CURR_DAY",0);
        currYear = intent.getIntExtra("CURR_YEAR",0);
        queryDate = intent.getCharSequenceExtra("QUERY_DATE").toString();

        Log.v("NewEventActivity", "queryDate received: " + queryDate);

        // Add the 'fragment_container' to the StartActivity FrameLayout.
        // Fill this frame_container with the StartFragment for display.
        //getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, startFragment).commit();

        // Create a RelativeLayout screen for manipulating the ResultFragment View
        eventView = (RelativeLayout) findViewById(R.id.newEventScreen);
        eventView.setBackgroundColor(Color.GRAY);

        calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, currMonth);
        calendar.set(Calendar.DAY_OF_MONTH, currDay);
        calendar.set(Calendar.YEAR, currYear);

        initialScreen();
    }

    /**
     * This method is responsible for setting up a majority of the View
     */
    private void initialScreen() {

        // Current date
        TextView eventDate = (TextView) eventView.findViewById(R.id.currDate);
        eventDate.setText(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH) + " " + calendar.get(Calendar.DAY_OF_MONTH) + ", " + calendar.get(Calendar.YEAR));
        eventDate.setBackgroundResource(R.drawable.standard_border);
        eventDate.setTextColor(Color.WHITE);

        // Event Name input
        final EditText eventName = (EditText) eventView.findViewById(R.id.eventName);
        eventName.setPadding(20, 5, 20, 5);
        eventName.setText("");
        eventName.setHint("  Input event name...");
        eventName.setBackgroundResource(R.drawable.input_fields);
        eventName.setHintTextColor(Color.WHITE);
        eventName.setTextColor(Color.WHITE);

        // Time selectors
        LinearLayout timeSelectors = (LinearLayout) eventView.findViewById(R.id.timeSelectors);
        timeSelectors.setBackgroundResource(R.drawable.input_fields);

        eventStart = (TextView) eventView.findViewById(R.id.startTime);
        eventStart.setTextColor(Color.WHITE);
        eventStart.setOnClickListener(startTimeClickListener);

        eventEnd = (TextView) eventView.findViewById(R.id.endTime);
        eventEnd.setTextColor(Color.WHITE);
        eventEnd.setOnClickListener(endTimeClickListener);

        // Event Content input
        final EditText eventContent = (EditText) eventView.findViewById(R.id.eventContent);
        eventContent.setPadding(1, 1, 1, 1);
        eventContent.setText("");
        eventContent.setHint("  Input event details...");
        eventContent.setBackgroundResource(R.drawable.input_fields);
        eventContent.setHintTextColor(Color.WHITE);
        eventContent.setTextColor(Color.WHITE);

        // Save button
        ImageButton saveButton = (ImageButton) eventView.findViewById(R.id.saveEvent);
        saveButton.setClickable(true);
        saveButton.setImageResource(R.mipmap.save_button);
        saveButton.setBackgroundColor(Color.TRANSPARENT);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvent(eventName.getText().toString(), eventContent.getText().toString());
            }
        });
    }

    private void saveEvent(String eventName, String eventContent) {

        Event newEvent = new Event(eventName, eventContent, queryDate, eventStartTime, eventEndTime);

        if (db.addEvent(newEvent)) {
            Toast.makeText(NewEventActivity.this, "Yeah, Event Saved!", Toast.LENGTH_SHORT).show();
        }

        // Back to EventActivity
        Intent intent = new Intent(NewEventActivity.this, MainActivity.class);

        intent.putExtra("CURR_MONTH", currMonth);
        intent.putExtra("CURR_YEAR", currYear);

        startActivity(intent);
    }

    private View.OnClickListener startTimeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            TimePickerFragment time_fragment = new TimePickerFragment();
            time_fragment.setListener(startTimeListener);
            time_fragment.show(transaction, "TimePicker");
        }
    };

    private View.OnClickListener endTimeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            TimePickerFragment time_fragment = new TimePickerFragment();
            time_fragment.setListener(endTimeListener);
            time_fragment.show(transaction, "TimePicker");
        }
    };

    private TimePickerDialog.OnTimeSetListener startTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            eventStart.setText("Start: " + formatTime(hour, minute));

            calendar.set(currYear, currMonth, currDay, hour, minute);

            eventStartTime = calendar.getTime().getTime();
            Log.v("NewEventActivity", "startTime: " + eventStartTime);

        }
    };

    private TimePickerDialog.OnTimeSetListener endTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            eventEnd.setText("End: " + formatTime(hour, minute));

            calendar.set(currYear, currMonth, currDay, hour, minute);

            eventEndTime = calendar.getTime().getTime();
            Log.v("NewEventActivity", "startTime: " + eventEndTime);
        }
    };

    private String formatTime(int hour, int minute) {

        String timeOfDay = "";
        String minuteDisplay = "";

        // Correct the minute and hour displays
        minuteDisplay = (minute < 10) ? "0" + minute : "" + minute;
        timeOfDay = (hour >= 12) ? "PM" : "AM";

        // Correct 12AM time
        if (hour == 0) hour += 12;
        hour = (hour > 12) ? hour%12 : hour;

        return (hour + ":" + minuteDisplay + " " + timeOfDay);
    }

}
