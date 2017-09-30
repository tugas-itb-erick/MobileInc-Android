package com.chlordane.android.mobileinc;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by asus on 9/29/2017.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TOKEN_TAG = "FirebaseIIDService";
    private static final String REGISTRATION_TAG = "FirebaseRegistration";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TOKEN_TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(final String refreshedToken) {
        Log.d(REGISTRATION_TAG,"Method sendRegistrationToServer invoked.");
    }
}

