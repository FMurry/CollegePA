package simplify.fwm.collegepa;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import simplify.fwm.collegepa.Course.Course;
import simplify.fwm.collegepa.DataStructure.CourseCardAdapter;


public class CourseFragment extends Fragment {

    private List<Course> courses;
    private RecyclerView recycler;
    private RecyclerView.LayoutManager rvLayout;
    private RecyclerView.Adapter rvAdapter;
    private TextView noCourses;
    private ImageView ncIcon;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ParseQuery<ParseObject> courseQuery;
    private int size;


    private static final String TAG = "CourseFragment";

    private OnFragmentInteractionListener mListener;

    public CourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseFragment newInstance(String param1, String param2) {
        CourseFragment fragment = new CourseFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        courseQuery = ParseQuery.getQuery("Course");
        courseQuery.whereEqualTo("createdby", ParseUser.getCurrentUser());
        courseQuery.orderByAscending("CourseID");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_course, container, false);

        ncIcon = (ImageView)v.findViewById(R.id.nc_icon);
        noCourses = (TextView)v.findViewById(R.id.tv_no_courses);


        recycler = (RecyclerView)v.findViewById(R.id.recycler_view);
        recycler.setHasFixedSize(true);

        rvLayout = new LinearLayoutManager(v.getContext());
        recycler.setLayoutManager(rvLayout);
        courseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> courseList, ParseException e) {
                if (e == null) {
                    Log.d(TAG, "Retrieved" + courseList.size());
                    courses = new ArrayList<Course>();
                    ParseCourses(courseList);
                    rvAdapter = new CourseCardAdapter(courses);
                    Log.d(TAG, "course size: " + courses.size());
                    recycler.setAdapter(rvAdapter);
                    Log.d(TAG, "Adapter Set");
                    if (size != 0) {
                        ncIcon.setVisibility(View.INVISIBLE);
                        noCourses.setVisibility(View.INVISIBLE);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Refresh();
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                },3000);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return v;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            Log.d(TAG,"Attach succeeded");
            mListener = (OnFragmentInteractionListener) getActivity();
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+
                    "Must Implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void ParseCourses(List<ParseObject> objects){

        size = objects.size();
        for(int i = 0; i<size;i++){
            ParseObject current = objects.get(i);
            String newID = current.getString("CourseID");
            String newFullName = current.getString("CourseName");
            String newType = current.getString("Type");
            Course course = new Course(newID,newFullName,newType);
            courses.add(course);
        }

    }

    public void Refresh(){
        Fragment fragment = this;
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(fragment);
        fragmentTransaction.attach(fragment);
        fragmentTransaction.commit();
        recycler.setAdapter(null);

    }
}