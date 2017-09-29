package com.chlordane.android.mobileinc;


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
    private ArrayList<Product> ProductList;

    public TabFragmentShopXiaomi() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tab_fragment_shop_xiaomi, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewXiaomi);
        recyclerView.setHasFixedSize(true);
        ProductAdapter adapter = new ProductAdapter(getContext(),ProductList);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager((getActivity()));
        recyclerView.setLayoutManager(linearLayoutManager);

        return rootView;
    }

    public void initData(){
        ProductList = new ArrayList<Product>(3);

        Product prod1 = new Product("Xiaomi1","desc lorem ipsum", 1);
        Product prod2 = new Product("Xiaomi2","desc lorem ipsum", 2);
        Product prod3 = new Product("Xiaomi3","desc lorem ipsum", 3);

        ProductList.add(prod1);
        ProductList.add(prod2);
        ProductList.add(prod3);

    }
}
