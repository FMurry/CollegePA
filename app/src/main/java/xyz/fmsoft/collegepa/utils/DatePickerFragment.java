package xyz.fmsoft.collegepa.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by fredericmurry on 12/8/16.
 */

public class DatePickerFragment extends AppCompatDialogFragment implements DatePickerDialog.OnDateSetListener {


    public interface DateListener{
        void returnFormattedDate(String date);
    }

    DateListener listener;
    private static final String TAG = "DatePickerFragment";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        listener = (DateListener) getActivity();

        return new DatePickerDialog(getActivity(),this,year,month,day);
    }

    /**
     * @param view        The view associated with this listener.
     * @param year        The year that was set.
     * @param monthOfYear The month that was set (0-11) for compatibility
     *                    with {@link Calendar}.
     * @param dayOfMonth  The day of the month that was set.
     */
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year,monthOfYear,dayOfMonth);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(c.getTime());
        Log.d(TAG, date);
        if (listener != null){
            listener.returnFormattedDate(date);
        }
    }
}
