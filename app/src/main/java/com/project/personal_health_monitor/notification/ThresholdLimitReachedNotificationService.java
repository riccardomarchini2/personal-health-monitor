package com.project.personal_health_monitor.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;

import com.project.personal_health_monitor.PersonalHealthMonitor;
import com.project.personal_health_monitor.R;
import com.project.personal_health_monitor.persistence.model.HealthParameter;
import com.project.personal_health_monitor.persistence.model.ReportWithHealthParameters;
import com.project.personal_health_monitor.view.MainActivity;
import com.project.personal_health_monitor.view.home.HomeFragment;
import com.project.personal_health_monitor.view_model.ReportViewModel;
import com.project.personal_health_monitor.view_model.model.SummaryHealthParameter;
import com.project.personal_health_monitor.view_model.model.SummaryReport;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.project.personal_health_monitor.view.SettingsActivity.THRESHOLD_CONTROL_DEFAULT;
import static com.project.personal_health_monitor.view.SettingsActivity.THRESHOLD_DEFAULT;

public class ThresholdLimitReachedNotificationService extends LifecycleService {

    // Sets the importance of notification. Shows everywhere, makes noise and peeks.
    private static final int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

    // Sets channel's name for NotificationManager Class
    private static final String CHANNEL_NAME = "PersonalHealthMonitor";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String name = intent.getExtras().getString("name");
        double threshold = intent.getExtras().getDouble("threshold");
        double averageValue = intent.getExtras().getDouble("averageValue");

        sendNotification(name, threshold,averageValue);
        return super.onStartCommand(intent, flags, startId);
    }

    private void sendNotification(String name, double threshold, double averageValue) {
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

        // Creates notification in the notification channel
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_NAME)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle("THRESHOLD EXCEED")
            .setContentText("Actual health parameter has exceeded the safe threshold. "+"Paramenter: "+name+", threshold: "+threshold+", average value: "+averageValue)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(contentIntent)
            .setAutoCancel(true);

        // Post a notification to be shown in the status bar or update an existing one
        notificationManager.notify(Notification.NOTIFICATION_TYPE_IDS.get(NotificationType.THRESHOLD_LIMIT_REACHED), notificationBuilder.build());

    }

}
