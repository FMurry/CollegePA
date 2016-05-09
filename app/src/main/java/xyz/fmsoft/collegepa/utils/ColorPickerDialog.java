package xyz.fmsoft.collegepa.utils;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.fmsoft.collegepa.R;

/**
 * Created by fredericmurry on 5/9/16.
 */
public class ColorPickerDialog extends AppCompatDialogFragment{

    @Bind(R.id.blue_button)FloatingActionButton _blueButton;
    @Bind(R.id.darkblue_button)FloatingActionButton _darkBlueButton;
    @Bind(R.id.green_button)FloatingActionButton _greenButton;
    @Bind(R.id.darkgreen_button)FloatingActionButton _darkGreenButton;
    @Bind(R.id.orange_button)FloatingActionButton _orangeButton;
    @Bind(R.id.darkorange_button)FloatingActionButton _darkOrangeButton;
    @Bind(R.id.purple_button)FloatingActionButton _purpleButton;
    @Bind(R.id.darkpurple_button)FloatingActionButton _darkPurpleButton;
    @Bind(R.id.red_button)FloatingActionButton _redButton;
    @Bind(R.id.darkred_button)FloatingActionButton _darkRedButton;
    @Bind(R.id.bluegray_button)FloatingActionButton _bluegrayButton;
    @Bind(R.id.darkbluegray_button)FloatingActionButton _darkBluegrayButton;
    public ColorPickerDialog(){
        //Empty Constructor required
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p/>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_colorpickerdialog, container);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void colorChoose(View v){
        switch (v.getId()){
            case R.id.blue_button:
                _blueButton.setImageResource(R.drawable.ic_check_white_48dp);
                break;
            case R.id.darkblue_button:
                _darkBlueButton.setImageResource(R.drawable.ic_check_white_48dp);
                break;
            case R.id.green_button:
                _greenButton.setImageResource(R.drawable.ic_check_white_48dp);
                break;
            case R.id.darkgreen_button:
                _darkGreenButton.setImageResource(R.drawable.ic_check_white_48dp);
                break;
            case R.id.orange_button:
                _orangeButton.setImageResource(R.drawable.ic_check_white_48dp);
                break;
            case R.id.darkorange_button:
                _darkOrangeButton.setImageResource(R.drawable.ic_check_white_48dp);
                break;
            case R.id.purple_button:
                _purpleButton.setImageResource(R.drawable.ic_check_white_48dp);
                break;
            case R.id.darkpurple_button:
                _darkPurpleButton.setImageResource(R.drawable.ic_check_white_48dp);
                break;
            case R.id.red_button:
                _redButton.setImageResource(R.drawable.ic_check_white_48dp);
                break;
            case R.id.darkred_button:
                _darkRedButton.setImageResource(R.drawable.ic_check_white_48dp);
                break;
            case R.id.bluegray_button:
                _bluegrayButton.setImageResource(R.drawable.ic_check_white_48dp);
                break;
            case R.id.darkbluegray_button:
                _darkBluegrayButton.setImageResource(R.drawable.ic_check_white_48dp);
                break;

        }
    }
}
