package com.chlordane.android.mobileinc;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by asus on 10/1/2017.
 */

public class Utils {

    public static void setMainTheme(Context context){
        String val = PreferenceManager.getDefaultSharedPreferences(context).getString("theme_list", "1");
        int theme = Integer.parseInt(val);
        switch (theme) {
            case 1: context.setTheme(R.style.AppThemeMain); break;
            case 2: context.setTheme(R.style.DarkThemeMain); break;
            case 3: context.setTheme(R.style.DarkThemeMain); break;
            case 4: context.setTheme(R.style.DarkThemeMain); break;
            default: context.setTheme(R.style.AppThemeMain); break;
        }
    }

    public static void setAppTheme(Context context){
        String val = PreferenceManager.getDefaultSharedPreferences(context).getString("theme_list", "1");
        int theme = Integer.parseInt(val);
        switch (theme) {
            case 1: context.setTheme(R.style.AppTheme); break;
            case 2: context.setTheme(R.style.DarkTheme); break;
            case 3: context.setTheme(R.style.DarkTheme); break;
            case 4: context.setTheme(R.style.DarkTheme); break;
            default: context.setTheme(R.style.AppTheme); break;
        }
    }

}
