package xyz.fmsoft.collegepa.utils;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

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
    @Bind(R.id.color_ok)TextView _submit;

    private boolean[] picked;
    ArrayList<FloatingActionButton> fabList;
    private OnCompleteListener mListener;

    private final String TAG = "ColorPickerDialog";
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
        picked = new boolean[14];
        fabList = new ArrayList<>();
        for(int i = 0;i<picked.length;i++){
            picked[i] = false;
        }
        _blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorChooser(v);
            }
        });
        _darkBlueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorChooser(v);
            }
        });
        _greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorChooser(v);
            }
        });
        _darkGreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorChooser(v);
            }
        });
        _orangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorChooser(v);
            }
        });
        _darkOrangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorChooser(v);
            }
        });
        _purpleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorChooser(v);
            }
        });
        _darkPurpleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorChooser(v);
            }
        });

        _redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorChooser(v);
            }
        });
        _darkRedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorChooser(v);
            }
        });
        _bluegrayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorChooser(v);
            }
        });
        _darkBluegrayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorChooser(v);
            }
        });
        _submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorChooser(v);
            }
        });
        fabList.add(_blueButton);
        fabList.add(_darkBlueButton);
        fabList.add(_greenButton);
        fabList.add(_darkGreenButton);
        fabList.add(_orangeButton);
        fabList.add(_darkOrangeButton);
        fabList.add(_purpleButton);
        fabList.add(_darkPurpleButton);
        fabList.add(_redButton);
        fabList.add(_darkRedButton);
        fabList.add(_bluegrayButton);
        fabList.add(_darkBluegrayButton);


        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnCompleteListener)activity;
        }
        catch (final ClassCastException e){
            throw new ClassCastException(activity.toString()+" must implement OnCompleteListener");
        }
    }



    public void colorChooser(View v) {
        switch (v.getId()){
            case R.id.blue_button:
                _blueButton.setImageResource(R.drawable.ic_check_white_48dp);
                picked[0] = true;
                setToggle(0);
                break;
            case R.id.darkblue_button:
                _darkBlueButton.setImageResource(R.drawable.ic_check_white_48dp);
                setToggle(1);
                break;
            case R.id.green_button:
                _greenButton.setImageResource(R.drawable.ic_check_white_48dp);
                setToggle(2);
                break;
            case R.id.darkgreen_button:
                _darkGreenButton.setImageResource(R.drawable.ic_check_white_48dp);
                setToggle(3);
                break;
            case R.id.orange_button:
                _orangeButton.setImageResource(R.drawable.ic_check_white_48dp);
                setToggle(4);
                break;
            case R.id.darkorange_button:
                _darkOrangeButton.setImageResource(R.drawable.ic_check_white_48dp);
                setToggle(5);
                break;
            case R.id.purple_button:
                _purpleButton.setImageResource(R.drawable.ic_check_white_48dp);
                setToggle(6);
                break;
            case R.id.darkpurple_button:
                _darkPurpleButton.setImageResource(R.drawable.ic_check_white_48dp);
                setToggle(7);
                break;
            case R.id.red_button:
                _redButton.setImageResource(R.drawable.ic_check_white_48dp);
                setToggle(8);
                break;
            case R.id.darkred_button:
                _darkRedButton.setImageResource(R.drawable.ic_check_white_48dp);
                setToggle(9);
                break;
            case R.id.bluegray_button:
                _bluegrayButton.setImageResource(R.drawable.ic_check_white_48dp);
                setToggle(10);
                break;
            case R.id.darkbluegray_button:
                _darkBluegrayButton.setImageResource(R.drawable.ic_check_white_48dp);
                setToggle(11);
                break;
            case R.id.color_ok:
                onFinish();
                break;

        }
    }

    public void setToggle(int index){
        picked[index] = true;
        fabList.get(index).setImageResource(R.drawable.ic_check_white_48dp);
        for(int i = 0;i<fabList.size();i++){
            if(i!=index){
                picked[i] = false;
                fabList.get(i).setImageResource(0);
            }
        }
    }

    public void onFinish(){
        int index = 0;
        for(int i = 0; i<picked.length; i++){
            if(picked[i]){
                index = i;
            }
        }
        String colorID = "";
        switch (index){
            case 0:
                colorID = getResources().getString(0+R.color.blue);
                break;
            case 1:
                colorID = getResources().getString(0+R.color.darkblue);
                break;
            case 2:
                colorID = getResources().getString(0+R.color.green);
                break;
            case 3:
                colorID = getResources().getString(0+R.color.darkgreen);
                break;
            case 4:
                colorID = getResources().getString(0+R.color.orange);
                break;
            case 5:
                colorID = getResources().getString(0+R.color.darkorange);
                break;
            case 6:
                colorID = getResources().getString(0+R.color.purple);
                break;
            case 7:
                colorID = getResources().getString(0+R.color.darkpurple);
                break;
            case 8:
                colorID = getResources().getString(0+R.color.red);
                break;
            case 9:
                colorID = getResources().getString(0+R.color.darkred);
                break;
            case 10:
                colorID = getResources().getString(0+R.color.bluegray);
                break;
            case 11:
                colorID = getResources().getString(0+R.color.darkbluegray);
                break;
            default:
                colorID = getResources().getString(0+R.color.colorPrimary);
        }
        Log.d(TAG,colorID);
        //TODO: Pass colorID back to Activity then dismiss fragment
        mListener.OnComplete(colorID);
        dismiss();
    }

    public interface OnCompleteListener {
         void OnComplete(String value);
    }
}
