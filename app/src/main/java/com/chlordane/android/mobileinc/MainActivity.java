package com.chlordane.android.mobileinc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final String EXTRA_MESSAGE = "com.chlordane.android.mobileinc.extra.MESSAGE";
    public static final int TEXT_REQUEST = 1;
    private ViewPager mViewPager;

    private static final String TAG = "MainActivity";
    private static final String ACCOUNT_SUCCESS_TAG = "FirebaseAccountAdd";
    private static final String ACCOUNT_ERROR_TAG = "FirebaseAccountError";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;

    private RelativeLayout firstActivity;
    private CoordinatorLayout appBarMainActivity;
    private RelativeLayout contentMainActivity;

    private NavigationView navigationView;
    private DrawerLayout mDrawer;

    private String playerName;
    private TextView mPlayerNameTextView;

    private SharedPreferences mPreferences;
    private static final String mSharedPrefFile = "com.chlordane.android.mobileinc";
    private SharedPreferences.Editor editor;

    private final String MI5COUNT_KEY = "mi5_count";
    private final String MIMAXCOUNT_KEY = "mimax_count";
    private final String REDMICOUNT_KEY = "redmi_count";
    private final String GALAXYNOTE8COUNT_KEY = "galaxynote8_count";
    private final String GALAXYNOTE5COUNT_KEY = "galaxynote5_count";
    private final String GALAXYS8COUNT_KEY = "galaxys8_count";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeMain);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPreferences = getSharedPreferences(mSharedPrefFile, Context.MODE_PRIVATE);
        editor = mPreferences.edit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shopIntent = new Intent(getApplicationContext(), ReviewCartActivity.class);
                startActivityForResult(shopIntent, TEXT_REQUEST);
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("All"));
        tabLayout.addTab(tabLayout.newTab().setText("Samsung"));
        tabLayout.addTab(tabLayout.newTab().setText("Xiaomi"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        final android.support.v4.view.PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // Initialize Activities
        firstActivity = (RelativeLayout) findViewById(R.id.activity_first);
        appBarMainActivity = (CoordinatorLayout) findViewById(R.id.activity_app_bar_main);
        contentMainActivity = (RelativeLayout) findViewById(R.id.activity_content_main);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();

        // Just to Log SharefPreferences Key-Values
        Map<String,?> keys = PreferenceManager.getDefaultSharedPreferences(this).getAll();
        for(Map.Entry<String,?> entry : keys.entrySet()){
            Log.d("SharedPrefs",entry.getKey() + ": " +
                    entry.getValue().toString());
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideProgressDialog();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingIntent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivityForResult(settingIntent, TEXT_REQUEST);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_setting) {
            Intent shopIntent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivityForResult(shopIntent, TEXT_REQUEST);
        } else if (id == R.id.nav_about) {
            Intent aboutIntent = new Intent(getApplicationContext(), AboutActivity.class);
            startActivityForResult(aboutIntent, TEXT_REQUEST);
        } else if (id == R.id.nav_qr_scan) {
            Intent aboutIntent = new Intent(getApplicationContext(), QRScanActivity.class);
            startActivityForResult(aboutIntent, TEXT_REQUEST);
        } else if (id == R.id.nav_sign_out) {
            editor.putInt(MI5COUNT_KEY,0);
            editor.apply();
            editor.putInt(MIMAXCOUNT_KEY,0);
            editor.apply();
            editor.putInt(REDMICOUNT_KEY,0);
            editor.apply();
            editor.putInt(GALAXYNOTE8COUNT_KEY,0);
            editor.apply();
            editor.putInt(GALAXYNOTE5COUNT_KEY,0);
            editor.apply();
            editor.putInt(GALAXYS8COUNT_KEY,0);
            editor.apply();
            signOut();
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void login(View view) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                }
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully
            GoogleSignInAccount acct = result.getSignInAccount();
            String playerName = acct.getDisplayName();
            View v = navigationView.getHeaderView(0);
            TextView nameTextView = (TextView) v.findViewById(R.id.playerName);
            nameTextView.setText(playerName);

            firebaseAuthWithGoogle(acct);
            sendNameAndTokenToServer(playerName);
        } else {
            int errorCode = result.getStatus().getStatusCode();
            Log.d(TAG, "errorCode = " + Integer.toString(errorCode));

            if (errorCode == GoogleSignInStatusCodes.NETWORK_ERROR) {
                // Handle network error
                Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();

        if (user != null) {
            firstActivity.setVisibility(View.GONE);
            contentMainActivity.setVisibility(View.VISIBLE);
            appBarMainActivity.setVisibility(View.VISIBLE);

            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } else {
            firstActivity.setVisibility(View.VISIBLE);
            contentMainActivity.setVisibility(View.GONE);
            appBarMainActivity.setVisibility(View.GONE);

            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private void sendNameAndTokenToServer(final String name) {
        final String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Name: " + name + ", Token: " + token);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://mobileinc.herokuapp.com/api/manage/user";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(ACCOUNT_SUCCESS_TAG, "Success, Server Response : " + response);
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(ACCOUNT_ERROR_TAG, "Error : " + error.toString());
            }
        })
        {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String,String>();
                params.put("name", name);
                params.put("firebase_key", token);

                return params;
            }
        };

        requestQueue.add(postRequest);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }
}
