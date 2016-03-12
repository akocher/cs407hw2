package cs407.cs407_homework2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;

public class DatabaseHandler extends SQLiteOpenHelper
{
    // For tagging messages in Log Cat.
    private static final String TAG = "MyActivity";

    //*************************************************************************
    // Database attributes.
    //*************************************************************************
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "eventDatabase";

    //*************************************************************************
    // Event table attributes.
    //*************************************************************************

    // Name of Event table within the database.
    private static final String TABLE_EVENTS = "events";

    // Columns in the Event table.
    public static final String COLUMN_EVENT_ID = "eventId";
    public static final String COLUMN_EVENT_NAME = "eventName";
    public static final String COLUMN_EVENT_CONTENT = "eventContent";
    public static final String COLUMN_EVENT_DATE = "eventDate";
    public static final String COLUMN_EVENT_START = "eventStart";
    public static final String COLUMN_EVENT_END = "eventEnd";


    /**
     * Default constructor.
     * @param context
     */
    public DatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //*************************************************************************
    // Creating tables in this app's database.
    //*************************************************************************
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.v(TAG, "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$"
                + "$$$$$$$$$$");
        Log.v(TAG, "$$ onCreate called: creating database.");
        Log.v(TAG, "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$"
                + "$$$$$$$$$$");

        // The Event table.
        String CREATE_EVENT_TABLE = "CREATE TABLE " +
                TABLE_EVENTS + "("
                + COLUMN_EVENT_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_EVENT_NAME + " TEXT,"
                + COLUMN_EVENT_CONTENT + " TEXT,"
                + COLUMN_EVENT_DATE + " TEXT,"
                + COLUMN_EVENT_START + " INTEGER,"
                + COLUMN_EVENT_END + " INTEGER)";

        // All tables are created when database is initialized.
        db.execSQL(CREATE_EVENT_TABLE);

        Log.v(TAG, "****** START: Creating sample Events.");

        // Add sample Events.
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        long time = date.getTime();

        addEvent(new Event("Babysitting", "Babysit little Charlie", sdf.format(date), time, time), db);
        addEvent(new Event("Study group", "Study for Cryptography", sdf.format(date), time, time), db);

        Log.v(TAG, "****** END: Creating sample Events.");
    }

    //*************************************************************************
    // The onUpgrade() method is called when the handler is invoked with a
    // greater database version number from the one previously used. We will
    // simply remove the old database and create a new one.
    //*************************************************************************
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Delete all tables.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);

        // Create fresh tables.
        onCreate(db);
    }


    /**
     * Delete the entire database (all tables) and recreates it.
     *
     * @param context the app's context. See the following:
     * http://stackoverflow.com/questions/7917947/get-context-in-android
     */
    public void recreateDatabase(Context context)
    {
        Log.v(TAG, "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
                + "!!!!!!!!!!");
        Log.v(TAG, "!! recreateDatabase called: deleting database.");
        Log.v(TAG, "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
                + "!!!!!!!!!!");

        // Close the SQLiteOpenHelper. This kills any open connections to the
        // database.
        this.close();

        // Delete this database.
        context.deleteDatabase(DATABASE_NAME);

        // Force the database to recreate.
        SQLiteDatabase dummy = this.getWritableDatabase();
    }


    //*************************************************************************
    // Event Table operations.
    //*************************************************************************

    /**
     * Adds a single Event to the Event table. There is no error checking
     * within this method, and it is meant to stock the Event table with
     * "sample Events" when the database is created.
     */
    private void addEvent(Event event, SQLiteDatabase db)
    {
        // Create key-value pairs for the columns in the Event table.
        ContentValues values = new ContentValues();

        // Note: do not use the Event's ID. The Event table will
        // automatically generate them, since it is the primary key.

        // Event name field.
        values.put(COLUMN_EVENT_NAME, event.getEventName());

        // Event content field.
        values.put(COLUMN_EVENT_CONTENT, event.getEventContent());

        // Event date field.
        values.put(COLUMN_EVENT_DATE, event.getEventDate());

        // Event time field.
        values.put(COLUMN_EVENT_START, event.getEventStartTime());

        // Event time field.
        values.put(COLUMN_EVENT_END, event.getEventEndTime());

        // Insert new row (Event) into the Event table. If it fails,
        // we will return false.
        if (db.insert(TABLE_EVENTS, null, values) == -1)
        {
            Log.v(TAG, "****** Insert of sample Event failed.");
        }
    }


    /**
     * Adds a single Event to the Event table. Adding a duplicate Event
     * is not allowed and will return false.
     *
     * Note that this will properly assign a unique ID for the Event. An
     * ID should not be assigned when a Event object is created, and is done
     * at the time the Event is added to the Event table.
     *
     * @param event The Event to add to the Event table.
     *
     * @return true iff the Event was added successfully to the
     *         Event table, else false.
     */
    public boolean addEvent(Event event)
    {
        // Assume Event insertion will be successful.
        boolean retVal = true;

        // Check if this Event is within the Event table already.
        // If so, return false, add nothing to the Event table.
        if (getEvent(event.getEventName(),event.getEventContent(),event.getEventDate()) != null) {
            return false;
        }

        // Get a reference to our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // Create key-value pairs for the columns in the Event table.
        ContentValues values = new ContentValues();

        // Note: do not use the Event's ID. The Event table will
        // automatically generate them, since it is the primary key.

        // Event name field.
        values.put(COLUMN_EVENT_NAME, event.getEventName());
        Log.v("addEvent", "Event Name: " + event.getEventName());

        // Event content field.
        values.put(COLUMN_EVENT_CONTENT, event.getEventContent());
        Log.v("addEvent", "Event Content: " + event.getEventContent());

        // Event date field.
        values.put(COLUMN_EVENT_DATE, event.getEventDate());
        Log.v("addEvent", "Event Date: " + event.getEventDate());

        // Event time field.
        values.put(COLUMN_EVENT_START, event.getEventStartTime());
        Log.v("addEvent", "Event Start: " + event.getEventStartTime());

        // Event time field.
        values.put(COLUMN_EVENT_END, event.getEventEndTime());
        Log.v("addEvent", "Event End: " + event.getEventEndTime());

        // Insert new row (Event) into the Event table. If it fails,
        // we will return false.
        if (db.insert(TABLE_EVENTS, null, values) == -1)
        {
            Log.v("addEvent", "Failed to add event\n");
            retVal = false;
        } else {
            Log.v("addEvent", "Successfully added event\n");
        }

        // Close out database.
        db.close();

        return retVal;
    }


    /**
     * Returns the Event whose content field is given. If no such Event
     * is found, null is returned.
     *
     * @param eventContent The content field of the Event being
     *                      searched for.
     *
     * @return null if Event was not found, else an Event object, filled
     *         with the Event's data, which is pulled from the database.
     */
    public Event getEvent(String eventName, String eventContent, String eventDate)
    {
        // Create an Event to put the matching Event's data in.
        // This will hold the return value.
        // Note: We will return the first matching Event we find,
        //       however, there should not be duplicates--this is
        //       an invariant.
        Event event = new Event();

        // Get a reference to our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // Query to find the Event with the given content.
        String query = "SELECT * FROM " + TABLE_EVENTS + " WHERE "
                + COLUMN_EVENT_NAME + " = \"" + eventName + "\" AND "
                + COLUMN_EVENT_CONTENT + " = \"" + eventContent + "\" AND "
                + COLUMN_EVENT_DATE + " = \"" + eventDate + "\"";

        Cursor cursor = db.rawQuery(query, null);

        // Find the first matching Event.
        if (cursor.moveToFirst())
        {
            // ID is in column 0.
            event.setEventID(Integer.parseInt(cursor.getString(0)));

            // Event content is in column 1.
            event.setEventContent(cursor.getString(1));

            // Event Date is in column 2.
            event.setEventDate(cursor.getString(2));
        }
        else
        {
            // No Event found. Return null.
            event = null;
        }

        // Close out the database and cursor.
        db.close();
        cursor.close();

        return event;
    }

    /**
     * Returns a List<Event> with all of the Events in the Event table.
     *
     * @return a List<Events> with all of the Events in the Event table.
     */
    public List<Event> getAllEvents()
    {
        // Return value.
        List<Event> events = new ArrayList<Event>();

        // Get a reference to our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // Query to select all rows of Events table.
        String query = "SELECT * FROM " + TABLE_EVENTS;

        Log.v("getAllEvents", query);

        Cursor cursor = db.rawQuery(query, null);

        // Loop through all rows, adding Events to our list as we go.
        if (cursor.moveToFirst())
        {
            do
            {
                Event event = new Event();

                // ID is in column 1
                event.setEventID(Integer.parseInt(cursor.getString(0)));

                // Event content is in column 2.
                event.setEventName(cursor.getString(1));

                // Event content is in column 3.
                event.setEventContent(cursor.getString(2));

                // Event Date is in column 4.
                event.setEventDate(cursor.getString(3));

                // Event Time is in column 5/6.
                event.setEventStartTime(cursor.getLong(4));
                event.setEventEndTime(cursor.getLong(5));

                // Add this Event to the list.
                events.add(event);
            }
            while (cursor.moveToNext());
        }

        // Close out database and cursor.
        db.close();
        cursor.close();

        return events;
    }

    /**
     * Returns a List<Event> with all of the Events in the Event table.
     *
     * @return a List<Events> with all of the Events in the Event table.
     */
    public List<Event> getAllDateEvents(String eventDate)
    {
        // Return value.
        List<Event> events = new ArrayList<Event>();

        // Get a reference to our database.
        SQLiteDatabase db = this.getWritableDatabase();

         // Query to select all rows of Events table.
        String query = "SELECT * FROM " + TABLE_EVENTS + " WHERE "
                + COLUMN_EVENT_DATE + " = \"" + eventDate + "\""
                + " ORDER BY " + COLUMN_EVENT_START;

        Log.v("getAllEvents", query);

        Cursor cursor = db.rawQuery(query, null);

        // Loop through all rows, adding Events to our list as we go.
        if (cursor.moveToFirst())
        {
            do
            {
                Event event = new Event();

                // ID is in column 1
                event.setEventID(Integer.parseInt(cursor.getString(0)));

                // Event content is in column 2.
                event.setEventName(cursor.getString(1));

                // Event content is in column 3.
                event.setEventContent(cursor.getString(2));

                // Event Date is in column 4.
                event.setEventDate(cursor.getString(3));

                // Event Time is in column 5/6.
                event.setEventStartTime(cursor.getLong(4));
                event.setEventEndTime(cursor.getLong(5));

                // Add this Event to the list.
                events.add(event);
            }
            while (cursor.moveToNext());
        }

        // Close out database and cursor.
        db.close();
        cursor.close();

        return events;
    }


    /**
     * Deletes the Event, specified by content, within the Event table.
     *
     * @param eventId the Event to be deleted from the
     *                      Event table.
     *
     * @return true iff this Event was deleted, else false.
     */
    public boolean deleteEvent(int eventId)
    {
        // Assume we won't find this Event.
        boolean result = false;

        // The query within the Event Table.
        String query = "Select * FROM " + TABLE_EVENTS + " WHERE "
                + COLUMN_EVENT_ID + " = \"" + eventId + "\"";

        // Get a reference of our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // For scanning the Event table.
        Cursor cursor = db.rawQuery(query, null);

        // Find the first instance of this Event.
        // Note: duplicates Events are now allowed, so there should only
        //       ever be one of each Event in the Event table.
        if (cursor.moveToFirst())
        {
            // Delete the Event whose ID matche the first found Event.
            int numDeleted = db.delete(TABLE_EVENTS, COLUMN_EVENT_ID + " = ?",
                    new String[] { String.valueOf(Integer.parseInt(cursor.getString(0))) });

            // We found an Event to delete, and it was deleted.
            if (numDeleted > 0)
            {
                result = true;
            }
        }

        // Close database and cursor.
        db.close();
        cursor.close();

        return result;
    }

    /**
     * Deletes all Events in the Event Table.
     *
     * @return true iff all Events are deleted from the Event table.
     */
    public void deleteAllEvents()
    {
        // Get a reference of our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete all records from WorkoutLog table.
        db.execSQL("DELETE FROM " + TABLE_EVENTS);
    }

    /**
     * Updates the Event (with the given ID) with the type and content
     * of the given Event.
     *
     * @param ID The ID of the Event to update.
     *
     * @param event The Event containing the information to update with.
     *
     * @return true iff the Event specified by the given ID is updated
     *         with the title and body of the given Event, else false.
     */
    public boolean updateEvent(int ID, Event event)
    {
        // Return value.
        boolean retVal = false;

        // If this Event isn't already in the database.
        if (getEvent(event.getEventName(),event.getEventContent(),event.getEventDate()) == null)
        {
            // Get a reference of our database.
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();

            // Event type and content.
            values.put(COLUMN_EVENT_DATE, event.getEventDate());
            values.put(COLUMN_EVENT_CONTENT, event.getEventContent());

            // Update the Event's row in the Event table. If update fails, return false.
            if (db.update(TABLE_EVENTS, values, COLUMN_EVENT_ID + " = " + String.valueOf(ID), null) == -1) {
                retVal = false;
            }
            else
            {
                // Update was successful.
                retVal = true;
            }

            // Close out database.
            db.close();
        }

        return retVal;
    }
}