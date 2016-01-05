package simplify.fwm.collegepa;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import simplify.fwm.collegepa.R;

import static android.Manifest.permission.READ_CONTACTS;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class SignupActivity extends AppCompatActivity  {

    private static final String TAG = "SignupActivity";

    private ParseUser user;
    @Bind(R.id.signup_firstname)AppCompatEditText firstName;
    @Bind(R.id.signup_lastname)AppCompatEditText lastName;
    @Bind(R.id.signup_email)AppCompatEditText email;
    @Bind(R.id.signup_password)AppCompatEditText password;
    @Bind(R.id.signup_button)AppCompatButton signupButton;
    @Bind(R.id.signup_login) TextView loginLink;
    private String emailInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        user = new ParseUser();
        try {
            ButterKnife.bind(this);
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            getSupportActionBar().hide();
        }
        catch (NullPointerException ex){
            ex.printStackTrace();
        }
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void signUp(){

        if(!validate()){
            onSignupFailed();
            return;
        }
        signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Signing Up");
        progressDialog.show();

        String firstNameInput = firstName.getText().toString();
        String lastNameInput = lastName.getText().toString();
        emailInput = email.getText().toString();
        String passwordInput = password.getText().toString();

        //TODO: Implement Signup logic here
        user.setUsername(emailInput);
        user.setEmail(emailInput);
        user.setPassword(passwordInput);
        user.put("firstName", firstNameInput);
        user.put("lastName", lastNameInput);
        ParseUser.logOut();
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onSignupSuccess();
                            progressDialog.dismiss();
                            Toast.makeText(getBaseContext(),"Sign Up Successful",Toast.LENGTH_LONG).show();
                        }
                    }, 2000);
                }
                else {
                    e.printStackTrace();
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ParseUser.logOut();
                            onSignupFailed();
                            progressDialog.dismiss();
                            email.setError("Email already has account");
                        }
                    }, 2000);
                }
            }
        });

        //TODO:-----------------------------


    }

    public void onSignupSuccess(){
        signupButton.setEnabled(true);
        ParseUser.logOut();
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed(){
        Log.d(TAG,"Sign Up Failed");
        signupButton.setEnabled(true);

    }

    public boolean validate(){
        boolean valid = true;

        String firstNameInput = firstName.getText().toString();
        String lastNameInput = lastName.getText().toString();
        String emailInput = email.getText().toString();
        String passwordInput = password.getText().toString();

        if(firstNameInput.isEmpty() || firstNameInput.length()<3){
            firstName.setError("Enter at least 3 characters");
            valid = false;
        }
        else{
            firstName.setError(null);
        }

        if(lastNameInput.isEmpty() || lastNameInput.length()<1){
            lastName.setError("Enter at least 1 character");
            valid = false;
        }
        else{
            lastName.setError(null);
        }

        if(emailInput.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            email.setError("Enter valid email");
            valid = false;
        }
        else {
            email.setError(null);
        }

        if(passwordInput.isEmpty() || password.length() < 6 || password.length()> 12){
            password.setError("Between 6 and 12 alphanumeric characters");
            valid = false;
        }
        else{
            password.setError(null);
        }
        return valid;
    }
}
