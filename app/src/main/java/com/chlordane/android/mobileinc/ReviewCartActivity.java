package com.chlordane.android.mobileinc;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class ReviewCartActivity extends AppCompatActivity {

    private ArrayList<Integer> amountOfItem;
    private float discountValue;

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
    private final String DISCOUNT_KEY = "discount";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_cart);

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

        clearPreviousData();
        mPreferences = getSharedPreferences(mSharedPrefFile, Context.MODE_PRIVATE);
        editor = mPreferences.edit();

        initializeData();
        printCartData();
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
        amountOfItem = new ArrayList<Integer>(6);

        amountOfItem.add(mPreferences.getInt(MI5COUNT_KEY,0));
        amountOfItem.add(mPreferences.getInt(MIMAXCOUNT_KEY,0));
        amountOfItem.add(mPreferences.getInt(REDMICOUNT_KEY,0));
        amountOfItem.add(mPreferences.getInt(GALAXYNOTE8COUNT_KEY,0));
        amountOfItem.add(mPreferences.getInt(GALAXYNOTE5COUNT_KEY,0));
        amountOfItem.add(mPreferences.getInt(GALAXYS8COUNT_KEY,0));

        discountValue = mPreferences.getFloat(DISCOUNT_KEY,new Float(0.1));

    }

    public void printCartData(){
        String[] productNameList = getResources().getStringArray(R.array.product_names);
        float[] priceList = new float[6];
        float disc = discountValue;
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

        total = total * (1-disc);
        discount.setText(Float.toString(disc));
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
}
