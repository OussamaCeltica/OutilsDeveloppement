package com.sinch.android.rtc.sample.push.fcm;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sinch.android.rtc.NotificationResult;
import com.sinch.android.rtc.SinchHelpers;
import com.sinch.android.rtc.calling.CallNotificationResult;
import com.sinch.android.rtc.sample.push.LoginActivity;
import com.sinch.android.rtc.sample.push.R;
import com.sinch.android.rtc.sample.push.SinchService;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.util.Map;

public class FcmListenerService extends FirebaseMessagingService {

    public static String CHANNEL_ID = "Sinch Push Notification Channel";

    private final String PREFERENCE_FILE = "com.sinch.android.rtc.sample.push.shared_preferences";
    SharedPreferences sharedPreferences;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        Map data = remoteMessage.getData();
        if (SinchHelpers.isSinchPushPayload(data)) {
            new ServiceConnection() {
                private Map payload;

                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {

                    Context context = getApplicationContext();
                    sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);

                    if (payload != null) {
                        SinchService.SinchServiceInterface sinchService = (SinchService.SinchServiceInterface) service;
                        if (sinchService != null) {
                            NotificationResult result = sinchService.relayRemotePushNotificationPayload(payload);
                            // handle result, e.g. show a notification or similar
                            // here is example for notifying user about missed/canceled call:
                            if (result.isValid() && result.isCall()) {
                                CallNotificationResult callResult = result.getCallResult();
                                if (callResult != null && result.getDisplayName() != null) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(callResult.getRemoteUserId(), result.getDisplayName());
                                    editor.commit();
                                }
                                if (callResult.isCallCanceled()) {
                                    String displayName = result.getDisplayName();
                                    if (displayName == null) {
                                        displayName = sharedPreferences.getString(callResult.getRemoteUserId(),"n/a");
                                    }
                                    createMissedCallNotification(displayName != null && !displayName.isEmpty() ? displayName : callResult.getRemoteUserId());
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        context.deleteSharedPreferences(PREFERENCE_FILE);
                                    }
                                }
                            }
                        }
                    }
                    payload = null;
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {}

                public void relayMessageData(Map<String, String> data) {
                    payload = data;
                    createNotificationChannel(NotificationManager.IMPORTANCE_MAX);
                    getApplicationContext().bindService(new Intent(getApplicationContext(), SinchService.class), this, BIND_AUTO_CREATE);
                }
            }.relayMessageData(data);
        }
    }

    private void createNotificationChannel(int importance) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Sinch";
            String description = "Incoming Sinch Push Notifications.";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void createMissedCallNotification(String userId) {

        createNotificationChannel(NotificationManager.IMPORTANCE_DEFAULT);

        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), LoginActivity.class), 0);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle("Missed call from ")
                        .setContentText(userId)
                        .setContentIntent(contentIntent)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, builder.build());
    }
}