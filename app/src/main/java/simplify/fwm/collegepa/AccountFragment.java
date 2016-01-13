package simplify.fwm.collegepa;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;


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
    @Bind(R.id.warning_account) TextView warning;


    // TODO: Rename and change types of parameters

    private OnFragmentInteractionListener mListener;

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
        ButterKnife.bind(this,v);

        ParseUser current = ParseUser.getCurrentUser();
        if(current != null) {
            if (current.getBoolean("emailVerified")) {
                warning.setVisibility(View.INVISIBLE);

            } else {
                warning.setVisibility(View.VISIBLE);
            }
        }

        if(current!=null){
            welcome.setText("Welcome "+current.get("firstName")+" "+current.get("lastName")+"!");
            displayEmail.setText(current.getEmail());
        }
        else{
            welcome.setText("Are you Logged in?");
        }


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
}

//TODO: If user email is not verified then remove some features
