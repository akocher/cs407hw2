package cs407.cs407_homework2;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;

import java.util.Calendar;

/**
 * Created by Alex Kocher
 */
public class TimePickerFragment extends DialogFragment {

    /**
     * This listener will activate the TimePickerFragment when invoked
     */
    private OnTimeSetListener timeSetListener;

    public TimePickerFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Calendar calendar = Calendar.getInstance();

        TimePickerDialog time = new TimePickerDialog(getActivity(), timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        return time;
    }

    public void setListener(OnTimeSetListener listener) {
        timeSetListener = listener;
    }
}