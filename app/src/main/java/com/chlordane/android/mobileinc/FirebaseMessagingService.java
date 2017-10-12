package com.chlordane.android.mobileinc;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by asus on 9/29/2017.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private SharedPreferences mPreferences;
    private static final String mSharedPrefFile = "com.chlordane.android.mobileinc";
    private SharedPreferences.Editor editor;

    private final String PROMO_KEY = "promo_key";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        showNotification(remoteMessage);
    }

    private void showNotification(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
        mPreferences = getSharedPreferences(mSharedPrefFile, Context.MODE_PRIVATE);
        editor = mPreferences.edit();

        final String qrcode = remoteMessage.getData().get("code");

        if (qrcode != null){
            editor.putString(PROMO_KEY,qrcode);
            editor.apply();
        }

        Boolean enabled = preference.getBoolean("notifications_new_message", true);
        if (enabled) {
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.logo_lowres_icon)
                            .setContentTitle(remoteMessage.getNotification().getTitle())
                            .setContentText(remoteMessage.getNotification().getBody())
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            String strRingtonePreference = preference.getString("notifications_new_message_ringtone", "DEFAULT_SOUND");
            Uri uri = Uri.parse(strRingtonePreference);
            if (strRingtonePreference.equals("DEFAULT_SOUND")){
                notificationBuilder.setSound(defaultSoundUri);
            } else {
                notificationBuilder.setSound(uri);
            }

            Boolean vibratePreference = preference.getBoolean("notifications_new_message_vibrate", true);
            if (vibratePreference){
                notificationBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
            }

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }
    }
}
