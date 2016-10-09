package xyz.fmsoft.collegepa;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.fmsoft.collegepa.Course.Course;
import xyz.fmsoft.collegepa.utils.ColorPickerDialog;
import xyz.fmsoft.collegepa.utils.Constants;
import xyz.fmsoft.collegepa.utils.FingerPrintDialog;

public class CourseActivity extends AppCompatActivity implements ColorPickerDialog.OnCompleteListener{



    private String courseName;
    private static DatabaseReference root;
    private ColorPickerDialog colorPickerDialog;
    private DatabaseReference user;
    private final String TAG = "CourseActivity";
    private Course current;

    @Bind(R.id.course_toolbar)Toolbar _toolbar;
    @Bind(R.id.course_detail_viewpager)ViewPager _viewPager;
    @Bind(R.id.course_detail_tabs)TabLayout _tabLayout;
    @Bind(R.id.appbar)AppBarLayout _appBarLayout;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    /**
     * The {@link ViewPager} that will host the section contents.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        root = FirebaseDatabase.getInstance().getReference();
        user = root.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        final int position = getIntent().getIntExtra("position",0);
        final String color = getIntent().getStringExtra("color");
        if(color.equals("#FF33B5E5".toLowerCase())){
            setTheme(R.style.AppThemeBlue);
        }
        else if(color.equals(getString(0+R.color.darkblue).toLowerCase())){
            Log.d(TAG, "Setting theme to Dark Blue");
            setTheme(R.style.AppThemeDarkBlue);
        }
        else if(color.equals(getString(0+R.color.purple).toLowerCase())){
            setTheme(R.style.AppThemePurple);
        }
        else if(color.equals(getString(0+R.color.darkpurple).toLowerCase())){
            setTheme(R.style.AppThemeDarkPurple);
        }
        else if(color.equals(getString(0+R.color.green).toLowerCase())){
            setTheme(R.style.AppThemeGreen);
        }
        else if(color.equals(getString(0+R.color.darkgreen).toLowerCase())){
            setTheme(R.style.AppThemeDarkGreen);
        }
        else if(color.equals(getString(0+R.color.orange).toLowerCase())){
            setTheme(R.style.AppThemeOrange);
        }
        else if(color.equals(getString(0+R.color.darkorange).toLowerCase())){
            setTheme(R.style.AppThemeDarkOrange);
        }
        else if(color.equals(getString(0+R.color.red).toLowerCase())){
            setTheme(R.style.AppThemeRed);
        }
        else if(color.equals(getString(0+R.color.darkred).toLowerCase())){
            setTheme(R.style.AppThemeDarkRed);
        }
        else if(color.equals(getString(0+R.color.bluegray).toLowerCase())){
        }
        else if(color.equals(getString(0+R.color.darkbluegray).toLowerCase())){
            setTheme(R.style.AppThemeDark);
        }
        else{
        }
        if(android.os.Debug.isDebuggerConnected()){
            android.os.Debug.waitForDebugger();
        }

        user.child("Courses").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for(DataSnapshot course : dataSnapshot.getChildren()){
                    if(android.os.Debug.isDebuggerConnected()){
                        android.os.Debug.waitForDebugger();
                    }
                    if(i==position){
                        Log.d(TAG,(String)course.child("CourseName").getValue());
                        String courseName = (String)course.child("CourseName").getValue();
                        getSupportActionBar().setTitle(courseName);
                        String courseID = (String)course.child("CourseID").getValue();
                        getSupportActionBar().setSubtitle(courseID);
                        String room = (String)course.child("Room").getValue();
                        String type = (String) course.child("Type").getValue();
                        current = new Course(courseID,courseName,type);
                        boolean sunday = (boolean)course.child("Sunday").getValue();
                        boolean monday = (boolean)course.child("Monday").getValue();
                        boolean tuesday = (boolean)course.child("Tuesday").getValue();
                        boolean wednesday = (boolean)course.child("Wednesday").getValue();
                        boolean thursday = (boolean)course.child("Thursday").getValue();
                        boolean friday = (boolean)course.child("Friday").getValue();
                        boolean saturday = (boolean)course.child("Saturday").getValue();
                        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(current.getColorID())));
                        // _tabLayout.setBackgroundColor(Color.parseColor(current.getColorID()));
                        //_appBarLayout.setBackgroundColor(Color.parseColor(current.getColorID()));
                        current.setSunday(sunday);
                        current.setMonday(monday);
                        current.setTuesday(tuesday);
                        current.setWednesday(wednesday);
                        current.setThursday(thursday);
                        current.setFriday(friday);
                        current.setSaturday(saturday);
                    }
                    i++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,databaseError.getMessage());
            }


        });
        super.onCreate(savedInstanceState);
        Log.d(TAG,"Setting views");
        setContentView(R.layout.activity_course);
        ButterKnife.bind(this);
        setSupportActionBar(_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        courseName = getIntent().getStringExtra("name");
        if(!courseName.equals(null)){
            getSupportActionBar().setTitle(courseName);
        }
        else{
            getSupportActionBar().setTitle("");
        }
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CourseDetailFragment(),"Course");
        //TODO: Change Title of Fragment to course name
        //TODO: Add Assignment Fragments
        //TODO: Change from TabLayout to bottom Nav
        _viewPager.setAdapter(adapter);

        _tabLayout.setupWithViewPager(_viewPager);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.action_edit_color){
            FragmentManager fragmentManager = getSupportFragmentManager();
            colorPickerDialog = new ColorPickerDialog();
            colorPickerDialog.show(fragmentManager, "Colorpickerdialog");
            //TODO: call changeColor();
        }

        if (id == android.R.id.home){
           finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnComplete(String value) {

        courseName = getSupportActionBar().getTitle().toString();// Ensure that courseName does not have default "Course Name Value
        DatabaseReference courseRef = root.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Courses").child(courseName);
        courseRef.child("color").setValue(value);
        finish();
        //TODO: Refresh Activity
    }


    class ViewPagerAdapter extends FragmentPagerAdapter{
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm){
            super(fm);
        }

        /**
         * Return the Fragment associated with a specified position.
         *
         * @param position
         */
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        /**
         * Return the number of views available.
         */
        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        /**
         * This method may be called by the ViewPager to obtain a title string
         * to describe the specified page. This method may return null
         * indicating no title for this page. The default implementation returns
         * null.
         *
         * @param position The position of the title requested
         * @return A title for the requested page
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }


    public void changeColor(String color){
        DatabaseReference user = root.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        user.child("Courses").child(courseName).child("color").setValue(color);
    }



}
