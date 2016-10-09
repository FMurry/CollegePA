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
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private DatabaseReference root;
    private DatabaseReference user;
    private FirebaseAuth firebaseAuth;
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
        firebaseAuth = FirebaseAuth.getInstance();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_course_detail, container, false);
        ButterKnife.bind(this, v);
        root = FirebaseDatabase.getInstance().getReference();
        String uID = firebaseAuth.getCurrentUser().getUid();
        user =  root.child("users").child(uID);
        getActivity().getIntent().getIntExtra("position",position);
        position = getActivity().getIntent().getIntExtra("position",0);
        Log.d(TAG,"Course is at position: "+String.valueOf(position));

        return v;
    }

}
