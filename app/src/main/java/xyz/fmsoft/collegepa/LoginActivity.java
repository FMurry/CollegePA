package xyz.fmsoft.collegepa;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.internal.FacebookDialogFragment;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.plus.Account;
import com.google.android.gms.plus.Plus;


import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
    @Bind(R.id.facebook_login)LoginButton _facebookSignup;
    @Bind(R.id.login_forgot_password)TextView forgotPassword;
    @Bind(R.id.login_linearlayout)LinearLayout _linearlayout;

    private GoogleApiClient mGoogleApiClient;
    private String OAuthtoken;
    private GoogleSignInAccount account;
    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        OAuthtoken = null;
        ButterKnife.bind(this);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }

        AppEventsLogger.activateApp(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();

        _googleSignUp.setSize(SignInButton.SIZE_WIDE);
        _googleSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleLogin();
            }
        });

        // Set up facebook button
        _facebookSignup.setReadPermissions("email");
        _facebookSignup.setFragment(new FacebookDialogFragment());
        callbackManager = CallbackManager.Factory.create();

        _facebookSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookLogin();
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
     * Login to app using Google Account
     */
    public void googleLogin(){
        Log.d(TAG, "G button Pressed");
        //TODO: Ask Permission before opening account fragment
        if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.GET_ACCOUNTS)){
                Toast.makeText(LoginActivity.this, "No Permission", Toast.LENGTH_SHORT).show();
            }
            else{
                // No explanation needed, we can request the permission.
                Log.d(TAG, "Requesting Permission");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.GET_ACCOUNTS}, REQUEST_PERMISSION_GET_ACCOUNT);
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, REQUEST_GOOGLE_SIGNIN);
            }

        }
        else {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, REQUEST_GOOGLE_SIGNIN);
        }
    }


    /**
     * If the google login was successful obtain the OAuth 2.0 Key
     * @param googleSignInResult the result of user signing in
     */
    private void googleLoginHelper(GoogleSignInResult googleSignInResult)  {

        if(googleSignInResult.isSuccess()){
            Log.d(TAG,"Google Login Successful");
            account = googleSignInResult.getSignInAccount();
            getGoogleOAuthTokenAndLogin();

        }
        else{
            int errorCode = googleSignInResult.getStatus().getStatusCode();
            Log.d(TAG,"Google Error Code "+errorCode);
            Snackbar snackbar = Snackbar.make(_linearlayout,"Sign in Failed with code "+errorCode,Snackbar.LENGTH_LONG);
        }
    }

    /**
     * Login to app using Facebook
     */
    public void facebookLogin(){

        _facebookSignup.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, error.getMessage());
            }
        });
    }

    /**
     * Login to app using email and password
     */
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
     * Finish process of logging in with google account replicate of login() method
     */
    public void LoginWithGoogle(){
        Log.d(TAG,"Logging in with Google Account");
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait......");
        progressDialog.show();

        final String name = account.getDisplayName();
        final String email = account.getEmail();
        final Uri profilePic = account.getPhotoUrl();
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                root.authWithOAuthToken("google", OAuthtoken, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {

                        root.child("users").child(authData.getUid()).child("provider").setValue(authData.getProvider());
                        root.child("users").child(authData.getUid()).child("device").setValue(Build.MODEL);
                        root.child("users").child(authData.getUid()).child("OSversion").setValue(Build.VERSION.SDK_INT);
                        root.child("users").child(authData.getUid()).child("name").setValue(name);
                        root.child("users").child(authData.getUid()).child("email").setValue(email);
                        root.child("users").child(authData.getUid()).child("photo").setValue(profilePic.toString());
                        progressDialog.dismiss();
                        onLoginSuccess();
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Log.d(TAG,firebaseError.getDetails());
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
        },2000);

    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode the request code
     * @param resultCode the result code
     * @param data intent containing data
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

    /**
     * Called when Login is Successful allows user to enter DrawerActivity
     */
    public void onLoginSuccess(){
        loginButton.setEnabled(true);
        setResult(Activity.RESULT_OK, null);
        startActivity(new Intent(this, DrawerActivity.class));
        finish();
    }

    /**
     * User Failed to Login Do nothing
     */
    public void onLoginFailed(){
        loginButton.setEnabled(true);
    }

    /**
     * Validate that login information that user entered is allowed
     * @return True if validated false if not
     */
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

        return valid;
    }

    /**
     * User Forgot their password validate that they entered an email prior to clicking Forgot your password
     * @return True if validated False if not
     */
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

    /**
     * User Forgot their password reset it
     */
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
                            if(firebaseError.getCode() == FirebaseError.INVALID_EMAIL){
                                Toast.makeText(LoginActivity.this, "User not found with email", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getBaseContext(), firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
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

    /**
     * Gets the OAuth Token for account and sets it
     */
    public void getGoogleOAuthTokenAndLogin(){
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                if(android.os.Debug.isDebuggerConnected()){
                    android.os.Debug.waitForDebugger();
                }
                String token = null;
                try{
                    String scope = String.format("oauth2:%s", Scopes.PLUS_LOGIN);
                    token = GoogleAuthUtil.getToken(getApplicationContext(), Plus.AccountApi.getAccountName(mGoogleApiClient),scope);
                }catch (UserRecoverableAuthException e){
                    Log.d(TAG,"UserRecoverableAuthException :"+e.getMessage());
                    if(mGoogleApiClient.hasConnectedApi(Plus.API)) {
                        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                        startActivityForResult(signInIntent, REQUEST_GOOGLE_SIGNIN);
                    }
                    else{
                        mGoogleApiClient.connect();
                        getGoogleOAuthTokenAndLogin();
                    }
                } catch (GoogleAuthException e) {
                    //TODO Request Permission
                    Log.d(TAG,"GoogleAuthException: "+e.getMessage());

                }
                catch (IOException e) {
                    Log.d(TAG,"IOException: "+e.getMessage());
                }
                return token;
            }
            @Override
            protected void onPostExecute(String token) {
                if (token != null) {
                    setOAuthToken(token);
                    LoginWithGoogle();
                }
            }
        };
        task.execute();
        }

    /**
     * Mutator for OAuthtoken
     * @param token the token to set
     */
    public void setOAuthToken(String token) {
        OAuthtoken = token;
    }
}

