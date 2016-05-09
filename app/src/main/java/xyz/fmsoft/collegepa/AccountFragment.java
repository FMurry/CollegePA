package xyz.fmsoft.collegepa;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.fmsoft.collegepa.utils.Constants;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *  AccountFragment.OnFragmentInteractionListener interface
 * to handle interaction events.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    @Bind(R.id.account_pic)ImageView accountIcon;
    @Bind(R.id.account_log_out)TextView logOut;
    @Bind(R.id.account_welcome)TextView welcome;
    @Bind(R.id.account_email) TextView displayEmail;
    @Bind(R.id.account_canvas) ImageView _canvas;
    @Bind(R.id.account_change_password)TextView _changePassword;


    // TODO: Rename and change types of parameters

    private OnFragmentInteractionListener mListener;
    private Firebase root = new Firebase(Constants.FIREBASE_ROOT_URL);
    private Firebase user;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * Method that is called when views are created this is where we can initialize views
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_account,container,false);
        ButterKnife.bind(this, v);
        if(root.getAuth() != null) {
            user = root.child("users").child(root.getAuth().getUid());
            user.child("email").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    displayEmail.setText(dataSnapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
        else{
            displayEmail.setText("Nothing??");
        }

        _canvas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvasLogin();
            }
        });
        _changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        return v;

    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void canvasLogin(){
        //TODO: Implement logging into canvas
        Snackbar.make(this.getView(),"Not implemented",Snackbar.LENGTH_LONG).show();
    }

    public void changePassword(){
        ChangePasswordDialog changePasswordDialog = new ChangePasswordDialog();
        changePasswordDialog.setCancelable(false);
        changePasswordDialog.show(getFragmentManager(), "Change Password");
    }
}

