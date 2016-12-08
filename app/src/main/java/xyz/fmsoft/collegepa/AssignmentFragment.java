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
import xyz.fmsoft.collegepa.Course.Assignment;
import xyz.fmsoft.collegepa.DataStructure.AssignmentListAdapter;
import xyz.fmsoft.collegepa.utils.DividerItemDecoration;


public class AssignmentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "AssignmentFragment";

    @Bind(R.id.recycler_view_assignments) RecyclerView recycler;
    @Bind(R.id.assignment_swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.tv_no_assignments) TextView noAssignments;
    @Bind(R.id.na_icon) ImageView naIcon;
    // TODO: Rename and change types of parameters



    private List<Assignment> assignments;
    private RecyclerView.LayoutManager rvLayout;
    private RecyclerView.Adapter rvAdapter;
    private int size;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference root;
    private DatabaseReference userAssignmentBranch;

    private OnFragmentInteractionListener mListener;


    public AssignmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AssignmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AssignmentFragment newInstance(String param1, String param2) {
        AssignmentFragment fragment = new AssignmentFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_assignment,container,false);
        ButterKnife.bind(this, v);
        firebaseAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();
        if(user!=null) {
            userAssignmentBranch = root.child("users").child(user.getUid()).child("Assignments");
            Query query = userAssignmentBranch.orderByKey();
            userAssignmentBranch.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    size = (int)dataSnapshot.getChildrenCount();
                    Log.d(TAG,"Size of Assignments: "+size);
                    if(size != 0){
                        naIcon.setVisibility(View.GONE);
                        noAssignments.setVisibility(View.GONE);
                    }
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
            assignments = new ArrayList<>();
            getFirebaseAssignments(userAssignmentBranch);
        // Inflate the layout for this fragment
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Refresh();
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
            Log.d(TAG, "Attach succeeded");
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

    public void getFirebaseAssignments(DatabaseReference assignmentBranch){
        if(firebaseAuth.getCurrentUser()!=null) {
            assignmentBranch.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    saveAssignment(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError firebaseError) {
                    Log.d(TAG, firebaseError.getMessage());
                }
            });
        }
    }

    public void saveAssignment(DataSnapshot snapshot){
        if(android.os.Debug.isDebuggerConnected()){
            android.os.Debug.waitForDebugger();
        }
        for(DataSnapshot child : snapshot.getChildren()){
            try {
                String name = child.child("AssignmentName").getValue(String.class);
                String course = child.child("CourseName").getValue(String.class);
                String description = (String) child.child("Description").getValue(String.class);
                Assignment assignment = new Assignment(name,description,"100");
                boolean duplicate = false;
                for(int i = 0;i<assignments.size();i++){
                    if(assignments.get(i).equals(assignment)){
                        duplicate = true;
                    }
                }
                if(!duplicate){
                    assignments.add(assignment);
                }
            }
            catch(NullPointerException ex){
                Log.d(TAG,ex.getMessage());
            }
            catch (Exception e){
                //TODO: Report the error and send
                Log.d(TAG,e.getMessage());
                firebaseAuth.signOut();

            }


        }
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST);
        recycler.addItemDecoration(itemDecoration);
        rvAdapter = new AssignmentListAdapter(assignments);
        Log.d(TAG, "Assignment size: " + assignments.size());
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