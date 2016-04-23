package simplify.fwm.collegepa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import butterknife.Bind;
import butterknife.ButterKnife;
import simplify.fwm.collegepa.Course.Course;
import simplify.fwm.collegepa.utils.Constants;

public class AddCourseActivity extends AppCompatActivity {

    @Bind(R.id.add_course_name)EditText _courseName;
    @Bind(R.id.add_course_id)EditText _courseID;
    @Bind(R.id.add_course_room)EditText _courseRoom;
    @Bind(R.id.add_course_spinner)Spinner _courseTypeSpinner;
    @Bind(R.id.add_course_add)AppCompatButton _addCourse;
    @Bind(R.id.add_course_cancel)AppCompatButton _cancelCourse;
    @Bind(R.id.add_course_monday)CheckBox _monday;
    @Bind(R.id.add_course_tuesday)CheckBox _tuesday;
    @Bind(R.id.add_course_wednesday)CheckBox _wednesday;
    @Bind(R.id.add_course_thursday)CheckBox _thursday;
    @Bind(R.id.add_course_friday)CheckBox _friday;
    @Bind(R.id.add_course_saturday)CheckBox _saturday;
    @Bind(R.id.add_course_sunday)CheckBox _sunday;

    private AuthData user;
    private Firebase root;
    private Firebase userBranch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        ButterKnife.bind(this);

        root = new Firebase(Constants.FIREBASE_ROOT_URL);
        user = root.getAuth();
        userBranch = root.child("users").child(user.getUid());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getBaseContext(), R.array.course_types,
                R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _courseTypeSpinner.setAdapter(adapter);
        this.setTitle("Add a Course");

        _courseID.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        _addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCourse();
            }
        });

        _cancelCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DrawerActivity.class));
                finish();
            }
        });
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
            Firebase courseBranch = userBranch.child("Courses").child(name);
            course.saveToFirebase(courseBranch);
            startActivity(new Intent(this,DrawerActivity.class));
            finish();
            //TODO: Need to refresh previous activity when finished with this one
        }
    }

}
