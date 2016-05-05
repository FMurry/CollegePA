package xyz.fmsoft.collegepa;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.fmsoft.collegepa.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordDialog extends DialogFragment {


    @Bind(R.id.changepassword_ok)AppCompatButton _ok;
    @Bind(R.id.changepassword_cancel)AppCompatButton _cancel;
    @Bind(R.id.changepassword_oldpassword)EditText _oldPassword;
    @Bind(R.id.changepassword_password)EditText _password;
    @Bind(R.id.changepassword_password2)EditText _password2;

    private Firebase root = new Firebase(Constants.FIREBASE_ROOT_URL);
    private Firebase user = root.child("users").child(root.getAuth().getUid());
    private String email;


    public ChangePasswordDialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_change_password_dialog,container,false);
        ButterKnife.bind(this, v);
        email = "";

        _ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
        _cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

        user.child("email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setEmail(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        return v;
    }

    public  boolean validate(){
        boolean isValid = true;
        String password = _password.getText().toString();
        String password2 = _password2.getText().toString();

        if(password.length() < 6 || password.length() > 20){
            isValid = false;
            _password.setError("Between 6 and 20 alphanumeric characters");
        }
        else {
            _password.setError(null);
        }

        if(!(password2.equals(password))){
            isValid = false;
            _password2.setError("Passwords don't match");
        }
        else{
            _password2.setError(null);
        }

        return isValid;
    }
    public void changePassword(){
        if(!validate()){
            return;
        }

        String mail = getEmail();
        String oldPassword = _oldPassword.getText().toString();
        String password = _password.getText().toString();
        String password2 = _password2.getText().toString();

        root.changePassword(mail, oldPassword, password, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "Password Changed", Toast.LENGTH_SHORT).show();

                close();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                Toast.makeText(getContext(), firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setEmail(String email){
        this.email = email;

    }
    public String getEmail(){
        return email;
    }

    public void close(){
        dismiss();
    }
}
