package simplify.fwm.collegepa;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseSession;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;


    @Bind(R.id.login_email) AppCompatEditText email;
    @Bind(R.id.login_password) AppCompatEditText password;
    @Bind(R.id.login_button) AppCompatButton loginButton;
    @Bind(R.id.login_signup) TextView signUp;
    @Bind(R.id.login_post_email) TextView emailVerifyPrompt;
    @Bind(R.id.login_forgot_password)TextView forgotPassword;

    private String postSignEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try {
            ButterKnife.bind(this);
            emailVerifyPrompt.setVisibility(View.GONE);

        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            getSupportActionBar().hide();
        }
        catch (NullPointerException ex){
            ex.printStackTrace();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateForgot()) {
                    forgotPassword();
                }
            }
        });
    }

    public void login(){

        if(!validate()){
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(true);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait......");
        progressDialog.show();

        String emailCurrent = email.getText().toString();
        String passwordCurrent = password.getText().toString();
        ParseUser.logInInBackground(emailCurrent, passwordCurrent, new LogInCallback() {
            @Override
            public void done(final ParseUser user, ParseException e) {
                //Login Succeeded
                if (user != null) {
                    Log.d(TAG, "Log in succeeded");
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            onLoginSuccess();

                        }
                    }, 3000);
                }
                //Login Failed
                else {
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onLoginFailed();
                            progressDialog.dismiss();
                            Toast.makeText(getBaseContext(), "Login Failed", Toast.LENGTH_LONG).show();
                        }
                    }, 2000);

                }
            }
        });

    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_SIGNUP){
            if(resultCode==RESULT_OK){
                Log.d(TAG, "onActivityResult");
                emailVerifyPrompt.setVisibility(View.VISIBLE);

            }
        }
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void onLoginSuccess(){
        loginButton.setEnabled(true);
        setResult(Activity.RESULT_OK, null);
        finish();
    }

    public void onLoginFailed(){
        Toast.makeText(getBaseContext(), "Login Incorrect",Toast.LENGTH_LONG).show();

        loginButton.setEnabled(true);
    }

    public boolean validate(){
        boolean valid = true;

        String currentEmail = email.getText().toString();
        String currentPassword = password.getText().toString();

        if(currentEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(currentEmail).matches()){
            email.setError("Enter a Valid Email");
            valid = false;
        }
        else{
            email.setError(null);
        }

        if(currentPassword.isEmpty() || currentPassword.length() < 6 || currentPassword.length() > 12){
            password.setError("Please Enter Password between 6 and 10 alphanumeric characters");
            valid = false;
        }
        else{
            password.setError(null);
        }
        return valid;
    }

    public boolean validateForgot(){
        boolean valid = true;

        String currentEmail = email.getText().toString();

        if(currentEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(currentEmail).matches()){
            email.setError("Please Enter email that has Account");
            valid = false;
            Toast.makeText(getBaseContext(),"Please Enter Valid Email",Toast.LENGTH_LONG).show();
        }
        else{
            email.setError(null);
        }

        return valid;
    }

    public void forgotPassword(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait......");
        progressDialog.show();

        String currentEmail = email.getText().toString();
        if(validateForgot()){
            ParseUser.requestPasswordResetInBackground(currentEmail, new RequestPasswordResetCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null){
                        Toast.makeText(getBaseContext(),"Check Email to Reset",Toast.LENGTH_LONG).show();
                        new android.os.Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();

                            }
                        }, 2000);
                        if(ParseUser.getCurrentUser()!=null){
                            ParseUser.logOut();
                        }

                    }
                    else{
                        Toast.makeText(getBaseContext(),"Email Not found, Please sign up",Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                        new android.os.Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();

                            }
                        }, 2000);
                    }
                }
            });
        }
    }
}

