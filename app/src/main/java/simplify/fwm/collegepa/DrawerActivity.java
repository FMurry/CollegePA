package simplify.fwm.collegepa;


        import android.app.Activity;
        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.graphics.Color;
        import android.net.Uri;
        import android.os.Build;
        import android.os.Bundle;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.support.design.widget.NavigationView;
        import android.support.v4.view.GravityCompat;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.ActionBarDrawerToggle;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.parse.Parse;
        import com.parse.ParseObject;
        import com.parse.ParseUser;

        import butterknife.Bind;
        import butterknife.ButterKnife;
        import simplify.fwm.collegepa.Course.Course;

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        OnFragmentInteractionListener{

    private static final int REQUEST_LOGIN = 1;
    private static final int REQUEST_ADD = 2;
    private static final String TAG="DrawerActivity";

    @Bind(R.id.drawer_layout) DrawerLayout drawer;
    @Bind(R.id.nav_view) NavigationView navigationView;
    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.main_layout) RelativeLayout mainLayout;

    private ActionBarDrawerToggle toggle;
    private ParseUser currentUser;
    private TextView userName;
    private TextView userEmail;
    private boolean loggedIn;
    private MenuItem account_drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        ButterKnife.bind(this);
        toolbar.setTitle("Courses");
        setSupportActionBar(toolbar);

        currentUser = ParseUser.getCurrentUser();
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        try {
            Fragment fragment = (Fragment) CourseFragment.class.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flcontent,fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        View headerLayout = navigationView.getHeaderView(0);
        userName = (TextView)headerLayout.findViewById(R.id.user_name);
        userEmail = (TextView)headerLayout.findViewById(R.id.user_email);
        Menu navMenu = navigationView.getMenu();
        account_drawer = (MenuItem)navMenu.findItem(R.id.nav_Account);
        if(currentUser==null){
            account_drawer.setTitle("Log In");
        }
        if(currentUser!=null) {
            userName.setText("Welcome " + currentUser.get("firstName") + "!");
            userEmail.setText(currentUser.getEmail());

            //What device is the user using?
            currentUser.put("deviceName",Build.MODEL);
            currentUser.saveEventually();

            Loading();
        }
        else{
            Intent loginActivity = new Intent(this, simplify.fwm.collegepa.LoginActivity.class);
            startActivityForResult(loginActivity, REQUEST_LOGIN);

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
        if (id == R.id.action_add) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        boolean frag = true;
        Class fragmentClass=null;
        switch (item.getItemId()) {
            case R.id.nav_Courses:
                //Handle the Course action
                toolbar.setTitle("Courses");
                fragmentClass = CourseFragment.class;
                Loading();
                break;
            case R.id.nav_Assignments:
                toolbar.setTitle("Assignments");
                fragmentClass= AssignmentFragment.class;
                break;
            case R.id.nav_About:
                toolbar.setTitle("About");
                fragmentClass = AboutFragment.class;
                break;
            case R.id.nav_Settings:
                frag = false;
                Intent settingsActivity = new Intent(this,SettingsActivity.class);
                startActivity(settingsActivity);
                break;
            case R.id.nav_Account:
                Intent loginActivity = new Intent(this, LoginActivity.class);
                if(currentUser == null){
                    startActivityForResult(loginActivity, REQUEST_LOGIN);
                    frag=false;
                }
                else{
                    toolbar.setTitle("Account");
                    Log.d(TAG, String.valueOf(Build.VERSION.SDK_INT));
                    fragmentClass=AccountFragment.class;
                }
                break;

            case R.id.nav_Contact:
                Intent emailClient = new Intent(Intent.ACTION_SEND);
                frag = false;
                emailClient.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.email_contact)});
                emailClient.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.email_subject));
                emailClient.putExtra(Intent.EXTRA_TEXT,"");
                emailClient.setType("message/rfc822");
                startActivity(Intent.createChooser(emailClient,"Send Email using"));
                break;
            case R.id.nav_Log_out:
                ParseUser.logOut();
                recreate();
                break;
            default:
                fragmentClass = CourseFragment.class;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        if(frag) {
            try {
                fragment = (Fragment) fragmentClass.newInstance();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flcontent,fragment).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        return true;
    }

    public void ActionPressed(MenuItem v){
        switch (v.getItemId()){
            case R.id.action_add:
                Intent add = new Intent(this, addCourseActivity.class);
                startActivityForResult(add, REQUEST_ADD);
                recreate();
                break;

        }
    }

    public void OnClick(View v){
        switch (v.getId()){
            case R.id.account_log_out:
                ParseUser.logOut();
                currentUser = ParseUser.getCurrentUser();
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
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                Intent refresh = new Intent(this, DrawerActivity.class);
                startActivity(refresh);
            }
        }
        else if(requestCode == 2){
            if(resultCode == Activity.RESULT_OK){
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

    public void Loading(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching Courses");
        progressDialog.show();

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        },1000);
    }
}