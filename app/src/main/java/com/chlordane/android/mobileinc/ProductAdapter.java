package com.chlordane.android.mobileinc;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        //Glide.with(mContext).load(currentProduct.getImageResource()).into(holder.mProductImage);
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
                    //intent untuk buka web
                }
            });
        }

        public void bindTo(Product currentProduct) {
            mProductName.setText(currentProduct.getProductName());
            mDescription.setText(currentProduct.getProductDescription());
        }
    }
}
