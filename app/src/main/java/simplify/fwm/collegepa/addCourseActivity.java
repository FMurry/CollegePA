package simplify.fwm.collegepa;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

import simplify.fwm.collegepa.Course.Course;

public class addCourseActivity extends AppCompatActivity {

    private static final String TAG = "addCourseActivity";

    private Toolbar toolbar;
    private EditText ETCourseName;
    private EditText ETFullCourseName;
    Spinner courseTypeSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        toolbar = (Toolbar) findViewById(R.id.addtoolbar);
        toolbar.setTitle("Add Course");
        setSupportActionBar(toolbar);

        courseTypeSpinner = (AppCompatSpinner)findViewById(R.id.course_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.course_types,
                R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseTypeSpinner.setAdapter(adapter);
        ETCourseName = (EditText)findViewById(R.id.course_name);
        ETFullCourseName = (EditText)findViewById(R.id.course_Full_Name);

    }

    public void createCourse(View v){
        switch (v.getId()){
            case R.id.course_Full_Name:
                if(ETFullCourseName.getText().toString().contains(getResources().getText(R.string.FullNameExample))){
                    ETFullCourseName.setText("");
                    Log.d(TAG,"Clearing Name");
                }
                break;
            case R.id.course_name:
                if(ETCourseName.getText().toString().contains(getResources().getText(R.string.NameExample))){
                    ETCourseName.setText("");
                    Log.d(TAG,"Clearing ID");
                }
                break;
            case R.id.submit_course:
                String name = ETCourseName.getText().toString();
                String fullName = ETFullCourseName.getText().toString();
                String type = "0";
                if(courseTypeSpinner.getSelectedItem().toString().equals("Lecture")){
                    Log.d(TAG,"Lecture Captured");
                    type = "0";
                }
                else if(courseTypeSpinner.getSelectedItem().toString().equals("Seminar")){
                    Log.d(TAG,"Seminar Captured");
                    type = "1";
                }
                else if(courseTypeSpinner.getSelectedItem().toString().equals("Lab")){
                    Log.d(TAG,"Lab Captured");
                    type = "2";
                }
                else if(courseTypeSpinner.getSelectedItem().toString().equals("Workshop")){
                    Log.d(TAG,"Workshop Captured");
                    type = "3";
                }

                //If Name is empty show Toast
                if(fullName.equals("") || fullName.equals(null)){
                    Toast.makeText(this,"Please Enter Name", Toast.LENGTH_SHORT).show();
                }
                //If Course ID is empty show toast
                else if(name.equals("") || name.equals(null)){
                    Toast.makeText(this,"Please Enter Course ID",Toast.LENGTH_SHORT).show();
                }

                else{
                    Course course = new Course(name,fullName,type);
                    Log.d(TAG, "Submitting Course");
                    ParseObject courseObj = new ParseObject("Course");
                    course.SaveToParse(courseObj);
                    courseObj.saveEventually();
                    setResult(Activity.RESULT_OK, null);
                    finish();
                }
                break;
        }
    }

}
//TODO: REDO ADD COURSE LAYOUT