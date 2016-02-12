package simplify.fwm.collegepa;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23;
import android.support.v7.widget.RecyclerView;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import simplify.fwm.collegepa.utils.Constants;

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        OnFragmentInteractionListener {


    //TODO: FACEBOOK FUCKED PARSE NEED A NEW BACKEND FOR DATA

    private static final int REQUEST_LOGIN = 1;
    private static final int REQUEST_ADD = 2;
    private static final int REQUEST_FINGERPRINT = 3;
    private static final String TAG = "DrawerActivity";
    private final Firebase root = new Firebase(Constants.FIREBASE_ROOT_URL);

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
    private boolean loggedIn;
    private MenuItem account_drawer;
    private Snackbar snackbarConnection;
    private int theme = 0;
    private int verified = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Menu navMenu = navigationView.getMenu();
        account_drawer = (MenuItem) navMenu.findItem(R.id.nav_Account);
        if (root.getAuth() == null) {
            account_drawer.setTitle("Log In");
        }

        root.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if(authData !=null){
                    root.child("users").child(root.getAuth().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userName.setText("Welcome " + dataSnapshot.child("firstName").getValue(String.class));
                    userEmail.setText(dataSnapshot.child("email").getValue(String.class));
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });


            Loading();
                }
                else {
                    Intent loginActivity = new Intent(getBaseContext(), simplify.fwm.collegepa.LoginActivity.class);
                    startActivityForResult(loginActivity, REQUEST_LOGIN);
                }
            }
        });
//        if (root.getAuth() != null) {
//
//            //TODO: Get name and email of user
//            root.child("users").child(root.getAuth().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    userName.setText("Welcome " + dataSnapshot.child("firstName").getValue(String.class));
//                    userEmail.setText(dataSnapshot.child("email").getValue(String.class));
//                }
//
//                @Override
//                public void onCancelled(FirebaseError firebaseError) {
//
//                }
//            });
//
//
//            Loading();
//        } else {
//            Intent loginActivity = new Intent(this, simplify.fwm.collegepa.LoginActivity.class);
//            startActivityForResult(loginActivity, REQUEST_LOGIN);
//
//        }
        if (!isConnected()) {
            showConnectionSnack();
        }

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
            FragmentManager fragmentManager = getSupportFragmentManager();
            addCourseDialog addCourseDialog = new addCourseDialog();
            addCourseDialog.show(fragmentManager, "fragment_add_course");
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
                if (root.getAuth() == null) {
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
                root.unauth();
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
                root.unauth();
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


}
