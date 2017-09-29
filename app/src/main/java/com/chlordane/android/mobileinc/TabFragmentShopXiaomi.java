package com.chlordane.android.mobileinc;


import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragmentShopXiaomi extends Fragment {
    private ArrayList<Product> productList;
    private ProductAdapter mAdapter;

    public TabFragmentShopXiaomi() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tab_fragment_shop_xiaomi, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewXiaomi);
        recyclerView.setHasFixedSize(true);

        productList = new ArrayList<Product>();
        mAdapter = new ProductAdapter(getContext(),productList);
        recyclerView.setAdapter(mAdapter);
        initData();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager((getActivity()));
        recyclerView.setLayoutManager(linearLayoutManager);

        return rootView;
    }

    public void initData(){
        String[] productNameList = getResources().getStringArray(R.array.product_names);
        String[] productDescriptionList = getResources().getStringArray(R.array.product_descriptions);

        productList.clear();
        TypedArray productsImageResources = getResources().obtainTypedArray(R.array.product_images);

        for(int i=0;i<3;i++){
            productList.add(new Product(productNameList[i],productDescriptionList[i],productsImageResources.getResourceId(i,0)));
        }

        mAdapter.notifyDataSetChanged();
    }
}
