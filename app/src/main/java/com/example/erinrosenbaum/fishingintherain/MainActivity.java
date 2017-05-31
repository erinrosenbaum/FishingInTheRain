package com.example.erinrosenbaum.fishingintherain;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.Gravity;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;

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


public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    public Button goToMaps;


    private static final String TAG = "SIGNIN_EXERCISE";
    private static final int RES_CODE_SIGN_IN = 1001;

    private GoogleApiClient mGoogleApiClient;

    private TextView m_tvStatus;
    private TextView m_tvDispName;
    private TextView m_tvEmail;

    private void startSignIn() {
        // Create sign-in intent and begin auth flow with APIClient that was created
        // Use code to indicate we want sign-in process for use with activity handler
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RES_CODE_SIGN_IN);
    }

    private void signOut() {
        // Sign the user out and update the UI
        // account remains associated with the app
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                // <Status> is a Java generic type
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        m_tvStatus.setText(R.string.status_notsignedin);
                        // set email and dispaly name to empty strings
                        m_tvEmail.setText("");
                        m_tvDispName.setText("");
                        myGlobalVars.signedIn = -1;
                    }
                });
    }

    private void disconnect() {
        // Disconnect this account completely and update UI
        // Asynchronous, takes result call-back object
        // Use callbacks to update UI
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        m_tvStatus.setText(R.string.status_notconnected);
                        // set email and dispaly name to empty strings
                        m_tvEmail.setText("");
                        m_tvDispName.setText("");
                        myGlobalVars.signedIn = -1;
                    }
                });
    }

    private void goToTheMapActivity() {
        Intent go = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(go);
    }

    private void goToMyPlaces() {
        Intent go = new Intent(MainActivity.this, MyPlaces.class);
        startActivity(go);
    }

    // checks to see if sign-in was successful
    public void signInResultHandler(GoogleSignInResult result) {
        if (result.isSuccess()) {
            Status status = result.getStatus();
            int statusCode = status.getStatusCode();
            Log.i("Code", Integer.toString(statusCode));
            myGlobalVars.signedIn = 226;

            GoogleSignInAccount acct = result.getSignInAccount();
            m_tvStatus.setText(R.string.status_signedin);
            try {
                // sets user info into text views in layout
                m_tvDispName.setText(acct.getDisplayName());
                m_tvEmail.setText(acct.getEmail());

            }
            catch (NullPointerException e) {
                Log.d(TAG, "Error retrieving some account information");
            }
        }
        else {
            Status status = result.getStatus();
            int statusCode = status.getStatusCode();
            if (statusCode == GoogleSignInStatusCodes.SIGN_IN_CANCELLED) {
                m_tvStatus.setText(R.string.status_signincancelled);
                myGlobalVars.signedIn = -1;
            }
            else if (statusCode == GoogleSignInStatusCodes.SIGN_IN_FAILED) {
                m_tvStatus.setText(R.string.status_signinfail);
                myGlobalVars.signedIn = -1;
            }
            else {
                m_tvStatus.setText(R.string.status_nullresult);
                myGlobalVars.signedIn = -1;
            }
        }
    }

    // *************************************************
    // -------- ANDROID ACTIVITY LIFECYCLE METHODS
    // *************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_tvStatus = (TextView)findViewById(R.id.tvStatus);
        m_tvDispName = (TextView)findViewById(R.id.tvDispName);
        m_tvEmail = (TextView)findViewById(R.id.tvEmail);

        findViewById(R.id.btnSignIn).setOnClickListener(this);
        findViewById(R.id.btnSignOut).setOnClickListener(this);
        findViewById(R.id.btnDisconnect).setOnClickListener(this);
        findViewById(R.id.goToMaps).setOnClickListener(this);
        findViewById(R.id.goToPlaces).setOnClickListener(this);




        // Create the sign-in object
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                //.requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .build();

        // Sign-in object is passed to the Google API client
        // Build the GoogleApiClient object
        // allows usage of sign-in API
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Customize the sign in button
        SignInButton signInButton = (SignInButton) findViewById(R.id.btnSignIn);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setColorScheme(SignInButton.COLOR_DARK);
        //signInButton.setScopes(gso.getScopeArray());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    // This is called when the user selects and account
    // extract code and pass to sign-in handler
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == RES_CODE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            signInResultHandler(result);
        }
    }

    // *************************************************
    // -------- GOOGLE PLAY SERVICES METHODS
    // *************************************************
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Could not connect to Google Play Services");
    }

    // *************************************************
    // -------- CLICK LISTENER FOR THE ACTIVITY
    // *************************************************
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignIn:
                startSignIn();
                break;
            case R.id.btnSignOut:
                signOut();
                break;
            case R.id.btnDisconnect:
                disconnect();
                break;
            case R.id.goToMaps:
                if (myGlobalVars.signedIn == 226){
                    goToTheMapActivity();
                }
                else {
                    Toast toast = Toast.makeText(this, "Must sign-in to access this feature", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                break;
            case R.id.goToPlaces:
                if (myGlobalVars.signedIn == 226){
                    goToMyPlaces();
                }
                else {
                    Toast toast = Toast.makeText(this, "Must sign-in to access this feature", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                break;


        }
    }

}


