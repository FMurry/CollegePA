package xyz.fmsoft.collegepa;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.fmsoft.collegepa.Course.Course;
import xyz.fmsoft.collegepa.utils.Constants;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseDetailFragment extends Fragment {


    @Bind(R.id.CourseDetail_name)TextView _courseName;
    private static final String TAG = "CourseDetailFragment";
    private Firebase user;
    private int position;
    private Course current;


    public CourseDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseDetailFragment newInstance(String param1, String param2) {
        CourseDetailFragment fragment = new CourseDetailFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_course_detail, container, false);
        ButterKnife.bind(this, v);
        String uID = new Firebase(Constants.FIREBASE_ROOT_URL).getAuth().getUid();
        user = new Firebase(Constants.FIREBASE_ROOT_URL).child("users").child(uID);
        getActivity().getIntent().getIntExtra("position",position);
        position = getActivity().getIntent().getIntExtra("position",0);
        Log.d(TAG,"Course is at position: "+String.valueOf(position));
        user.child("Courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for(DataSnapshot course : dataSnapshot.getChildren()){

                    if(i==position){
                        Log.d(TAG,(String)course.child("CourseName").getValue());
                        String courseName = (String)course.child("CourseName").getValue();
                        _courseName.setText(courseName);
                        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(courseName);
                        String courseID = (String)course.child("CourseID").getValue();
                        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(courseID);
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
            public void onCancelled(FirebaseError firebaseError) {
                Log.d(TAG, firebaseError.getMessage());
            }
        });

        return v;
    }

}
