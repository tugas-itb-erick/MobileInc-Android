package com.chlordane.android.mobileinc;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class TrendService extends Service {

    private static final String TAG = "TrendService";
    public static final int notify = 300000;  // interval between two services (Here Service run every 10 min)
    int count = 0;  // number of times service is displayed
    private Handler mHandler = new Handler();   //run on another Thread to avoid crash
    private Timer mTimer = null;    //timer handling

    public TrendService() {
        super();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        if (mTimer != null) // Cancel if already existed
            mTimer.cancel();
        else
            mTimer = new Timer();   //recreate new
        mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, notify);   //Schedule task
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();    //For Cancel Timer
    }

    //class TimeDisplay for handling task
    class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "Service Started!");

                    RequestQueue requestQueue = Volley.newRequestQueue(TrendService.this);
                    String url = "http://mobileinc.herokuapp.com/api/trend";

                    StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse");
                            Log.d(TAG, response);

                            Intent intent = new Intent(TrendService.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            PendingIntent pendingIntent = PendingIntent.getActivity(TrendService.this, 0 /* Request code */, intent,
                                    PendingIntent.FLAG_ONE_SHOT);

                            SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(TrendService.this);

                            Boolean enabled = preference.getBoolean("notifications_new_message", true);
                            if (enabled) {
                                try {
                                    JSONObject remote = new JSONObject(response);
                                    String trending = remote.get("trending").toString();
                                    String orders = remote.get("orders").toString();
                                    
                                    if(Integer.parseInt(orders) != 0) {

                                        NotificationCompat.Builder notificationBuilder =
                                                new NotificationCompat.Builder(TrendService.this)
                                                        .setSmallIcon(R.drawable.logo_lowres_icon)
                                                        .setContentTitle(trending + " is Popular!")
                                                        .setContentText(trending + " has been ordered " + orders + " times! Grab it now fast!")
                                                        .setAutoCancel(true)
                                                        .setContentIntent(pendingIntent);

                                        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                        String strRingtonePreference = preference.getString("notifications_new_message_ringtone", "DEFAULT_SOUND");
                                        Uri uri = Uri.parse(strRingtonePreference);
                                        if (strRingtonePreference.equals("DEFAULT_SOUND")) {
                                            notificationBuilder.setSound(defaultSoundUri);
                                        } else {
                                            notificationBuilder.setSound(uri);
                                        }

                                        Boolean vibratePreference = preference.getBoolean("notifications_new_message_vibrate", true);
                                        if (vibratePreference) {
                                            notificationBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
                                        }

                                        NotificationManager notificationManager =
                                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                                        notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    },new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // on error
                        }
                    });

                    requestQueue.add(postRequest);
                }
            });

        }

    }
}

