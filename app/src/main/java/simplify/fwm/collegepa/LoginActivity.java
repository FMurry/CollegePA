package simplify.fwm.collegepa;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import simplify.fwm.collegepa.utils.Constants;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private Firebase root = new Firebase(Constants.FIREBASE_ROOT_URL);


    @Bind(R.id.login_email) AppCompatEditText email;
    @Bind(R.id.login_password) AppCompatEditText password;
    @Bind(R.id.login_button) AppCompatButton loginButton;
    @Bind(R.id.login_signup) TextView signUp;
    @Bind(R.id.login_post_email) TextView emailVerifyPrompt;
    @Bind(R.id.login_forgot_password)TextView forgotPassword;
    @Bind(R.id.login_linearlayout)LinearLayout _linearlayout;

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
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
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
                forgotPassword();
            }
        });
    }

    public void login(){

        if(!validate()){
            onLoginFailed();
            return;
        }

        if(!isConnected()){
           onLoginFailed();
            Snackbar snackbar = Snackbar.make(_linearlayout,"No connection",Snackbar.LENGTH_INDEFINITE).setAction("Retry",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            login();
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            View snackView = snackbar.getView();
            TextView text = (TextView)snackView.findViewById(android.support.design.R.id.snackbar_text);
            text.setTextColor(Color.YELLOW);
            snackbar.show();
            return;
        }

        loginButton.setEnabled(true);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait......");
        progressDialog.show();

        final String emailCurrent = email.getText().toString();
        final String passwordCurrent = password.getText().toString();
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //FIREBASE LOGIN
                root.authWithPassword(emailCurrent, passwordCurrent, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Log.d(TAG, "Log in succeeded");


                        root.child("users").child(authData.getUid()).child("provider").setValue(authData.getProvider());
                        root.child("users").child(authData.getUid()).child("device").setValue(Build.MODEL);
                        root.child("users").child(authData.getUid()).child("OSversion").setValue(Build.VERSION.SDK_INT);

                        progressDialog.dismiss();
                        onLoginSuccess();
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        if(firebaseError.getCode() == FirebaseError.DISCONNECTED){
                            progressDialog.dismiss();
                            Toast.makeText(getBaseContext(), "No Connection", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            onLoginFailed();
                            progressDialog.dismiss();
                            Toast.makeText(getBaseContext(), "Login Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        }, 3000);


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

        if(currentPassword.isEmpty() || currentPassword.length() < 6 || currentPassword.length() > 20){
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

        final String currentEmail = email.getText().toString();
        if(validateForgot()){
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    root.resetPassword(currentEmail, new Firebase.ResultHandler() {
                        @Override
                        public void onSuccess() {
                            progressDialog.dismiss();
                            Toast.makeText(getBaseContext(), "Check Email for Password Reset", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(FirebaseError firebaseError) {
                            progressDialog.dismiss();
                            Toast.makeText(getBaseContext(),firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }, 2500);

        }
        else{
            Toast.makeText(this,"Please Enter Email",Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    }

    /**
     * Determines whether android device has an internet connection
     * @return
     */
    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean connected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return connected;
    }
}

