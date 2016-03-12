package cs407.cs407_homework2;

import android.content.Intent;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    /**
     * DatabaseHandler for all database queries
     */
    DatabaseHandler db;

    /**
     * Global fragment to assign into the FrameLayout fragment container.
     */
    private MonthView monthFragment;

    /**
     * Current month TextView
     */
    private TextView monthString;

    /**
     * Global fragment transaction manager used to manage fragment
     * visibility in the FrameLayout.
     */
    private FragmentTransaction transaction;

    /**
     * First day of the month
     */
    public int firstDayOfMonth;
    public int numDays;

    /**
     * Globals for accessing parts of the View calendar
     */
    public int currDay;
    public int currMonth;
    public int currYear;

    /**
     * List of days in the current month with Event info
     */
    public List<Integer> daysWithEvents;

    /**
     * Global fragments to assign into the FrameLayout fragment container.
     * They are global to avoid reinstantiating new Fragments each time
     * the user restarts the quiz
     */
    private MonthView textFragment;

    /**
     * CalendarView rows and cols for MonthView
     */
    private final int numRows = 7;
    private final int numCols = 7;

    /**
     * Global calendar for tracking date information
     */
    private final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        //*********************************************************************
        // START: Exercise database to check functionality.
        //*********************************************************************

        db = new DatabaseHandler(this);

        // If the database is empty, create a new one for the user
        if (db.getAllEvents().size() < 1) {
            Log.v("MainActivity", "$$$ CREATING DATABASE $$$");
            DatabaseTester dbTest = new DatabaseTester(this);
            dbTest.testDatabase();
        }

        //*********************************************************************
        // Once database has been created, continue...
        //*********************************************************************

        // Create new Fragment object to show the Calendar
        monthFragment = new MonthView();

        // Check if there is a saved instance to prevent overlapping
        // of new Activity fragments with old Activity fragments
        if (savedInstanceState == null) {

            // Add the 'fragment_container' to the StartActivity FrameLayout.
            // Fill this frame_container with the StartFragment for display.
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, monthFragment).commit();
        }

        Intent intent = getIntent();

        currMonth = intent.getIntExtra("CURR_MONTH",-1);
        currYear = intent.getIntExtra("CURR_YEAR",-1);

        if ( (currMonth > 0) && (currYear > 0) ) {
            calendar.set(Calendar.MONTH, currMonth);
            calendar.set(Calendar.YEAR, currYear);
        }

        initializeScreen();
    }

    /**
     * This method is responsible for setting up a majority of the View
     */
    private void initializeScreen() {

        // Display the current Calendar's month and year
        switch (calendar.get(Calendar.MONTH)) {
            case 0: numDays = 31; break;
            case 1: numDays = (calendar.get(Calendar.YEAR)%4 == 0) ? 29 : 28; break;
            case 2: numDays = 31; break;
            case 3: numDays = 30; break;
            case 4: numDays = 31; break;
            case 5: numDays = 30; break;
            case 6: numDays = 31; break;
            case 7: numDays = 31; break;
            case 8: numDays = 30; break;
            case 9: numDays = 31; break;
            case 10: numDays = 30; break;
            case 11: numDays = 31; break;
        }

        // Create a RelativeLayout screen for manipulating the ResultFragment View
        final RelativeLayout calendarView = (RelativeLayout) findViewById(R.id.calendar);
        calendarView.setBackgroundColor(Color.BLACK);

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));

        // Save of calendar variables
        currMonth = calendar.get(Calendar.MONTH);
        currYear = calendar.get(Calendar.YEAR);
        firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);

        // Set the days of the month with events
        daysWithEvents = getDaysWithEvents();

        // Current calendar month being displayed on the screen
        monthString = (TextView) calendarView.findViewById(R.id.dateToday);
        monthString.setPadding(20, 1, 20, 1);
        monthString.setBackgroundResource(R.drawable.standard_border);
        monthString.setText(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH) + " " + currYear);
        monthString.setPadding(200,1,200,1);
        monthString.setTextColor(Color.WHITE);

        // Set up StartFragment View's 'Start Quiz' button with an action listener
        ImageButton prevButton = (ImageButton) calendarView.findViewById(R.id.prevArrow);
        prevButton.setClickable(true);
        prevButton.setImageResource(R.mipmap.prev_arrow);
        prevButton.setBackgroundColor(Color.TRANSPARENT);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateView(calendar, -1);
            }
        });

        // Set up StartFragment View's 'Start Quiz' button with an action listener
        ImageButton nextButton = (ImageButton) calendarView.findViewById(R.id.nextArrow);
        nextButton.setClickable(true);
        nextButton.setImageResource(R.mipmap.next_arrow);
        nextButton.setBackgroundColor(Color.TRANSPARENT);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateView(calendar, 1);
            }
        });

    }

    /**
     * Updates the calendar month to show on the screen
     *
     * @param calendar
     * @param direction
     */
    public void updateView(Calendar calendar, int direction) {

        currMonth = (currMonth + direction)%12;
        if (currMonth == 0) {
            if (direction > 0) currYear++;
        } else if (currMonth < 0) {
            currMonth = Calendar.DECEMBER;
            currYear--;
        }

        // Update the current month to display
        calendar.set(Calendar.MONTH, currMonth);
        calendar.set(Calendar.YEAR, currYear);

        // Set the number of days in the month
        switch (calendar.get(Calendar.MONTH)) {
            case 0: numDays = 31; break;
            case 1: numDays = (calendar.get(Calendar.YEAR)%4 == 0) ? 29 : 28; break;
            case 2: numDays = 31; break;
            case 3: numDays = 30; break;
            case 4: numDays = 31; break;
            case 5: numDays = 30; break;
            case 6: numDays = 31; break;
            case 7: numDays = 31; break;
            case 8: numDays = 30; break;
            case 9: numDays = 31; break;
            case 10: numDays = 30; break;
            case 11: numDays = 31; break;
        }

        // Update the month to display
        displayMonth(calendar);

        // Set the days of the month with events
        daysWithEvents = getDaysWithEvents();

        firstDayOfMonth = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);

        monthFragment = new MonthView();

        // Switch the MonthView for display
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, monthFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    /**
     * This method starts the EventActivity
     *
     * @param month
     * @param day
     * @param year
     */
    public void displayDateEvents(int month, int day, int year) {

        // Set the Date to show Event data
        currMonth = month;
        currDay = day;
        currYear = year;

        Intent intent = new Intent(MainActivity.this, EventActivity.class);

        intent.putExtra("CURR_MONTH", currMonth);
        intent.putExtra("CURR_DAY", currDay);
        intent.putExtra("CURR_YEAR", currYear);

        startActivity(intent);
    }

    /**
     * Display the month and year of the currently displayed calendar
     */
    private void displayMonth(Calendar calendar) {
        monthString.setText(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH) + " " + currYear);
    }

    /**
     * This method gets all of the days with Event data in the current month.
     * This will be used to tell the user what days have event information.
     */
    private List<Integer> getDaysWithEvents() {

        List<Event> events = db.getAllEvents();
        List<Integer> eventDays = new ArrayList<Integer>();

        for (Event event : events) {
            String[] datePieces = event.getEventDate().split("/");

            if ( (Integer.parseInt(datePieces[0]) == (currMonth + 1)) && (Integer.parseInt(datePieces[2]) == currYear) ) {
                eventDays.add(Integer.parseInt(datePieces[1]));
            }
        }

        return eventDays;
    }

    /**
     * This method is used for debbuging purposes, specifically
     * allowing Fragments to display Toast messages.
     * @param message
     */
    public void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}
