package ca.ualberta.CMPUT3012019T02.alexandria.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.activity.MainActivity;

import static ca.ualberta.CMPUT3012019T02.alexandria.App.getContext;

/**
 * The service for handling notifications
 */
public class NotificationService extends FirebaseMessagingService {


    // Based on https://stackoverflow.com/questions/16045722/notification-not-showing
    // And https://github.com/firebase/quickstart-android/blob/d307afe958a672bebdf3394c10017cc671c027e2/messaging/app/src/main/java/com/google/firebase/quickstart/fcm/java/MyFirebaseMessagingService.java
    // Both licensed under Apache License, Version 2.0
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            NotificationCompat.Builder mBuilder =   new NotificationCompat.Builder(getContext(), "heads_up")
                    .setSmallIcon(R.drawable.baseline_local_library_24)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(Notification.PRIORITY_HIGH);
            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
            mBuilder.setContentIntent(contentIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelId = "heads_up";
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Alexandria Notifications",
                        NotificationManager.IMPORTANCE_HIGH);
                mNotificationManager.createNotificationChannel(channel);
                mBuilder.setChannelId(channelId);
            }

            mNotificationManager.notify(0, mBuilder.build());
        }
    }
}