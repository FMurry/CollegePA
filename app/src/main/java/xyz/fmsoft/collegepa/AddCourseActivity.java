package xyz.fmsoft.collegepa;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.fmsoft.collegepa.Course.Course;
import xyz.fmsoft.collegepa.utils.Constants;
import xyz.fmsoft.collegepa.utils.TimePickerFragment;

public class AddCourseActivity extends AppCompatActivity implements TimePickerFragment.TimeListener{

    //TODO: REDO UI FOR ADD ACTIVITY


    @Bind(R.id.add_course_name)EditText _courseName;
    @Bind(R.id.add_course_id)EditText _courseID;
    @Bind(R.id.add_course_room)EditText _courseRoom;
    @Bind(R.id.add_course_spinner)Spinner _courseTypeSpinner;
    @Bind(R.id.add_course_monday)CheckBox _monday;
    @Bind(R.id.add_course_tuesday)CheckBox _tuesday;
    @Bind(R.id.add_course_wednesday)CheckBox _wednesday;
    @Bind(R.id.add_course_thursday)CheckBox _thursday;
    @Bind(R.id.add_course_friday)CheckBox _friday;
    @Bind(R.id.add_course_saturday)CheckBox _saturday;
    @Bind(R.id.add_course_sunday)CheckBox _sunday;
    @Bind(R.id.add_course_start_time)AppCompatButton _startTime;
    @Bind(R.id.add_course_end_time)AppCompatButton _endTime;
    private static final String TAG = "AddCourseActivity";

    private FirebaseUser user;
    private DatabaseReference root;
    private DatabaseReference userBranch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        ButterKnife.bind(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG,user.getUid());
        root = FirebaseDatabase.getInstance().getReference();
        userBranch = root.child("users").child(user.getUid());

        if(getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getBaseContext(), R.array.course_types,
                R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _courseTypeSpinner.setAdapter(adapter);
        this.setTitle("Add a Course");

        _courseID.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        _startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(v);
            }
        });
        _endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(v);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_course, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()){
            case R.id.action_submit:
                addCourse();
                break;
            case android.R.id.home:
                startActivity(new Intent(this,DrawerActivity.class));
                finish();
                break;
        }
       return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,DrawerActivity.class));
        finish();
    }

    public void addCourse(){
        String name = _courseName.getText().toString();
        String id = _courseID.getText().toString();
        String type = "";
        if(_courseTypeSpinner.getSelectedItem().toString().equals("Lecture")){
            type = "0";
        }
        else if(_courseTypeSpinner.getSelectedItem().toString().equals("Seminar")){
            type = "1";
        }
        else if(_courseTypeSpinner.getSelectedItem().toString().equals("Lab")){
            type = "2";
        }
        else if(_courseTypeSpinner.getSelectedItem().toString().equals("Workshop")){
            type = "3";
        }
        //If Name is empty show Toast
        if(name.isEmpty()){
            Toast.makeText(getBaseContext(), "Please Enter Name", Toast.LENGTH_SHORT).show();
        }
        //If Course ID is empty show toast
        else if(id.isEmpty()){
            Toast.makeText(getBaseContext(),"Please Enter Course ID",Toast.LENGTH_SHORT).show();
        }

        else if(_courseRoom.getText().toString().isEmpty()){
            Toast.makeText(getBaseContext(), "Please Enter Room Number", Toast.LENGTH_SHORT).show();
        }
        //Start time entered but not end time
        else if(_startTime.getText().toString().toLowerCase().equals("start time") && !(_endTime.getText().toString().toLowerCase().equals("end time"))){
            Toast.makeText(this, "Must have end time with start time", Toast.LENGTH_SHORT).show();
        }
        //End Time entered but not start time
        else if(!(_startTime.getText().toString().toLowerCase().equals("start time")) && _endTime.getText().toString().toLowerCase().equals("end time")){
            Toast.makeText(this, "Must have start time with end time", Toast.LENGTH_SHORT).show();
        }

        else{
            Course course = new Course(id,name,type);
            course.setRoom(_courseRoom.getText().toString());
            course.setSunday(_sunday.isChecked());
            course.setMonday(_monday.isChecked());
            course.setTuesday(_tuesday.isChecked());
            course.setWednesday(_wednesday.isChecked());
            course.setThursday(_thursday.isChecked());
            course.setFriday(_friday.isChecked());
            course.setSaturday(_saturday.isChecked());
            course.setColorID(getResources().getString(0+R.color.colorPrimary));
            if(!(_startTime.getText().toString().toLowerCase().equals("start time"))){
                course.setStartTime(_startTime.getText().toString());
            }
            if(!(_endTime.getText().toString().toLowerCase().equals("end time"))){
                course.setEndTime(_endTime.getText().toString());
            }
            DatabaseReference courseBranch = userBranch.child("Courses").child(name);
            course.saveToFirebase(courseBranch);
            startActivity(new Intent(this,DrawerActivity.class));
            finish();

        }
    }

    /**
     * Shows the time picker fragment
     * @param v
     */
    public void showTimePicker(View v){
        DialogFragment dialogFragment = new TimePickerFragment();
        Bundle bundle = new Bundle();
        if(v.getId() == R.id.add_course_start_time){
            bundle.putString("from","start");
        }
        else{
            bundle.putString("from","end");
        }

        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(),"timePicker");

    }

    @Override
    public void returnFormattedTime(String time, String tag) {
        if(tag.equals("start")){
            _startTime.setText(time);
        }
        else{
            _endTime.setText(time);
        }
    }
}
