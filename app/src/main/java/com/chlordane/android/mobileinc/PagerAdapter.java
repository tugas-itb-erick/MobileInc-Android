package com.chlordane.android.mobileinc;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by ROG on 9/24/2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TabFragmentShopAll();
            //case 1:
              //  return new TabFragmentDummy();
            case 1:
                return new TabFragmentShopSamsung();
            case 2:
                return new TabFragmentShopXiaomi();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
