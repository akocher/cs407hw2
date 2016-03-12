package cs407.cs407_homework2;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;


public class MonthView extends Fragment{

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Create a RelativeLayout screen for manipulating the ResultFragment View
        GridLayout calendarGrid;

        calendarGrid = (GridLayout) inflater.inflate(R.layout.calendar_month, container, false);

        return populateCalendarView(calendarGrid);
    }

    /**
     * This method is responsible for populating the Calendar view on the screen
     *
     * @param calendarGrid View of the unpopulate grid
     *
     * @return calendarGrid View of the populated grid
     */
    private View populateCalendarView (GridLayout calendarGrid) {

        calendarGrid.setColumnCount(numCols);
        calendarGrid.setRowCount(numRows);
        calendarGrid.setOrientation(GridLayout.VERTICAL);
        calendarGrid.setBackgroundColor(Color.LTGRAY);

        // Math for figuring out how to display the current month's days
        WindowManager wm = (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;

        // These integers will be used for formatting each column of the calendar grid
        final int currDay;
        final int currMonth = ((MainActivity) getActivity()).currMonth;
        final int currYear = ((MainActivity) getActivity()).currYear;

        Calendar calendar = Calendar.getInstance();

        if (currMonth == calendar.get(Calendar.MONTH) && currYear == calendar.get(Calendar.YEAR)) {
            currDay = calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            currDay = -1;
        }

        // Set the calendar accordingly
        calendar.set(Calendar.MONTH, currMonth);
        calendar.set(Calendar.YEAR, currYear);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));

        // This offset will help figure out which grid cells should be shaded out
        // or shown in the current month
        int dayOffset = (calendar.get(Calendar.DAY_OF_WEEK) - 1); //calendar.getTime().getDay();
        int[] colDays = new int[numRows - 1];

        // List of event dates for altering the date color. To help the user find events
        List<Integer> daysWithEvents = ((MainActivity) getActivity()).daysWithEvents;

        /**
         * This loop populates the calendar grid with the proper day to
         * week day alignment given a particular month and year.
         */
        for (int i = 0 ; i < numCols ; i++) {

            if (i < dayOffset) {
                colDays[0] = 0;
                colDays[1] = (i - dayOffset) + 8;
            } else {
                colDays[0] = (i - dayOffset) + 1;
                colDays[1] = colDays[0] + 7;
            }

            for (int k = 2 ; k < numRows - 1; k++) {
                colDays[k] = colDays[k-1] + 7;
            }

            TextView colDay = new TextView(this.getContext());
            colDay.setBackgroundColor(Color.BLACK);
            colDay.setPadding(1, 15, 1, 1);

            switch (i) {
                case 0: colDay.setText("Sun"); break;
                case 1: colDay.setText("Mon"); break;
                case 2: colDay.setText("Tue"); break;
                case 3: colDay.setText("Wed"); break;
                case 4: colDay.setText("Thu"); break;
                case 5: colDay.setText("Fri"); break;
                case 6: colDay.setText("Sat"); break;
            }

            colDay.setTextColor(Color.WHITE);
            colDay.setGravity(Gravity.CENTER);
            colDay.setPadding(1, 1, 1, 1);

            // Add the column day header
            calendarGrid.addView(colDay, screenWidth / 7, 75);

            // This integer tells the lower logic to stop filling in
            // grid cells when the number of days in the current month
            // have all been accounted for.
            int numDays = ((MainActivity) getActivity()).numDays;

            for (int j = 0 ; j < numRows - 1 ; j++) {

                final int dayValue = colDays[j];

                Button currDateButton = new Button(this.getContext());
                currDateButton.setMaxWidth(screenWidth / 7);
                currDateButton.setMaxHeight(screenHeight / 7);

                // Only set clickable day buttons for actual days within
                // a given month, otherwise shade them out
                if ( (dayValue > 0) && (dayValue <= numDays) ) {

                    // Special days are lighter in color, so set the text black for contrast
                    currDateButton.setTextColor(Color.BLACK);

                    if (dayValue == currDay) {
                        currDateButton.setBackgroundResource(R.drawable.current_boarder);
                    }
                    else if (daysWithEvents.contains(dayValue)) {
                        currDateButton.setBackgroundResource(R.drawable.special_boarder);
                    }
                    else {
                        currDateButton.setBackgroundResource(R.drawable.standard_border);
                        currDateButton.setTextColor(Color.WHITE);
                    }
                    currDateButton.setText("" + dayValue);
                    currDateButton.setGravity(Gravity.CENTER);

                    currDateButton.setClickable(true);
                    currDateButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            ((MainActivity) getActivity()).displayDateEvents(currMonth, dayValue, currYear);
                        }
                    });
                } else {
                    currDateButton.setBackgroundColor(Color.GRAY);
                }

                // Add the button to the grid, which is added in column major order
                calendarGrid.addView(currDateButton, screenWidth / 7, screenHeight / 8);
            }
        }
        return calendarGrid;
    }
}
