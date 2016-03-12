package cs407.cs407_homework2;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Mike Feilbach on 4/3/2015. Has methods which exercise the
 * app's database with automated tests.
 */
public class DatabaseTester
{
    // For tagging messages in Log Cat.
    private static final String TAG = "MyActivity";

    // The app's context.
    private Context context;

    /**
     * Default constructor. Do not use, we must populate context when
     * instantiating this class.
     */
    public DatabaseTester()
    {
    }


    /**
     * Constructor that initializes the context field. The context must
     * be given in order to instantiate a DatabaseHandler, which is used
     * to interact with the app's database.
     *
     * @param context The app's context.
     */
    public DatabaseTester(Context context)
    {
        this.context = context;
    }


    /**
     * Used to test the database functionality. Will return early if one
     * of the tests does not pass. Tests are self-commenting via Log print
     * statements.
     */
    public void testDatabase()
    {
        // Create database handler. This is done whenever we want to interact
        // with the database. It gives access to the database functions in
        // the database API.
        DatabaseHandler db = new DatabaseHandler(this.context);

        // Delete & recreate the database.
        db.recreateDatabase(this.context);

        // Start test.
        Log.v(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
                + "@@@@@@@@@@");
        Log.v(TAG, "@@ START: Database Test.");
        Log.v(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
                + "@@@@@@@@@@");


        // Add sample Events.
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        Long time = date.getTime();

        String searchDate = sdf.format(date);

        db.addEvent(new Event("Pick up Jim from school", "Jim needs to be picked up from school at this time.", searchDate, time, time));
        db.addEvent(new Event("Exam", "CS407 exam @ ", searchDate, time, time));

        Log.v(TAG, "Checking there are two records in Events table.");
        Log.v(TAG, "" + db.getAllDateEvents(searchDate).size());
        if (db.getAllDateEvents(searchDate).size() == 4)
        {
            Log.v(TAG, "PASSED");
        }
        else
        {
            Log.v(TAG, "**************** FAILED.");
            return;
        }

        // Print out all Bubbles in Bubble table.
        List<Event> events = db.getAllDateEvents(searchDate);

        Log.v(TAG, "Events in table printed below.");
        for (Event event : events)
        {
            String log = "ID:\t" + event.getEventID()
                    + ", Name:\t" + event.getEventName()
                    + ", Content:\t" + event.getEventContent()
                    + ", Date:\t" + event.getEventDate()
                    + ", Start:\t" + event.getEventStartTime()
                    + ", End:\t" + event.getEventEndTime() + "\n";

            // Write this Bubble to the Log.
            Log.v(TAG, log);
        }

        Log.v(TAG, "Deleting: First Event from table.");

        int deleteEventId = events.get(1).getEventID();

        if (db.deleteEvent(deleteEventId))
        {
            Log.v(TAG, "PASSED");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        Log.v(TAG, "Trying to delete same Event again. Should not be able to " +
                "delete--this record no longer exists.");
        if (!db.deleteEvent(deleteEventId))
        {
            Log.v(TAG, "Passed");
        }
        else
        {
            Log.v(TAG, "**************** FAILED");
            return;
        }

        // Delete all of the events in the database, reverting the database to
        // a fresh state for adding new events
        db.deleteAllEvents();

        // End test! If we get here, all tests have passed. If any tests
        // fail, they return right away.
        Log.v(TAG, "********************************************************"
                + "**********");
        Log.v(TAG, "** END: Database Test. All tests passed!");
        Log.v(TAG, "********************************************************"
                + "**********");
    }
}

