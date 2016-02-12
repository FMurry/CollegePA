package simplify.fwm.collegepa.DataStructure;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import com.firebase.client.Firebase;

/**
 * Created by fredericmurry on 1/3/16.
 */
public class CollegepaApplication extends Application {

    private static final String TAG = "simplify.fwm.collegepa";


    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);


    }
}