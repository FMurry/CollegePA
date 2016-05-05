package xyz.fmsoft.collegepa;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.drive.Drive;


import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.fmsoft.collegepa.utils.Constants;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static final int REQUEST_GOOGLE_SIGNIN = 1;
    private static final int REQUEST_PERMISSION_GET_ACCOUNT = 101;
    private static String ACCOUNT_PERMISSIONS[] = new String[]{
            Manifest.permission.GET_ACCOUNTS
    };
    private Firebase root = new Firebase(Constants.FIREBASE_ROOT_URL);


    @Bind(R.id.login_email) AppCompatEditText email;
    @Bind(R.id.login_password) AppCompatEditText password;
    @Bind(R.id.login_button) AppCompatButton loginButton;
    @Bind(R.id.login_signup) TextView signUp;
    @Bind(R.id.google_signin)SignInButton _googleSignUp;
    @Bind(R.id.login_forgot_password)TextView forgotPassword;
    @Bind(R.id.login_linearlayout)LinearLayout _linearlayout;

    private String postSignEmail;
    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Constants.GOOGLE_OAUTH_KEY)
                .requestEmail()
                .build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // Use Google Standard WIDE sign in button
        _googleSignUp.setSize(SignInButton.SIZE_WIDE);
        _googleSignUp.setScopes(gso.getScopeArray());
        _googleSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleLogin();
            }
        });

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

    /**
     * Handles logging in with google account
     */
    public void googleLogin(){

            Intent googleAccountIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(googleAccountIntent, REQUEST_GOOGLE_SIGNIN);
    }



    private void googleLoginHelper(GoogleSignInResult googleSignInResult){

        if(googleSignInResult.isSuccess()){
            Log.d(TAG,"Google Login Successful");
            GoogleSignInAccount account = googleSignInResult.getSignInAccount();
            final String email = account.getEmail();
            final String name = account.getDisplayName();
            final String personID = account.getId();
            final Uri profilePhoto = account.getPhotoUrl();
            root.authWithOAuthToken("google", Constants.GOOGLE_OAUTH_KEY, new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    Log.d(TAG, "Log in succeeded");
                    root.child("users").child(authData.getUid()).child("provider").setValue(authData.getProvider());
                    root.child("users").child(authData.getUid()).child("device").setValue(Build.MODEL);
                    root.child("users").child(authData.getUid()).child("OSversion").setValue(Build.VERSION.SDK_INT);
                    root.child("users").child(authData.getUid()).child("email").setValue(email);
                    root.child("users").child(authData.getUid()).child("personID").setValue(personID);
                    root.child("users").child(authData.getUid()).child("profilePhoto").setValue(profilePhoto);
                    root.child("users").child(authData.getUid()).child("name").setValue(name);

                    onLoginSuccess();


                }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {

                }
            });

        }
        else{
            int errorCode = googleSignInResult.getStatus().getStatusCode();
            Log.d(TAG,"Google Error Code "+errorCode);
            Snackbar snackbar = Snackbar.make(_linearlayout,"Sign in Failed with code "+errorCode,Snackbar.LENGTH_LONG);
        }
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
                            Toast.makeText(getBaseContext(), "Login Incorrect", Toast.LENGTH_LONG).show();
                            Log.d(TAG,"Failure message: "+firebaseError.getDetails()+" "+firebaseError.getMessage());
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
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_SIGNUP){
                Log.d(TAG, "onActivityResult");
        }
        else if(requestCode == REQUEST_GOOGLE_SIGNIN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            googleLoginHelper(result);
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
        startActivity(new Intent(this, DrawerActivity.class));
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(LoginActivity.this, "Failed: "+connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }
}

