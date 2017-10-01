package com.chlordane.android.mobileinc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final String EXTRA_MESSAGE = "com.chlordane.android.mobileinc.extra.MESSAGE";
    public static final String EXTRA_NAME = "com.chlordane.android.mobileinc.extra.NAME";
    public static final String EXTRA_LOCATION = "com.chlordane.android.mobileinc.extra.LOCATION";
    public static final int TEXT_REQUEST = 1;
    private ViewPager mViewPager;

    private static final String TAG = "MainActivity";
    private static final String ACCOUNT_SUCCESS_TAG = "FirebaseAccountAdd";
    private static final String ACCOUNT_ERROR_TAG = "FirebaseAccountError";
    private static final int RC_SIGN_IN = 9001;

    // Authentication
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;

    // Layouts
    private RelativeLayout firstActivity;
    private CoordinatorLayout appBarMainActivity;
    private RelativeLayout contentMainActivity;

    private NavigationView navigationView;
    private DrawerLayout mDrawer;

    private String playerName;

    private SharedPreferences mPreferences;
    private static final String mSharedPrefFile = "com.chlordane.android.mobileinc";
    private SharedPreferences.Editor editor;

    private final String MI5COUNT_KEY = "mi5_count";
    private final String MIMAXCOUNT_KEY = "mimax_count";
    private final String REDMICOUNT_KEY = "redmi_count";
    private final String GALAXYNOTE8COUNT_KEY = "galaxynote8_count";
    private final String GALAXYNOTE5COUNT_KEY = "galaxynote5_count";
    private final String GALAXYS8COUNT_KEY = "galaxys8_count";
    private final String PROMO_KEY = "promo_key";

    // Location Service
    private LocationTracker myLocation;
    public String myAddress = null;
    String locationPermission = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int RC_LOCATION_PERMISSION_ID = 1001;

    // Shake Listener
    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;

    private Intent service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeMain);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPreferences = getSharedPreferences(mSharedPrefFile, Context.MODE_PRIVATE);
        editor = mPreferences.edit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("All"));
        //tabLayout.addTab(tabLayout.newTab());
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

        // Initialize Shake Listener
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();
        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                minimizeApp();
            }
        });

        // Just to Log SharefPreferences Key-Values
        Map<String,?> keys = PreferenceManager.getDefaultSharedPreferences(this).getAll();
        for(Map.Entry<String,?> entry : keys.entrySet()){
            Log.d("SharedPrefs",entry.getKey() + ": " +
                    entry.getValue().toString());
        }

        Toast.makeText(getApplicationContext(),"Press image to view product detail",Toast.LENGTH_LONG).show();
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

        getLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideProgressDialog();

        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        /*if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();*/
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
        if (id == R.id.action_review_cart) {
            Intent shopIntent = new Intent(getApplicationContext(), ReviewCartActivity.class);
            shopIntent.putExtra(EXTRA_NAME, playerName);
            shopIntent.putExtra(EXTRA_LOCATION, myAddress);
            startActivityForResult(shopIntent, TEXT_REQUEST);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
        } else if (id == R.id.nav_my_qr_code) {
            if(mPreferences.getString(PROMO_KEY,"").equals("")){
                Toast.makeText(getApplicationContext(),"You don't have any promotion code",Toast.LENGTH_SHORT).show();
            }else {
                Intent myQRIntent = new Intent(getApplicationContext(), YourQRCodeActivity.class);
                startActivityForResult(myQRIntent, TEXT_REQUEST);
            }
        } else if (id == R.id.nav_sign_out) {
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
        // Clear Sharedpref vars
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

        // Firebase sign out
        mAuth.signOut();

        // Stop Service
        stopService(service);
        service = null;

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
            playerName = acct.getDisplayName();
            View v = navigationView.getHeaderView(0);
            TextView nameTextView = (TextView) v.findViewById(R.id.playerName);
            nameTextView.setText(playerName);

            firebaseAuthWithGoogle(acct);
            sendNameAndTokenToServer(playerName);

            // Start Service once
            if (service == null) {
                service = new Intent(this, TrendService.class);
                startService(service);
            }
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

    private void getLocation(){
        Log.d(TAG, "getLocation invoked");
        if (myAddress == null)
            myAddress = "UNDEFINED";

        // Check Permission
        checkLocationPermission();
        myLocation = new LocationTracker(MainActivity.this);

        // check if GPS enabled
        if(myLocation.canGetLocation()){
            Log.d(TAG, "getLocation canGetLocation");
            double latitude = myLocation.getLatitude();
            double longitude = myLocation.getLongitude();

            // \n is for new line
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent
                // max location result to returned, by documents it recommended 1 to 5

                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();

                myAddress = city;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            myLocation.showSettingsAlert();
        }
    }

    private void checkLocationPermission() {
        try {
            Log.d(TAG, "checkLocationPermission invoked");

            if (ActivityCompat.checkSelfPermission(this, locationPermission)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{locationPermission},
                        RC_LOCATION_PERMISSION_ID);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RC_LOCATION_PERMISSION_ID:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, locationPermission)
                            != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    getLocation();
                }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    public void minimizeApp() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}
