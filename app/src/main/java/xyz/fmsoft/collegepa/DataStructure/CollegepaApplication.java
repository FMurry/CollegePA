package xyz.fmsoft.collegepa.DataStructure;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by fredericmurry on 1/3/16.
 */
public class CollegepaApplication extends Application {

    private static final String TAG = "xyz.fmsoft.collegepa";


    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


    }
}