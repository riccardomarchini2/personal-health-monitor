package com.project.personal_health_monitor.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Notification  extends BroadcastReceiver {

    public static void send(Context context, int id, LocalDateTime localDateTime) {
        /*
         * Creates instance of AlarmManager getting SystemService by context param and
         * initialize to ALARM_SERVICE for receiving intents.
         */
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Creates new intent to receives notification
        Intent notificationReceiver = new Intent(context, Notification.class);

        // Puts new notification to intent just created
        notificationReceiver.putExtra("id", id);

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

    public static void send(Context context, int id, int year, int month, int day, int hour, int minute) {
        send(context, id, LocalDateTime.of(year, month, day, hour, minute));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Creates instance of intent called notificationService passing context and Master class
        Intent notificationService = new Intent(context, NotificationMaster.class);

        int id = intent.getExtras().getInt("id");
        intent.putExtra("id", id);

        /*
         * starService method, manages service lifecycle surrounding the processing of the intent,
         * which can take multiple milliseconds of CPU time. Due to this cost, startService()
         * should not be used for frequent intent delivery to a service, and only
         * for scheduling significant work.
         */
        context.startService(notificationService);
    }
}
