package xyz.fmsoft.collegepa;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.List;

import xyz.fmsoft.collegepa.Course.Assignment;
import xyz.fmsoft.collegepa.Course.Course;


public class AssignmentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters
    private static final String TAG = "AssignmentFragment";

    private OnFragmentInteractionListener mListener;

    private ImageView naIcon;
    private TextView noAssignments;

    private List<Assignment> assignments;
    private RecyclerView.LayoutManager rvLayout;
    private RecyclerView.Adapter rvAdapter;
    private int size;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference root;
    private DatabaseReference userAssignmentBranch;

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
        naIcon =(ImageView) v.findViewById(R.id.na_icon);
        noAssignments =(TextView)v.findViewById(R.id.tv_no_assignments);
        firebaseAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();
        if(user!=null) {
            userAssignmentBranch = root.child("users").child(user.getUid()).child("Courses");
            Query query = userAssignmentBranch.orderByKey();
            userAssignmentBranch.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    size = (int)dataSnapshot.getChildrenCount();
                }

                @Override
                public void onCancelled(DatabaseError firebaseError) {

                }
            });
            if(size != 0){
                naIcon.setVisibility(View.GONE);
                noAssignments.setVisibility(View.GONE);
            }
        }
        // Inflate the layout for this fragment
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


}