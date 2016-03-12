package cs407.cs407_homework2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;


public class EventButtonAdapter extends BaseAdapter {

    /**
     * Context for the Adapter
     */
    private Context context;

    /**
     * List of Event objects for
     */
    private List<Event> events;

    public EventButtonAdapter(Context context, List<Event> eventList) {
        this.context = context;
        events = eventList;
    }

    @Override
    public View getView(int listPosition, View child, ViewGroup parent) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        // Child View that will be added to the parent
        child = inflater.inflate(R.layout.event_button_layout, parent, false);

        TextView eventName = (TextView) child.findViewById(R.id.eventNameButton);
        eventName.setMaxWidth(900);
        //eventName.setTextColor(Color.WHITE);

        TextView eventContent = (TextView) child.findViewById(R.id.eventContentButton);
        eventContent.setMaxWidth(900);
        eventContent.setTextColor(Color.WHITE);
        TextView eventTime = (TextView) child.findViewById(R.id.eventTimeButton);

        // Get the event to add to the list
        final Event event = events.get(listPosition);
        eventName.setText(event.getEventName());
        eventContent.setText(event.getEventContent());

        // Formates the start and end dates of the current Event
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        String startTime = sdf.format(event.getEventStartTime());
        String endTime = sdf.format(event.getEventEndTime());
        eventTime.setText("Starts: " + startTime + " " + "Ends: " + endTime);
        eventTime.setTextColor(Color.WHITE);

        // Set up a delete icon for the user to touch if they want to delete the Event
        ImageView deleteEvent = (ImageView) child.findViewById(R.id.deleteEvent);
        deleteEvent.setImageResource(R.mipmap.delete_button);
        deleteEvent.setBackgroundColor(Color.TRANSPARENT);

        return child;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return events.get(position).getEventID();
    }

}
