package simplify.fwm.collegepa.DataStructure;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.SaveCallback;

/**
 * Created by fredericmurry on 1/3/16.
 */
public class ParseApplication extends Application {

    private static final String TAG = "simplify.fwm.collegepa";


    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this);
        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("device", Build.MODEL);
        installation.put("androidOS", Build.VERSION.SDK_INT);
        installation.saveInBackground();

    }
}