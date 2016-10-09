package xyz.fmsoft.collegepa.DataStructure;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.facebook.FacebookSdk;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by fredericmurry on 1/3/16.
 */
public class CollegepaApplication extends Application {

    private static final String TAG = "xyz.fmsoft.collegepa";


    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


    }

    /**
     * Set the base context for this ContextWrapper.  All calls will then be
     * delegated to the base context.  Throws
     * IllegalStateException if a base context has already been set.
     *
     * @param base The new base context for this wrapper.
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}