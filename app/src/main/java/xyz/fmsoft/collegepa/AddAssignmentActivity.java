package xyz.fmsoft.collegepa;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.fmsoft.collegepa.Course.Assignment;
import xyz.fmsoft.collegepa.utils.DatePickerFragment;
import xyz.fmsoft.collegepa.utils.TimePickerFragment;

public class AddAssignmentActivity extends AppCompatActivity implements DatePickerFragment.DateListener, TimePickerFragment.TimeListener{

    private static final String TAG = "AddAssignmentActivity";

    @Bind(R.id.add_assignment_name) EditText _name;
    @Bind(R.id.add_assignment_totalPoints)EditText _totalPoints;
    @Bind(R.id.add_assignment_description) EditText _description;
    @Bind(R.id.add_assignment_date)AppCompatButton _date;
    @Bind(R.id.add_assignment_spinner) AppCompatSpinner _spinner;
    @Bind(R.id.add_assignment_time)AppCompatButton _time;

    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment);
        ButterKnife.bind(this);
        //Setting up Spinner
        user = FirebaseAuth.getInstance().getCurrentUser();
        final ArrayList<String> items = new ArrayList<>();
        if(user!=null) {
            DatabaseReference courseBranch = (FirebaseDatabase.getInstance().getReference()).child("users").child(user.getUid()).child("Courses");
            courseBranch.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int size = (int)dataSnapshot.getChildrenCount();
                    if(size > 0){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            items.add(child.child("CourseName").getValue(String.class));
                        }
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, items);
                    _spinner.setAdapter(arrayAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG,databaseError.getMessage());
                }
            });
        }
        //End Setting up spinner
        //Setting up Date Fragment
        _date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        });
        //End Setting up Date Fragment
        //Setting up Time Fragment
        _time.setOnClickListener(new View.OnClickListener() {
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
                addAssignment();
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

    /**
     * Validates if user entered required information
     * @return valid - whether submission is valid or not
     */
    public boolean isValid(){
        boolean valid = true;
        if(_name.getText().toString().isEmpty()){
            valid = false;
            _name.setError("Please Enter Name");
        }
        else{
            _name.setError(null);
        }
        if(_description.getText().toString().isEmpty()){
            valid = false;
            _description.setError("Please Enter Description");
        }
        else{
            _description.setError(null);
        }
        if(_totalPoints.getText().toString().isEmpty()){
            valid = false;
            _totalPoints.setError("Please Enter points assignment is worth");
        }
        else{
            _totalPoints.setError(null);
        }
        if(_date.getText().toString().toLowerCase().equals("due date")){
            valid = false;
            _date.setError("Select a Due date");
        }
        else{
            _date.setError(null);
        }
        if(_time.getText().toString().toLowerCase().equals("due time")){
            valid = false;
            _time.setError("Select a due time");
        }
        else{
            _time.setError(null);
        }

        return valid;
    }

    /**
     * Adds an assignment to Firebase database
     */
    public void addAssignment(){

        if(isValid()){
            String name = _name.getText().toString();
            String description = _description.getText().toString();
            String courseName = _spinner.getSelectedItem().toString();
            String totalPoints = _totalPoints.getText().toString();
            Assignment assignment = new Assignment(name,description, totalPoints);
            assignment.setCourseName(courseName);
            assignment.setDueDate(_date.getText().toString());
            assignment.setDueTime(_time.getText().toString());
            DatabaseReference assignmentBranch = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Assignments").child(name);
            assignment.saveToFirebase(assignmentBranch);
            startActivity(new Intent(this,DrawerActivity.class));
            finish();
        }
        else{
            Toast.makeText(this, "Please fill out name and description", Toast.LENGTH_SHORT).show();
        }


    }

    /**
     * Shows the date picker fragment
     * @param v
     */
    public void showDatePicker(View v){
        DialogFragment dialogFragment = new DatePickerFragment();
        dialogFragment.show(getSupportFragmentManager(),"datePicker");
    }

    /**
     * Shows the time picker fragment
     * @param v
     */
    public void showTimePicker(View v){
        DialogFragment dialogFragment = new TimePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from","start");
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(),"timePicker");
    }

    /**
     * Returns the formatted date in the form yyyy:MM:dd
     * @param date
     */
    @Override
    public void returnFormattedDate(String date) {_date.setText(date);}

    /**
     * Returns formatted time in the form HH:MM
     * @param time
     */
    @Override
    public void returnFormattedTime(String time, String tag) {_time.setText(time);}
}
