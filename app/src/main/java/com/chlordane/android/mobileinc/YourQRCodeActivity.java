package com.chlordane.android.mobileinc;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

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

        try {
            Picasso.with(getApplicationContext()).load("https://api.qrserver.com/v1/create-qr-code/?size=150x150&data="+mPreferences.getString(PROMO_KEY,"")).into(imageView);
        }catch (Exception e){

        }
    }
}
