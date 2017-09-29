package com.chlordane.android.mobileinc;

/**
 * Created by ROG on 9/28/2017.
 */

public class Product {
    private int imageResource;
    private String productName;
    private String description;
    private String url;

    public Product(String productName, String description, int imageResource) {
        this.productName = productName;
        this.description = description;
        this.imageResource = imageResource;
        this.url = url;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductDescription() {
        return description;
    }

    public int getImageResource(){
        return imageResource;
    }

    public String getProductUrl(){
        return url;
    }
}
