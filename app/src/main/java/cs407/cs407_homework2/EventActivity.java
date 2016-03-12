package cs407.cs407_homework2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EventActivity extends AppCompatActivity {

    /**
     * DatabaseHandler instance for manipulating event data
     */
    private final DatabaseHandler db = new DatabaseHandler(this);

    /**
     * String to use for Event querying
     */
    private String queryDate = "";

    /**
     * Relative view of entire screen.
     */
    private RelativeLayout eventView;

    /**
     * Calendar for Date information displaying.
     */
    private Calendar calendar;

    /**
     * Adapter for displaying event buttons
     */
    private EventButtonAdapter adapter;

    private ListView dateContentEvents;

    private int currMonth;
    private int currDay;
    private int currYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        // Create a RelativeLayout screen for manipulating the ResultFragment View
        eventView = (RelativeLayout) findViewById(R.id.eventScreen);
        eventView.setBackgroundColor(Color.GRAY);

        Intent intent = getIntent();

        currMonth = intent.getIntExtra("CURR_MONTH", 0);
        currDay = intent.getIntExtra("CURR_DAY", 0);
        currYear = intent.getIntExtra("CURR_YEAR", 0);

        // Set the of the Event date to use for querying
        queryDate = (currMonth + 1) + "/" + currDay + "/" + currYear;

        calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, currMonth);
        calendar.set(Calendar.DAY_OF_MONTH, currDay);
        calendar.set(Calendar.YEAR, currYear);

        initializeScreen();
    }

    /**
     * This method is responsible for setting up a majority of the View
     */
    private void initializeScreen() {

        TextView dateToday = (TextView) eventView.findViewById(R.id.eventDate);
        dateToday.setPadding(20, 1, 20, 1);
        dateToday.setBackgroundResource(R.drawable.standard_border);
        dateToday.setText(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH) + " " + calendar.get(Calendar.DAY_OF_MONTH) + ", " + calendar.get(Calendar.YEAR));
        dateToday.setTextColor(Color.WHITE);

        ImageButton addButton = (ImageButton) eventView.findViewById(R.id.addEvent);
        addButton.setClickable(true);
        addButton.setImageResource(R.mipmap.add_button);
        addButton.setBackgroundColor(Color.TRANSPARENT);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvent();
            }
        });

        ImageButton deleteButton = (ImageButton) eventView.findViewById(R.id.backToCalendar);
        deleteButton.setClickable(true);
        deleteButton.setImageResource(R.mipmap.calendar_button);
        deleteButton.setBackgroundColor(Color.TRANSPARENT);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToCalendar();
            }
        });

        dateContentEvents = (ListView) findViewById(R.id.dateContent);
        dateContentEvents.setVerticalScrollBarEnabled(true);
        dateContentEvents.setClickable(true);
        dateContentEvents.setOnItemClickListener(onItemClicked);

        refreshScreen();
    }

    private void refreshScreen() {

        // Set button parameters
        ListView.LayoutParams linear = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.MATCH_PARENT);

        // Set up the adapter for adding the Events to the screen
        List<Event> events = new ArrayList<Event>();
        adapter = new EventButtonAdapter(this, events);
        dateContentEvents.setAdapter(adapter);

        for (final Event event : db.getAllDateEvents(queryDate)) {
            events.add(event);
        }

        adapter.notifyDataSetChanged();
    }

    private void addEvent() {

        Intent intent = new Intent(EventActivity.this, NewEventActivity.class);

        intent.putExtra("QUERY_DATE", queryDate);
        intent.putExtra("CURR_MONTH", currMonth);
        intent.putExtra("CURR_DAY", currDay);
        intent.putExtra("CURR_YEAR", currYear);

        startActivity(intent);

    }

    private void backToCalendar() {
        Intent intent = new Intent(EventActivity.this, MainActivity.class);

        intent.putExtra("CURR_MONTH", currMonth);
        intent.putExtra("CURR_YEAR", currYear);

        startActivity(intent);
    }

    /**
     * Verify if the user would like to delete the Event
     */
    private AdapterView.OnItemClickListener onItemClicked = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final Event event = (Event) adapter.getItem(position);
            AlertDialog.Builder dialog = new AlertDialog.Builder(EventActivity.this);
            dialog.setTitle("Delete Event");
            dialog.setMessage("Are you sure that you want to delete this event?");
            dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int index) {
                    Log.v("onItemClicked", "Deleting event");
                    db.deleteEvent(event.getEventID());
                    refreshScreen();

                }
            }).setNegativeButton("Cancel", null).show();
        }
    };
}
