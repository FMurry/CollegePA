package simplify.fwm.collegepa.DataStructure;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by fredericmurry on 1/3/16.
 */
public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this);
    }
}