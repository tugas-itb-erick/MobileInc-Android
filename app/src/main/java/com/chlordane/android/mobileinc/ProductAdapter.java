package com.chlordane.android.mobileinc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ROG on 9/29/2017.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private ArrayList<Product> mProductData;
    private Context mContext;

    private int mCountMi5 = 0;
    private int mCountMiMax = 0;
    private int mCountRedmi = 0;
    private int mGalaxyNote8 = 0;
    private int mGalaxyS8 = 0;
    private int mGalaxyNote5 = 0;

    private final String MI5COUNT_KEY = "mi5_count";
    private final String MIMAXCOUNT_KEY = "mimax_count";
    private final String REDMICOUNT_KEY = "redmi_count";
    private final String GALAXYNOTE8COUNT_KEY = "galaxynote8_count";
    private final String GALAXYNOTE5COUNT_KEY = "galaxynote5_count";
    private final String GALAXYS8COUNT_KEY = "galaxys8_count";

    ProductAdapter(Context context, ArrayList<Product> productData){
        mContext = context;
        mProductData = productData;
    }

    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_mobile_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product currentProduct = mProductData.get(position);
        //Populate the textviews with data
        holder.bindTo(currentProduct);
        Picasso.with(mContext).load(currentProduct.getImageResource()).into(holder.mProductImage);
    }

    @Override
    public int getItemCount() {
        return mProductData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;
        private TextView mProductName;
        private TextView mDescription;
        private ImageView mProductImage;
        private Button mPlusButton;
        private Button mMinusButton;
        private TextView mItemCount;

        private SharedPreferences mPreferences;
        private static final String mSharedPrefFile = "com.chlordane.android.mobileinc";
        private final SharedPreferences.Editor editor;

        public ViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.productCardView);
            mProductName = (TextView) itemView.findViewById(R.id.productName);
            mDescription = (TextView) itemView.findViewById(R.id.productDescription);
            mProductImage = (ImageView) itemView.findViewById(R.id.mobileImage);
            mPlusButton = (Button) itemView.findViewById(R.id.plusOne);
            mMinusButton = (Button) itemView.findViewById(R.id.minusOne);
            mItemCount = (TextView) itemView.findViewById(R.id.itemCount);
            mPreferences = mContext.getSharedPreferences(mSharedPrefFile, Context.MODE_PRIVATE);
            editor = mPreferences.edit();
            Log.d("Shared Pref. Test",Integer.toString(mPreferences.getInt(MI5COUNT_KEY,0)));

            mProductImage.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String data;
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    if(mProductName.getText().equals("Mi 5")){
                        data = "http://www.mi.com/en/mi5/";
                    }else if(mProductName.getText().equals("Mi Max")){
                        data = "http://www.mi.com/en/mimax/";
                    }else if(mProductName.getText().equals("Redmi 3s")){
                        data = "http://www.mi.com/en/redmi3s/";
                    }else if(mProductName.getText().equals("Galaxy Note8 64GB (AT&T)")){
                        data = "http://www.samsung.com/us/mobile/phones/galaxy-note/galaxy-note8-64gb--at-t--orchid-gray-sm-n950uzvaatt/";
                    }else if(mProductName.getText().equals("Galaxy Note5 64GB (AT&T)")){
                        data = "http://www.samsung.com/us/mobile/phones/galaxy-note/samsung-galaxy-note5-64gb-at-t-black-sapphire-sm-n920azkeatt/";
                    }else {
                        data = "http://www.samsung.com/us/mobile/phones/galaxy-s/galaxy-s8-plus-64gb--unlocked--sm-g955uzkaxaa/";
                    }
                    intent.setData(Uri.parse(data));
                    if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                        mContext.startActivity(intent);
                    }
                }
            });

            mPlusButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //String stringValue = mItemCount.getText().toString();
                    int currentValue = 0;

                    if(mProductName.getText().equals("Mi 5")){
                        currentValue = mPreferences.getInt(MI5COUNT_KEY,0)+1;
                        editor.putInt(MI5COUNT_KEY,currentValue);
                    }else if(mProductName.getText().equals("Mi Max")){
                        currentValue = mPreferences.getInt(MIMAXCOUNT_KEY,0)+1;
                        editor.putInt(MIMAXCOUNT_KEY,currentValue);
                    }else if(mProductName.getText().equals("Redmi 3s")){
                        currentValue = mPreferences.getInt(REDMICOUNT_KEY,0)+1;
                        editor.putInt(REDMICOUNT_KEY,currentValue);
                    }else if(mProductName.getText().equals("Galaxy Note8 64GB (AT&T)")){
                        currentValue = mPreferences.getInt(GALAXYNOTE8COUNT_KEY,0)+1;
                        editor.putInt(GALAXYNOTE8COUNT_KEY,currentValue);
                    }else if(mProductName.getText().equals("Galaxy Note5 64GB (AT&T)")){
                        currentValue = mPreferences.getInt(GALAXYNOTE5COUNT_KEY,0)+1;
                        editor.putInt(GALAXYNOTE5COUNT_KEY,currentValue);
                    }else {
                        currentValue = mPreferences.getInt(GALAXYS8COUNT_KEY,0)+1;
                        editor.putInt(GALAXYS8COUNT_KEY,currentValue);
                    }
                    editor.apply();
                    mItemCount.setText(Integer.toString(currentValue));
                }
            });

            mMinusButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //String stringValue = mItemCount.getText().toString();
                    int currentValue = 0;

                    if(mProductName.getText().equals("Mi 5")){
                        currentValue = (mPreferences.getInt(MI5COUNT_KEY,0)==0)? 0:mPreferences.getInt(MI5COUNT_KEY,0)-1;
                        editor.putInt(MI5COUNT_KEY,currentValue);
                    }else if(mProductName.getText().equals("Mi Max")){
                        currentValue = (mPreferences.getInt(MIMAXCOUNT_KEY,0)==0)? 0:mPreferences.getInt(MIMAXCOUNT_KEY,0)-1;
                        editor.putInt(MIMAXCOUNT_KEY,currentValue);
                    }else if(mProductName.getText().equals("Redmi 3s")){
                        currentValue = (mPreferences.getInt(REDMICOUNT_KEY,0)==0)? 0:mPreferences.getInt(REDMICOUNT_KEY,0)-1;
                        editor.putInt(REDMICOUNT_KEY,currentValue);
                    }else if(mProductName.getText().equals("Galaxy Note8 64GB (AT&T)")){
                        currentValue = (mPreferences.getInt(GALAXYNOTE8COUNT_KEY,0)==0)? 0:mPreferences.getInt(GALAXYNOTE8COUNT_KEY,0)-1;
                        editor.putInt(GALAXYNOTE8COUNT_KEY,currentValue);
                    }else if(mProductName.getText().equals("Galaxy Note5 64GB (AT&T)")){
                        currentValue = (mPreferences.getInt(GALAXYNOTE5COUNT_KEY,0)==0)? 0:mPreferences.getInt(GALAXYNOTE5COUNT_KEY,0)-1;
                        editor.putInt(GALAXYNOTE5COUNT_KEY,currentValue);
                    }else {
                        currentValue = (mPreferences.getInt(GALAXYS8COUNT_KEY,0)==0)? 0:mPreferences.getInt(GALAXYS8COUNT_KEY,0)-1;
                        editor.putInt(GALAXYS8COUNT_KEY,currentValue);
                    }
                    editor.apply();
                    mItemCount.setText(Integer.toString(currentValue));
                }
            });
        }

        public void bindTo(Product currentProduct) {
            mProductName.setText(currentProduct.getProductName());
            mDescription.setText(currentProduct.getProductDescription());
            if(mProductName.getText().equals("Mi 5")){
                mItemCount.setText(Integer.toString(mPreferences.getInt(MI5COUNT_KEY,0)));
            }else if(mProductName.getText().equals("Mi Max")){
                mItemCount.setText(Integer.toString(mPreferences.getInt(MIMAXCOUNT_KEY,0)));
            }else if(mProductName.getText().equals("Redmi 3s")){
                mItemCount.setText(Integer.toString(mPreferences.getInt(REDMICOUNT_KEY,0)));
            }else if(mProductName.getText().equals("Galaxy Note8 64GB (AT&T)")){
                mItemCount.setText(Integer.toString(mPreferences.getInt(GALAXYNOTE8COUNT_KEY,0)));
            }else if(mProductName.getText().equals("Galaxy Note5 64GB (AT&T)")){
                mItemCount.setText(Integer.toString(mPreferences.getInt(GALAXYNOTE5COUNT_KEY,0)));
            }else {
                mItemCount.setText(Integer.toString(mPreferences.getInt(GALAXYS8COUNT_KEY,0)));
            }
        }
    }
}
