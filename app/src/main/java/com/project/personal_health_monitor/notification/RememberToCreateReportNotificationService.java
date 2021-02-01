package com.project.personal_health_monitor.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.ViewModelProvider;

import com.project.personal_health_monitor.PersonalHealthMonitor;
import com.project.personal_health_monitor.R;
import com.project.personal_health_monitor.view.MainActivity;
import com.project.personal_health_monitor.view.ReportActivity;
import com.project.personal_health_monitor.view_model.ReportViewModel;
import com.project.personal_health_monitor.view_model.ViewModelFactory;

import java.time.LocalDate;

import javax.inject.Inject;

public class RememberToCreateReportNotificationService extends LifecycleService {

    @Inject ViewModelFactory viewModelFactory;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PersonalHealthMonitor personalHealthMonitor = (PersonalHealthMonitor) this.getApplicationContext();
        ReportViewModel reportViewModel = personalHealthMonitor.applicationComponent().reportViewModel();

        reportViewModel.getBy(LocalDate.now()).observe(this, reportWithHealthParameters -> {
            if (reportWithHealthParameters.size() == 0) {
                sendNotification();
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

    private void sendNotification() {
        Context context = getApplicationContext();

        // Creates instance of Notification Channel
        NotificationChannel notificationChannel = new NotificationChannel(
            Notification.CHANNEL_NAME,
            Notification.CHANNEL_NAME,
            Notification.IMPORTANCE
        );

        // Creates instance of NotificationManager to get Notification's System Service
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);

        // Invokes channel creation's method
        notificationManager.createNotificationChannel(notificationChannel);

        // Creates new intent with params context and report activity class
        Intent reportActivity = new Intent(context, ReportActivity.class);

        /*
         * Flag indicating that if the described PendingIntent already exists,
         * then keep it but replace its extra data with what is in this new Intent.
         */
        PendingIntent contentIntent = PendingIntent.getActivity(context, 1, reportActivity, PendingIntent.FLAG_UPDATE_CURRENT);

        // Creates notification in the notification channel
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, Notification.CHANNEL_NAME)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle("You didn't create your report today")
            .setContentText("Click on the notification to start creating today's report")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(contentIntent)
            .setAutoCancel(true);

        // Post a notification to be shown in the status bar or update an existing one
        notificationManager.notify(Notification.NOTIFICATION_TYPE_IDS.get(NotificationType.REMEMBER_TO_CREATE_REPORT), notificationBuilder.build());

    }

}
