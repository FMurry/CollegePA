package xyz.fmsoft.collegepa;


import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.fmsoft.collegepa.utils.Constants;

//TODO Remove firstName LastName fields and firebase push Only Use Name
/**
 * A login screen that offers login via email/password.
 */
public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    private Firebase root = new Firebase(Constants.FIREBASE_ROOT_URL);
    @Bind(R.id.signup_firstname)
    AppCompatEditText firstName;
    @Bind(R.id.signup_lastname)
    AppCompatEditText lastName;
    @Bind(R.id.signup_email)
    AppCompatEditText email;
    @Bind(R.id.signup_password)
    AppCompatEditText password;
    @Bind(R.id.signup_button)
    AppCompatButton signupButton;
    @Bind(R.id.signup_login)
    TextView loginLink;
    private String emailInput;
    private boolean signupSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Firebase ref = new Firebase(Constants.FIREBASE_ROOT_URL);

        try {
            ButterKnife.bind(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            getSupportActionBar().hide();
        } catch (NullPointerException ex) {
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

    public void signUp() {

        if (!validate()) {
            onSignupFailed();
            return;
        }
        signupButton.setEnabled(false);



        final String firstNameInput = firstName.getText().toString();
        final String lastNameInput = lastName.getText().toString();
        emailInput = email.getText().toString();
        final String passwordInput = password.getText().toString();




        //Firebase Signup
        root.createUser(emailInput, passwordInput, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                onSignupSuccess();

                root.authWithPassword(emailInput, passwordInput, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Map<String, String> map = new HashMap<>();
                        map.put("provider", authData.getProvider());
                        map.put("email", emailInput);
                        map.put("firstName",firstNameInput);
                        map.put("lastName",lastNameInput);
                        map.put("device", Build.MODEL);
                        map.put("OSversion", String.valueOf(Build.VERSION.SDK_INT));
                        root.child("users").child(authData.getUid()).setValue(map);

                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Log.d(TAG, firebaseError.getMessage());
                    }
                });
            }

            @Override
            public void onError(final FirebaseError firebaseError) {
                final FirebaseError error = firebaseError;

                onSignupFailed();
                if (error.getCode() == FirebaseError.EMAIL_TAKEN) {
                    email.setError(error.getMessage());
                    Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, firebaseError.getMessage());
                }


            }
        });

                /*
                //Parse Signup
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {

                            onSignupSuccess();
                            progressDialog.dismiss();
                            Toast.makeText(getBaseContext(), "Sign Up Successful", Toast.LENGTH_LONG).show();

                        } else {
                            e.printStackTrace();

                            ParseUser.logOut();
                            onSignupFailed();
                            progressDialog.dismiss();
                            email.setError("Email already has account");

                        }
                    }
                });
                */


    }

    public void onSignupSuccess() {
        signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        root.unauth();
        finish();
    }

    public void onSignupFailed() {
        Log.d(TAG, "Sign Up Failed");
        signupButton.setEnabled(true);

    }

    public boolean validate() {
        boolean valid = true;

        String firstNameInput = firstName.getText().toString();
        String lastNameInput = lastName.getText().toString();
        String emailInput = email.getText().toString();
        String passwordInput = password.getText().toString();

        if (firstNameInput.isEmpty() || firstNameInput.length() < 3) {
            firstName.setError("Enter at least 3 characters");
            valid = false;
        } else {
            firstName.setError(null);
        }

        if (lastNameInput.isEmpty() || lastNameInput.length() < 1) {
            lastName.setError("Enter at least 1 character");
            valid = false;
        } else {
            lastName.setError(null);
        }

        if (emailInput.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError("Enter valid email");
            valid = false;
        } else {
            email.setError(null);
        }

        if (passwordInput.isEmpty() || password.length() < 6 || password.length() > 20) {
            password.setError("Between 6 and 20 alphanumeric characters");
            valid = false;
        } else {
            password.setError(null);
        }
        return valid;
    }
}
