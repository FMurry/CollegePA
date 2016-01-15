package simplify.fwm.collegepa;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import simplify.fwm.collegepa.Course.Course;

/**
 * Created by fredericmurry on 1/14/16.
 */
public class addCourseDialog extends DialogFragment {

    @Bind(R.id.add_course_name)EditText _courseName;
    @Bind(R.id.add_course_id)EditText _courseID;
    @Bind(R.id.add_course_spinner)Spinner _courseTypeSpinner;
    @Bind(R.id.add_course_add)TextView _addCourse;
    @Bind(R.id.add_course_cancel)TextView _cancelCourse;


    public addCourseDialog(){
        // Empty constructor required for DialogFragment

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_course, container);
        ButterKnife.bind(this,view);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.course_types,
                R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _courseTypeSpinner.setAdapter(adapter);
        getDialog().setTitle("Add a Course");

        _addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCourse();
            }
        });

        _cancelCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Close();
            }
        });
        return view;
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
            Toast.makeText(getContext(), "Please Enter Name", Toast.LENGTH_SHORT).show();
        }
        //If Course ID is empty show toast
        else if(id.isEmpty()){
            Toast.makeText(getContext(),"Please Enter Course ID",Toast.LENGTH_SHORT).show();
        }

        else{
            Course course = new Course(id,name,type);
            ParseObject courseObj = new ParseObject("Course");
            course.SaveToParse(courseObj);
            courseObj.saveEventually();
            Close();
            getActivity().recreate();
        }
    }

    /**
     * Closes the dialog
     */
    public void Close(){
        this.dismiss();
    }

}
