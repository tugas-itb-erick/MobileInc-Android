package com.chlordane.android.mobileinc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReviewCartActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;
    private ArrayList<Integer> amountOfItem;
    private String promoCode;
    private String name;
    private String location;

    private TextView[] productName;
    private TextView[] amountLabel;
    private TextView[] amount;
    private TextView[] subTotalPrice;
    private TextView discount;
    private TextView totalPrice;
    private EditText creditCardNumber;

    private SharedPreferences mPreferences;
    private static final String mSharedPrefFile = "com.chlordane.android.mobileinc";
    private SharedPreferences.Editor editor;

    private final String MI5COUNT_KEY = "mi5_count";
    private final String MIMAXCOUNT_KEY = "mimax_count";
    private final String REDMICOUNT_KEY = "redmi_count";
    private final String GALAXYNOTE8COUNT_KEY = "galaxynote8_count";
    private final String GALAXYNOTE5COUNT_KEY = "galaxynote5_count";
    private final String GALAXYS8COUNT_KEY = "galaxys8_count";
    private final String MYQR_KEY = "my_qr_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_cart);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        productName = new TextView[6];
        amountLabel = new TextView[6];
        amount = new TextView[6];
        subTotalPrice = new TextView[6];

        productName[0] = (TextView) findViewById(R.id.productName1);
        productName[1] = (TextView) findViewById(R.id.productName2);
        productName[2] = (TextView) findViewById(R.id.productName3);
        productName[3] = (TextView) findViewById(R.id.productName4);
        productName[4] = (TextView) findViewById(R.id.productName5);
        productName[5] = (TextView) findViewById(R.id.productName6);

        amountLabel[0] = (TextView) findViewById(R.id.amount1);
        amountLabel[1] = (TextView) findViewById(R.id.amount2);
        amountLabel[2] = (TextView) findViewById(R.id.amount3);
        amountLabel[3] = (TextView) findViewById(R.id.amount4);
        amountLabel[4] = (TextView) findViewById(R.id.amount5);
        amountLabel[5] = (TextView) findViewById(R.id.amount6);

        amount[0] = (TextView) findViewById(R.id.amountValue1);
        amount[1] = (TextView) findViewById(R.id.amountValue2);
        amount[2] = (TextView) findViewById(R.id.amountValue3);
        amount[3] = (TextView) findViewById(R.id.amountValue4);
        amount[4] = (TextView) findViewById(R.id.amountValue5);
        amount[5] = (TextView) findViewById(R.id.amountValue6);

        subTotalPrice[0] = (TextView) findViewById(R.id.totalPrice1);
        subTotalPrice[1] = (TextView) findViewById(R.id.totalPrice2);
        subTotalPrice[2] = (TextView) findViewById(R.id.totalPrice3);
        subTotalPrice[3] = (TextView) findViewById(R.id.totalPrice4);
        subTotalPrice[4] = (TextView) findViewById(R.id.totalPrice5);
        subTotalPrice[5] = (TextView) findViewById(R.id.totalPrice6);

        discount = (TextView) findViewById(R.id.discount);
        totalPrice = (TextView) findViewById(R.id.totalPriceAll);
        creditCardNumber = (EditText) findViewById(R.id.creditCardNumber);

        Intent intent = getIntent();
        name = intent.getStringExtra(MainActivity.EXTRA_NAME);
        location = intent.getStringExtra(MainActivity.EXTRA_LOCATION);

        clearPreviousData();
        mPreferences = getSharedPreferences(mSharedPrefFile, Context.MODE_PRIVATE);
        editor = mPreferences.edit();

        amountOfItem = new ArrayList<Integer>(6);

        initializeData();
        printCartData();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();
        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                minimizeApp();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    private void clearPreviousData() {
        int i;
        for(i = 0; i < 6; i++){
            productName[i].setText("");
            amountLabel[i].setText("");
            amount[i].setText("");
            subTotalPrice[i].setText("");
        }
    }

    public void initializeData(){
        amountOfItem.add(mPreferences.getInt(MI5COUNT_KEY,0));
        amountOfItem.add(mPreferences.getInt(MIMAXCOUNT_KEY,0));
        amountOfItem.add(mPreferences.getInt(REDMICOUNT_KEY,0));
        amountOfItem.add(mPreferences.getInt(GALAXYNOTE8COUNT_KEY,0));
        amountOfItem.add(mPreferences.getInt(GALAXYNOTE5COUNT_KEY,0));
        amountOfItem.add(mPreferences.getInt(GALAXYS8COUNT_KEY,0));

        promoCode = mPreferences.getString(MYQR_KEY,"");
        Log.d("init test : ", promoCode);
    }

    public void printCartData(){
        String[] productNameList = getResources().getStringArray(R.array.product_names);
        float[] priceList = new float[6];
        float disc = (float) 0.1;
        float total = 0;

        priceList[0] = 460;
        priceList[1] = 400;
        priceList[2] = 140;
        priceList[3] = 950;
        priceList[4] = 840;
        priceList[5] = 820;

        int i;
        for(i = 0; i < 6; i++){
            if(amountOfItem.get(i) > 0) {
                int j = 0;
                while (!productName[j].getText().equals("") && !amountLabel[j].getText().equals("")
                        && !amount[j].getText().equals("") && !subTotalPrice[j].getText().equals("")) {
                    j++;
                }
                productName[j].setText(productNameList[i]);
                amountLabel[j].setText("amount: ");
                amount[j].setText(Integer.toString(amountOfItem.get(i)));
                subTotalPrice[j].setText(Float.toString(amountOfItem.get(i) * priceList[i]));
                total = total + amountOfItem.get(i) * priceList[i];
            }
        }

        if(!(promoCode.equals(""))) {
            Log.d("review cart : ", promoCode);
            total = total * (1-disc);
            discount.setText("10%");
        }
        else discount.setText("0%");

        totalPrice.setText(Float.toString(total));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void payBill(View view) {
        boolean transactionStatus = false;
        if(isCartEmpty()){
            Toast.makeText(getApplicationContext(),"Your cart is empty!",Toast.LENGTH_SHORT).show();
        }else {
            if(creditCardNumber.getText().toString().length() < 16){
                if(creditCardNumber.getText().toString().length() == 0){
                    Toast.makeText(getApplicationContext(),"Credit card number is required",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Invalid credit card number (16 digits)",Toast.LENGTH_SHORT).show();
                }
            }else {
				
                Toast.makeText(getApplicationContext(), "Your transaction is being processed...", Toast.LENGTH_LONG).show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
						boolean status = false;
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        String url = "http://mobileinc.herokuapp.com/api/manage/order/order";

                        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String message = jsonObject.getString("message");

                                    Log.d("Message",message);

                                    if(message.equals("Promo code either invalid or expired.")) {
                                        Toast.makeText(getApplicationContext(),"Promo code invalid or expired",Toast.LENGTH_SHORT).show();

                                        editor.putString(MYQR_KEY,"");
                                        editor.apply();
                                    }
                                    else if(message.equals("No item ordered.")) {
                                        Toast.makeText(getApplicationContext(),"Your cart is empty!",Toast.LENGTH_SHORT).show();
                                        // jgn reset data, jgn balik ke main activity

                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "Transaction complete!", Toast.LENGTH_LONG).show();
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

                                        for(int i=0;i<6;i++){
                                            amountOfItem.set(i,0);
                                        }

                                        editor.putString(MYQR_KEY,"");
                                        editor.apply();
                                        finish();

                                    }
                                }
                                catch(Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "Transaction failed", Toast.LENGTH_LONG).show();
                            }
                        }) {
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("account_name", name);
                                params.put("city", location);
                                params.put("promo_code", promoCode);
                                params.put("Mi_5", Integer.toString(mPreferences.getInt(MI5COUNT_KEY, 0)));
                                params.put("Mi_Max", Integer.toString(mPreferences.getInt(MIMAXCOUNT_KEY, 0)));
                                params.put("Redmi_3s", Integer.toString(mPreferences.getInt(REDMICOUNT_KEY, 0)));
                                params.put("Galaxy_Note_8", Integer.toString(mPreferences.getInt(GALAXYNOTE8COUNT_KEY, 0)));
                                params.put("Galaxy_Note_5", Integer.toString(mPreferences.getInt(GALAXYNOTE5COUNT_KEY, 0)));
                                params.put("Galaxy_S8+", Integer.toString(mPreferences.getInt(GALAXYS8COUNT_KEY, 0)));

                                return params;
                            }
                        };

                        requestQueue.add(postRequest);
                    }
                }, 2000);
            }
        }
    }

    public boolean isCartEmpty(){
        int i = 0;
        boolean empty = true;
        while((empty)&&(i<6)){
            if(amountOfItem.get(i) > 0){
                empty = false;
            }else{
                i++;
            }
        }
        return empty;
    }

    public void minimizeApp() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}
