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


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference user;
    private String email;


    public ChangePasswordDialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_change_password_dialog,container,false);
        ButterKnife.bind(this, v);
        user = root.child(firebaseAuth.getCurrentUser().getUid());
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
            public void onCancelled(DatabaseError databaseError) {

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

        //TODO: Change password

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
