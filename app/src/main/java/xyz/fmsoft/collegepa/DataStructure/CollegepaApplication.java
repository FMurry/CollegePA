package xyz.fmsoft.collegepa.DataStructure;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by fredericmurry on 1/3/16.
 */
public class CollegepaApplication extends Application {

    private static final String TAG = "xyz.fmsoft.collegepa";


    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);


    }
}