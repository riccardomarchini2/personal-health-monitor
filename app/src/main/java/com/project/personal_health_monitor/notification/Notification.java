package com.project.personal_health_monitor.notification;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.project.personal_health_monitor.view.SettingsActivity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class Notification extends BroadcastReceiver {
    public static final Map<NotificationType, Integer> NOTIFICATION_TYPE_IDS = Map.of(
        NotificationType.REMEMBER_TO_CREATE_REPORT, 0,
        NotificationType.THRESHOLD_LIMIT_REACHED, 1
    );

    // Sets the importance of notification. Shows everywhere, makes noise and peeks.
    public static final int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

    // Sets channel's name for NotificationManager Class
    public static final String CHANNEL_NAME = "PersonalHealthMonitor";

    private static final Map<NotificationType, PendingIntent> NOTIFICATION_PENDING_INTENT = Map.of();

    public static void setRememberToCreateReportNotification(Context context, LocalDateTime localDateTime) {
        PendingIntent existingPendingIntent = NOTIFICATION_PENDING_INTENT.get(NotificationType.REMEMBER_TO_CREATE_REPORT);
        if (Objects.nonNull(existingPendingIntent)) {
            unsetRememberToCreateReportNotification(context);
        }

        /*
         * Creates instance of AlarmManager getting SystemService by context param and
         * initialize to ALARM_SERVICE for receiving intents.
         */
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Creates new intent to receives notification
        Intent notificationReceiver = new Intent(context, Notification.class);

        // Puts new notification to intent just created
        notificationReceiver.putExtra("id", NOTIFICATION_TYPE_IDS.get(NotificationType.REMEMBER_TO_CREATE_REPORT));

        /*
         * Creates new PendingIntent which returns an existing or new PendingIntent, matching
         * the given parameters.
         */
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationReceiver, PendingIntent.FLAG_UPDATE_CURRENT);

        // Returns the date of this moments in milliseconds
        long millis = localDateTime.toInstant(OffsetDateTime.now().getOffset()).toEpochMilli();

        // Sets exact alarm to wake up after millis milliseconds
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, millis, AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private static void unsetRememberToCreateReportNotification(Context context) {
        /*
         * Creates instance of AlarmManager getting SystemService by context param and
         * initialize to ALARM_SERVICE for receiving intents.
         */
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = NOTIFICATION_PENDING_INTENT.get(NotificationType.REMEMBER_TO_CREATE_REPORT);
        alarmManager.cancel(pendingIntent);
    }

    public static void setPostponeRememberToCreateReportNotification(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SettingsActivity.NAME, MODE_PRIVATE);

        int minutes = sharedPreferences.getInt(SettingsActivity.POSTPONE_BY, SettingsActivity.DEFAULT_POSTPONE_BY);

        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(minutes);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Creates new intent to receives notification
        Intent notificationReceiver = new Intent(context, Notification.class);

        // Puts new notification to intent just created
        notificationReceiver.putExtra("id", NOTIFICATION_TYPE_IDS.get(NotificationType.REMEMBER_TO_CREATE_REPORT));

        /*
         * Creates new PendingIntent which returns an existing or new PendingIntent, matching
         * the given parameters.
         */
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationReceiver, PendingIntent.FLAG_UPDATE_CURRENT);

        // Returns the date of this moments in milliseconds
        long millis = localDateTime.toInstant(OffsetDateTime.now().getOffset()).toEpochMilli();

        // Sets exact alarm to wake up after millis milliseconds
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, millis, pendingIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getExtras().getInt("id");

        // Creates instance of intent called notificationService passing context and Master class
        Intent notificationService = null;

        if (Objects.equals(id, NOTIFICATION_TYPE_IDS.get(NotificationType.REMEMBER_TO_CREATE_REPORT))) {
            notificationService = new Intent(context, RememberToCreateReportNotificationService.class);
        } else {
            notificationService = new Intent(context, ThresholdLimitReachedNotificationService.class);
        }

        /*
         * starService method, manages service lifecycle surrounding the processing of the intent,
         * which can take multiple milliseconds of CPU time. Due to this cost, startService()
         * should not be used for frequent intent delivery to a service, and only
         * for scheduling significant work.
         */
        context.startService(notificationService);
    }
}
