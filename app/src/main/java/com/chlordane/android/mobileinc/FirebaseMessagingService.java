package com.chlordane.android.mobileinc;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by asus on 9/29/2017.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

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
        String strRingtonePreference = preference.getString("notifications_new_message_ringtone", "DEFAULT_SOUND");
        Uri defaultSoundUri = Uri.parse(strRingtonePreference);

        String qrcode = remoteMessage.getData().get("code");
        if (qrcode != null){
            // Set to SharedPref


            // Send POST to server
        }

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo_lowres_icon)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
