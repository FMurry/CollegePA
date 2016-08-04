package xyz.fmsoft.collegepa;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import 	com.google.firebase.database.DataSnapshot;
import 	com.google.firebase.database.DatabaseReference;
import 	com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import 	com.google.firebase.database.ValueEventListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.fmsoft.collegepa.utils.FingerPrintDialog;

//TODO: Get up to date with new Firebase

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        OnFragmentInteractionListener, GoogleApiClient.OnConnectionFailedListener{



    private static final int REQUEST_LOGIN = 1;
    private static final int REQUEST_ADD = 2;
    private static final int REQUEST_FINGERPRINT = 3;
    private static final String TAG = "DrawerActivity";
    private DatabaseReference root;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.main_layout)
    RelativeLayout mainLayout;
    @Bind(R.id.flcontent)
    FrameLayout _frameLayout;
    private ActionBarDrawerToggle toggle;
    private TextView userName;
    private TextView userEmail;
    private ImageView userPic;
    private boolean loggedIn;
    private MenuItem account_drawer;
    private Snackbar snackbarConnection;
    private int theme = 0;
    private int verified = 0;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Starting Drawer Activity");
         gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.google_web_client_id))
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        root = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_drawer);
        ButterKnife.bind(this);
        toolbar.setTitle("Courses");
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        snackbarConnection = Snackbar.make(_frameLayout, "No Connection", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loading();
            }
        });
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        try {
            Fragment fragment = CourseFragment.class.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flcontent, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        View headerLayout = navigationView.getHeaderView(0);
        userName = (TextView) headerLayout.findViewById(R.id.user_name);
        userEmail = (TextView) headerLayout.findViewById(R.id.user_email);
        userPic = (ImageView) headerLayout.findViewById(R.id.user_photo);
        //profilePic = (ImageView)headerLayout.findViewById(R.id.profileImage);
        Menu navMenu = navigationView.getMenu();
        account_drawer = (MenuItem) navMenu.findItem(R.id.nav_Account);
        if(android.os.Debug.isDebuggerConnected()){
            android.os.Debug.waitForDebugger();
        }
        mAuthStateListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() !=null){
                    final StringBuilder providers = new StringBuilder("");
                    for(String str : firebaseAuth.getCurrentUser().getProviders()){
                        providers.append(str);
                    }
                    if(providers.toString().contains("google.com")){
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        userName.setText(user.getDisplayName());
                        userEmail.setText(user.getEmail());
                        Uri photoUri = firebaseAuth.getCurrentUser().getPhotoUrl();
                        Picasso.with(DrawerActivity.this)
                                .load(photoUri)
                                .placeholder(android.R.drawable.sym_def_app_icon)
                                .error(android.R.drawable.sym_def_app_icon)
                                .into(userPic);




                    }
                    else if(providers.toString().contains("facebook.com")){
                        FacebookSdk.sdkInitialize(getApplicationContext());
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        userName.setText(user.getDisplayName());
                        userEmail.setText(user.getEmail());
                        Uri photoUri = user.getPhotoUrl();
                        Picasso.with(DrawerActivity.this)
                                .load(photoUri)
                                .placeholder(android.R.drawable.sym_def_app_icon)
                                .error(android.R.drawable.sym_def_app_icon)
                                .into(userPic);
                    }
                    else {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        userName.setText(user.getDisplayName());
                        userEmail.setText(user.getEmail());

                    }
                }
                else {
                    Intent loginActivity = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(loginActivity);
                    finish();
                }
            }
        };
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_course) {
            //TODO: Change into FAB With roll animation for course and assignment-k
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            addCourseDialog addCourseDialog = new addCourseDialog();
//            addCourseDialog.show(fragmentManager, "fragment_add_course");

            startActivity(new Intent(this,AddCourseActivity.class));
            finish();
            return true;
        }

        if (id == R.id.action_add_assignment) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        boolean frag = true;
        Class fragmentClass = null;
        switch (item.getItemId()) {
            case R.id.nav_Courses:
                //Handle the Course action
                toolbar.setTitle("Courses");
                fragmentClass = CourseFragment.class;
                break;
            case R.id.nav_Assignments:
                toolbar.setTitle("Assignments");
                fragmentClass = AssignmentFragment.class;
                break;
            case R.id.nav_Grades:
                if (Build.VERSION.SDK_INT >= 23 && FingerprintManagerCompatApi23.isHardwareDetected(this)
                        && FingerprintManagerCompatApi23.hasEnrolledFingerprints(this)) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FingerPrintDialog fingerPrintDialog = new FingerPrintDialog();
                    fingerPrintDialog.show(fragmentManager, "FingerPrintDialog");
                    toolbar.setTitle("Grades");
                    fragmentClass = GradesFragment.class;
                } else {
                    toolbar.setTitle("Grades");
                    fragmentClass = GradesFragment.class;
                }
                break;
            case R.id.nav_About:
                toolbar.setTitle("About");
                fragmentClass = AboutFragment.class;
                break;
            case R.id.nav_Settings:
                frag = false;
                Intent settingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(settingsActivity);
                break;
            case R.id.nav_Account:
                Intent loginActivity = new Intent(this, LoginActivity.class);
                if (user == null) {
                    startActivityForResult(loginActivity, REQUEST_LOGIN);
                    frag = false;
                } else {
                    toolbar.setTitle("Account");
                    fragmentClass = AccountFragment.class;
                }
                break;

            case R.id.nav_Contact:
                Intent emailClient = new Intent(Intent.ACTION_SEND);
                frag = false;
                emailClient.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.email_contact)});
                emailClient.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
                emailClient.putExtra(Intent.EXTRA_TEXT, "");
                emailClient.setType("message/rfc822");
                startActivity(Intent.createChooser(emailClient, "Send Email using"));
                break;
            case R.id.nav_Log_out:
                StringBuilder str = new StringBuilder("");
                user = FirebaseAuth.getInstance().getCurrentUser();
                for(String s : user.getProviders()){
                    str.append(s);
                }
                if (str.toString().contains("google.com")) {
                    DisconnectGoogleAccount();
                }
                else if(str.toString().contains("facebook.com")){
                    LoginManager.getInstance().logOut();
                    FirebaseAuth.getInstance().signOut();
                }
                else {
                    FirebaseAuth.getInstance().signOut();
                }
                recreate();
                break;
            default:
                fragmentClass = CourseFragment.class;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        if (frag) {
            try {
                fragment = (Fragment) fragmentClass.newInstance();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flcontent, fragment).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return true;
    }


    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.account_log_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Intent refresh = new Intent(this, DrawerActivity.class);
                startActivity(refresh);
            }
        } else if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                Intent refresh = new Intent(this, DrawerActivity.class);
                startActivity(refresh);
            }
        }
    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * {@link #onResumeFragments()}.
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    public void Loading() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching Courses");
        progressDialog.show();
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        }, 1000);
        if (isConnected()) {
            if (snackbarConnection.isShown()) {
                Log.d(TAG, "Dismissing Snackbar");
                snackbarConnection.dismiss();
            }
        } else {
            showConnectionSnack();
        }
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean connected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return connected;
    }

    public void showConnectionSnack() {
        Log.d(TAG, "Showing snackbar");
        snackbarConnection.setActionTextColor(Color.RED);
        View snackbarView = snackbarConnection.getView();
        TextView sText = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        sText.setTextColor(Color.YELLOW);
        snackbarConnection.show();
    }

    public void StartGrade() {
        if (verified == 1) {
            Fragment fragment = (Fragment) GradesFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flcontent, fragment).commit();
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(DrawerActivity.this, "Google Connection Failed   ", Toast.LENGTH_SHORT).show();
    }


    public void DisconnectGoogleAccount(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                FirebaseAuth.getInstance().signOut();
                Log.d(TAG,"Google Successfully Signed out");
            }
        });
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.d(TAG,"Google Account Revoked");
                    }
                });
        mGoogleApiClient.disconnect();
        FirebaseAuth.getInstance().signOut();
        Log.d(TAG,"Goole API Client Disconnected");
    }

}
