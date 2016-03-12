package cs407.cs407_homework2;

import java.util.Date;


public class Event {

    // Event ID: Primary key for the Event objects in the database
    private int _eventID;

    // Event Name: Event Name information
    private String _eventName;

    // Event Content: Event description information
    private String _eventContent;

    // Event Date: Date that an event occurs on
    private String _eventDate;

    // Event Time: Time that an event occurs
    private long _eventStartTime;
    private long _eventEndTime;

    /**
     * Default Event constructor. Note that this constructor will
     * assign default (invalid) values to all fields.
     */
    public Event()
    {
    }

    /**
     * Event constructor.
     *
     * @param eventContent  The Event's content.
     * @param eventDate     The Event's occurance date.
     */
    public Event(String eventName, String eventContent, String eventDate, long eventStartTime, long eventEndTime)
    {
        this._eventName = eventName;
        this._eventContent = eventContent;
        this._eventDate = eventDate;
        this._eventStartTime = eventStartTime;
        this._eventEndTime = eventEndTime;
    }

    //*************************************************************************
    // Modify for access Event content.
    //*************************************************************************
    public String getEventContent()
    {
        return this._eventContent;
    }

    public void setEventContent(String eventContent)
    {
        this._eventContent = eventContent;
    }

    //*************************************************************************
    // Modify for access Event name.
    //*************************************************************************
    public String getEventName()
    {
        return this._eventName;
    }

    public void setEventName(String eventName)
    {
        this._eventName = eventName;
    }

    //*************************************************************************
    // Modify or access Event ID.
    //*************************************************************************
    public int getEventID()
    {
        return this._eventID;
    }

    public void setEventID(int eventID)
    {
        this._eventID = eventID;
    }

    //*************************************************************************
    // Modify or access Event date.
    //*************************************************************************
    public String getEventDate()
    {
        return this._eventDate;
    }

    public void setEventDate(String eventDate)
    {
        this._eventDate = eventDate;
    }

    //*************************************************************************
    // Modify or access Event Start Time.
    //*************************************************************************
    public long getEventStartTime()
    {
        return this._eventStartTime;
    }

    public void setEventStartTime(long eventStartTime)
    {
        this._eventStartTime = eventStartTime;
    }

    //*************************************************************************
    // Modify or access Event End Time.
    //*************************************************************************
    public long getEventEndTime()
    {
        return this._eventEndTime;
    }

    public void setEventEndTime(long eventEndTime)
    {
        this._eventEndTime = eventEndTime;
    }
}
