package com.chlordane.android.mobileinc;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class YourQRCodeActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private static final String mSharedPrefFile = "com.chlordane.android.mobileinc";
    private ImageView imageView;
    private final String PROMO_KEY = "promo_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_qrcode);

        mPreferences = getSharedPreferences(mSharedPrefFile, Context.MODE_PRIVATE);
        imageView = (ImageView) findViewById(R.id.qrCodeImage);

        Log.d("Promo code",mPreferences.getString(PROMO_KEY,""));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://mobileinc.herokuapp.com/api/manage/promotion/confirm";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Confirm response",response);

                // Move load image here to ensure synchronous actions
                LoadQRCode(imageView);
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Confirm error",error.toString());

                // Move load image here to ensure synchronous actions
                LoadQRCode(imageView);
            }
        })
        {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String,String>();
                params.put("promo_code",mPreferences.getString(PROMO_KEY,""));

                return params;
            }
        };

        requestQueue.add(postRequest);
    }

    protected void LoadQRCode(ImageView v)
    {
        try {
            Picasso.with(getApplicationContext()).load("https://api.qrserver.com/v1/create-qr-code/?size=150x150&data="+mPreferences.getString(PROMO_KEY,"")).into(v);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
