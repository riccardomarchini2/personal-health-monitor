package com.project.personal_health_monitor.notification;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleService;

public class PostponeService extends LifecycleService {

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Context context = getApplicationContext();

        Notification.setPostponeRememberToCreateReportNotification(context);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(Notification.NOTIFICATION_TYPE_IDS.get(NotificationType.REMEMBER_TO_CREATE_REPORT));

        return super.onStartCommand(intent, flags, startId);
    }
}
