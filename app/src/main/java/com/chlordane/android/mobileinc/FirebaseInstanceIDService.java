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
    private static final String ACCOUNT_SUCCESS_TAG = "FirebaseAccountAdd";
    private static final String ACCOUNT_ERROR_TAG = "FirebaseAccountError";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TOKEN_TAG, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(final String refreshedToken) {

        Log.d(REGISTRATION_TAG,"Method sendRegistrationToServer invoked.");

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
                params.put("name","Reinhard");
                params.put("firebase_key",refreshedToken);

                return params;
            }
        };

        requestQueue.add(postRequest);
    }
}

