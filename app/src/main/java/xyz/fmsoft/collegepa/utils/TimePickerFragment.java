package xyz.fmsoft.collegepa.utils;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by fredericmurry on 12/8/16.
 */

public class TimePickerFragment extends AppCompatDialogFragment implements TimePickerDialog.OnTimeSetListener {


    private static final String TAG = "TimePickerFragment";
    private String tag;

    public interface TimeListener{
        void returnFormattedTime(String time, String tag);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        listener = (TimeListener)getActivity();
        Bundle bundle = getArguments();
        tag = bundle.getString("from");
        return  new TimePickerDialog(getActivity(),this,hour,minute,false);
    }



    private TimeListener listener;
    /**
     * Called when the user is done setting a new time and the dialog has
     * closed.
     *
     * @param view      the view associated with this listener
     * @param hourOfDay the hour that was set
     * @param minute    the minute that was set
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String time;
        if(minute < 10){
            time = hourOfDay+":0"+minute;
        }
        else {
            time = hourOfDay + ":" + minute;
        }
        Log.d(TAG, tag);
        if (listener != null){
            listener.returnFormattedTime(time, tag);
        }

    }
}
