package xyz.fmsoft.collegepa;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.fmsoft.collegepa.Course.Course;
import xyz.fmsoft.collegepa.DataStructure.CourseCardAdapter;
import xyz.fmsoft.collegepa.utils.Constants;


public class CourseFragment extends Fragment {

    @Bind(R.id.recycler_view) RecyclerView recycler;
    @Bind(R.id.tv_no_courses) TextView noCourses;
    @Bind(R.id.nc_icon) ImageView ncIcon;
    @Bind(R.id.swipe_refresh)SwipeRefreshLayout swipeRefreshLayout;

    private List<Course> courses;
    private RecyclerView.LayoutManager rvLayout;
    private RecyclerView.Adapter rvAdapter;
    private int size;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference root;
    private DatabaseReference userCourseBranch;


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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_course, container, false);
        ButterKnife.bind(this, v);

        firebaseAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();
        if(user!=null) {
            userCourseBranch = root.child("users").child(user.getUid()).child("Courses");
            Query query = userCourseBranch.orderByKey();
            userCourseBranch.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    size = (int)dataSnapshot.getChildrenCount();
                }

                @Override
                public void onCancelled(DatabaseError firebaseError) {

                }
            });
        }



        recycler.setHasFixedSize(true);

        rvLayout = new LinearLayoutManager(v.getContext());
//        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        );
//        recycler.setLayoutParams(layoutParams);
        recycler.setLayoutManager(rvLayout);

        //If there is network available find all data from cloud
        if(isConnected()) {
            Log.d(TAG,"Retrieving from Cloud");
            courses = new ArrayList<>();
            //ParseCourses(courseList);
            getFirebaseCourses(userCourseBranch);

        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Refresh();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        if(size == 0){
            Log.d(TAG, "No Courses Message Visible");
            noCourses.setVisibility(View.VISIBLE);
            ncIcon.setVisibility(View.VISIBLE);
        }
        else{
            Log.d(TAG, "No Courses Message Gone");
            noCourses.setVisibility(View.GONE);
            ncIcon.setVisibility(View.GONE);
        }

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



    public void getFirebaseCourses(DatabaseReference courseBranch){
        if(firebaseAuth.getCurrentUser()!=null) {
//            courseBranch.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    saveCourse(dataSnapshot);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError firebaseError) {
//                    Log.d(TAG, firebaseError.getMessage());
//                }
//            });
            courseBranch.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    saveCourse(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, databaseError.getMessage());
                }
            });
        }
    }

    public void saveCourse(DataSnapshot snapshot){
        if(android.os.Debug.isDebuggerConnected()){
            android.os.Debug.waitForDebugger();
        }
        for(DataSnapshot child : snapshot.getChildren()){
            try {
                String courseName = (String) child.child("CourseName").getValue(String.class);
                String courseID = (String) child.child("CourseID").getValue(String.class);
                String type = (String) child.child("Type").getValue(String.class);
                Course course = new Course(courseID, courseName, type);
                course.setRoom((String) child.child("Room").getValue(String.class));
                course.setSunday((boolean) child.child("Sunday").getValue(boolean.class));
                course.setMonday((boolean) child.child("Monday").getValue(boolean.class));
                course.setTuesday((boolean) child.child("Tuesday").getValue(boolean.class));
                course.setWednesday((boolean) child.child("Wednesday").getValue(boolean.class));
                course.setThursday((boolean) child.child("Thursday").getValue(boolean.class));
                course.setFriday((boolean) child.child("Friday").getValue(boolean.class));
                course.setSaturday((boolean) child.child("Saturday").getValue(boolean.class));
                course.setColorID((String)child.child("color").getValue(String.class));
                course.setStartTime(child.child("StartTime").getValue(String.class));
                course.setEndTime(child.child("EndTime").getValue(String.class));
                boolean duplicate = false;
                for(int i = 0;i<courses.size();i++){
                    if(courses.get(i).equals(course)){
                        duplicate = true;
                    }
                }
                if(!duplicate){
                    courses.add(course);
                }
            }
            catch(NullPointerException ex){

            }
            catch (Exception e){
                //TODO: Report the error and send
                firebaseAuth.signOut();

            }


        }
        rvAdapter = new CourseCardAdapter(courses);
        Log.d(TAG, "course size: " + courses.size());

        recycler.setAdapter(rvAdapter);
        Log.d(TAG, "Adapter Set");
    }

    /**
     * Update all items on fragment
     */
    public void Refresh(){
        Fragment fragment = this;
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(fragment);
        fragmentTransaction.attach(fragment);
        fragmentTransaction.commit();
        //getFirebaseCourses(userCourseBranch);

        recycler.setAdapter(null);

    }


    /**
     * Determines whether android device has an internet connection
     * @return true if connected to internet false if not
     */
    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager)this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean connected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return connected;
    }
}