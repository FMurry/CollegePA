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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.internal.FacebookDialogFragment;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


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
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    private ProgressDialog loginProgress;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference root;



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
    private AppCompatActivity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance().getReference();
        activity = this;
        setContentView(R.layout.activity_login);
        OAuthtoken = null;
        ButterKnife.bind(this);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }


        AppEventsLogger.activateApp(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.google_web_client_id))
                .build();
        loginProgress = new ProgressDialog(this);
        loginProgress.setMessage("Please wait.......");
        loginProgress.setIndeterminate(true);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        _googleSignUp.setSize(SignInButton.SIZE_WIDE);
        _googleSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleLogin();
            }
        });

        // Set up facebook button
        _facebookSignup.setReadPermissions("email","public_profile");
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
            //getGoogleOAuthTokenAndLogin();
            LoginWithGoogle();

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

            Log.d(TAG,"Facebook Login Entered");
            if(android.os.Debug.isDebuggerConnected()){
                android.os.Debug.waitForDebugger();
            }
            _facebookSignup.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d(TAG,"Facebook onSuccess");
                    AccessToken token = loginResult.getAccessToken();
                    Log.d(TAG,token.getToken());
                    AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
                    facebookLoginHelper(credential);

                }

                @Override
                public void onCancel() {
                    Log.d(TAG,"onCancel");
                }

                @Override
                public void onError(FacebookException error) {
                    Log.d(TAG, error.getMessage());
                }
            });


    }

    public void facebookLoginHelper(AuthCredential credential){
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG,"Logging in with Facebook Account");
                loginProgress.setMessage("Please wait......");
                loginProgress.show();
                loginProgress.setCancelable(false);
                if(task.isSuccessful()){
                    AuthResult authResult = task.getResult();
                    FirebaseUser user = authResult.getUser();
                    List<String> providers = user.getProviders();
                    StringBuilder provider = new StringBuilder("");
                    for(String str: providers){
                        provider.append(str);
                    }
                    String name = user.getDisplayName();
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    final String date = format.format(calendar.getTime());
                    root.child("users").child(user.getUid()).child("provider").setValue(provider.toString());
                    root.child("users").child(user.getUid()).child("device").setValue(Build.MODEL);
                    root.child("users").child(user.getUid()).child("OSversion").setValue(Build.VERSION.SDK_INT);
                    root.child("users").child(user.getUid()).child("name").setValue(name);
                    root.child("users").child(user.getUid()).child("email").setValue(email);
                    root.child("users").child(user.getUid()).child("loginDate").setValue(date);
                    onLoginSuccess();

                }
                else{
                    //Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, task.getException().getMessage());
                    LoginManager.getInstance().logOut();
                    onLoginFailed();
                }
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

        loginProgress.show();
        loginProgress.setCancelable(false);

        final String emailCurrent = email.getText().toString();
        final String passwordCurrent = password.getText().toString();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        final String date = format.format(calendar.getTime());
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //FIREBASE LOGIN
                firebaseAuth.signInWithEmailAndPassword(emailCurrent, passwordCurrent)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "Log in succeeded");

                            FirebaseUser fUser = firebaseAuth.getCurrentUser();
                            String uID = fUser.getUid();
                            StringBuilder str = new StringBuilder("");
                            for(String s : fUser.getProviders()) str.append(s);

                            root.child("users").child(uID).child("provider").setValue(str.toString());
                            root.child("users").child(uID).child("device").setValue(Build.MODEL);
                            root.child("users").child(uID).child("OSversion").setValue(Build.VERSION.SDK_INT);
                            root.child("users").child(uID).child("loginDate").setValue(date);

                            onLoginSuccess();
                        }

                        else{
                            Exception exception = task.getException();
                            Log.d(TAG,exception.getMessage());
                            Toast.makeText(LoginActivity.this, "Login Incorrect", Toast.LENGTH_LONG).show();
                            onLoginFailed();
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
        loginProgress.show();
        loginProgress.setCancelable(false);

        final AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);

        final String name = account.getDisplayName();
        final String email = account.getEmail();
        final Uri profilePic = account.getPhotoUrl();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        final String date = format.format(calendar.getTime());
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                firebaseAuth.signInWithCredential(credential)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    AuthResult authResult = task.getResult();
                                    FirebaseUser user = authResult.getUser();
                                    List<String> providers = user.getProviders();
                                    StringBuilder provider = new StringBuilder("");
                                    for(String str: providers){
                                        provider.append(str);
                                    }
                                    root.child("users").child(user.getUid()).child("provider").setValue(provider.toString());
                                    root.child("users").child(user.getUid()).child("device").setValue(Build.MODEL);
                                    root.child("users").child(user.getUid()).child("OSversion").setValue(Build.VERSION.SDK_INT);
                                    root.child("users").child(user.getUid()).child("name").setValue(name);
                                    root.child("users").child(user.getUid()).child("email").setValue(email);
                                    root.child("users").child(user.getUid()).child("loginDate").setValue(date);
                                    try{
                                        root.child("users").child(user.getUid()).child("photo").setValue(profilePic.toString());
                                    }catch (Exception e) {
                                        Log.d(TAG, e.getMessage());
                                        root.child("users").child(user.getUid()).child("photo").setValue("NA");
                                    }
                                    onLoginSuccess();
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    onLoginFailed();
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
        else{
            callbackManager.onActivityResult(requestCode,resultCode,data);
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
        loginProgress.dismiss();
        startActivity(new Intent(this, DrawerActivity.class));
        finish();
    }

    /**
     * User Failed to Login Do nothing
     */
    public void onLoginFailed()
    {
        loginProgress.dismiss();
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
        progressDialog.setCancelable(false);

        final String currentEmail = email.getText().toString();
        if(validateForgot()){
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.sendPasswordResetEmail(currentEmail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getBaseContext(), "Check Email for Password Reset", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        progressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "User not found with email", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
            }, 2500);

        }
        else{
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
        if(android.os.Debug.isDebuggerConnected()){
            android.os.Debug.waitForDebugger();
        }
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

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
        Log.d(TAG, "OAuth Token: "+OAuthtoken);
    }
}

