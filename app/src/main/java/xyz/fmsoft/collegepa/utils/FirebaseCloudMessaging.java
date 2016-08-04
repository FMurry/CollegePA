package xyz.fmsoft.collegepa.utils;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by fredericmurry on 6/12/16.
 */
public class FirebaseCloudMessaging extends FirebaseInstanceIdService {

    private static final String TAG = "FirebaseCloudMessaging";
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG,"Refreshed Token:" + refreshedToken);

    }
}
