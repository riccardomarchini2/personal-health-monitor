package com.project.personal_health_monitor.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;

import com.project.personal_health_monitor.R;
import com.project.personal_health_monitor.view.MainActivity;

/**
 * It's the notification manager.
 */
public class NotificationMaster extends LifecycleService {

    // Sets the importance of notification. Shows everywhere, makes noise and peeks.
    private static final int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

    // Sets channel's name for NotificationManager Class
    private static final String CHANNEL_NAME = "PersonalHealthMonitor";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sendNotification(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void sendNotification(Intent intent) {
        int id = intent.getExtras().getInt("id");

        Context context = getApplicationContext();

        // Creates instance of Notification Channel
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_NAME, CHANNEL_NAME, IMPORTANCE);

        // Creates instance of NotificationManager to get Notification's System Service
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);

        // Invokes channel creation's method
        notificationManager.createNotificationChannel(notificationChannel);

        // Creates new intent with params context and main activity class
        Intent mainActivity = new Intent(context, MainActivity.class);

        /*
         * Flag indicating that if the described PendingIntent already exists,
         * then keep it but replace its extra data with what is in this new Intent.
         */
        PendingIntent contentIntent = PendingIntent.getActivity(context, 1, mainActivity, PendingIntent.FLAG_UPDATE_CURRENT);

        // Creats notification in the notification channel
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_NAME)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle("Remember to insert a report")
            .setContentText("Altro testo")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(contentIntent)
            .setAutoCancel(true);

        // Post a notification to be shown in the status bar or update an existing one
        notificationManager.notify(id, notificationBuilder.build());

    }

}
