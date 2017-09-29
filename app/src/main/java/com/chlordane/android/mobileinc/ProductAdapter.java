package com.chlordane.android.mobileinc;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        public ViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.productCardView);
            mProductName = (TextView) itemView.findViewById(R.id.productName);
            mDescription = (TextView) itemView.findViewById(R.id.productDescription);
            mProductImage = (ImageView) itemView.findViewById(R.id.mobileImage);
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
        }

        public void bindTo(Product currentProduct) {
            mProductName.setText(currentProduct.getProductName());
            mDescription.setText(currentProduct.getProductDescription());
        }
    }
}
