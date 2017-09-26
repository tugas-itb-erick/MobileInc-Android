package com.chlordane.android.mobileinc;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by ROG on 9/25/2017.
 */

public class PagerAdapterShop extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapterShop(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TabFragmentShopAll();
            case 1:
                return new TabFragmentShopSamsung();
            case 2:
                return new TabFragmentShopXiaomi();
            case 3:
                return new TabFragmentShopIPhone();
            case 4:
                return new TabFragmentShopLG();
            case 5:
                return new TabFragmentShopAsus();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "All";
            case 1:
                return "Samsung";
            case 2:
                return "Xiaomi";
            case 3:
                return "IPhone";
            case 4:
                return "LG";
            case 5:
                return "Asus";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
