package com.project.personal_health_monitor;

import android.app.Application;
import android.content.SharedPreferences;

import com.project.personal_health_monitor.dependency_injection.ApplicationComponent;
import com.project.personal_health_monitor.dependency_injection.ApplicationModule;
import com.project.personal_health_monitor.dependency_injection.DaggerApplicationComponent;
import com.project.personal_health_monitor.dependency_injection.PersistenceModule;
import com.project.personal_health_monitor.notification.Notification;
import com.project.personal_health_monitor.view.SettingsActivity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class PersonalHealthMonitor extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(new ApplicationModule(this))
            .persistenceModule(new PersistenceModule(this))
            .build();

        SharedPreferences sharedPreferences = getSharedPreferences(SettingsActivity.NAME, MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (!sharedPreferences.contains(SettingsActivity.REMEMBER_REPORT_HOUR) && !sharedPreferences.contains(SettingsActivity.REMEMBER_REPORT_HOUR)) {
            editor.putInt(SettingsActivity.REMEMBER_REPORT_HOUR, SettingsActivity.DEFAULT_REMEMBER_REPORT_HOUR);
            editor.putInt(SettingsActivity.REMEMBER_REPORT_MINUTE, SettingsActivity.DEFAULT_REMEMBER_REPORT_MINUTE);

            LocalTime localTime = LocalTime.of(SettingsActivity.DEFAULT_REMEMBER_REPORT_HOUR, SettingsActivity.DEFAULT_REMEMBER_REPORT_MINUTE);

            LocalDateTime nextAlarmDateTime = LocalDateTime.of(LocalDate.now(), localTime);
            if (nextAlarmDateTime.isBefore(LocalDateTime.now())) {
                nextAlarmDateTime = LocalDateTime.of(LocalDate.now().plusDays(1), localTime);
            }

            Notification.setRememberToCreateReportNotification(this, nextAlarmDateTime);
        }

        if (!sharedPreferences.contains(SettingsActivity.POSTPONE_BY)) {
            editor.putInt(SettingsActivity.POSTPONE_BY, SettingsActivity.DEFAULT_POSTPONE_BY);
        }

        editor.apply();
    }

    public ApplicationComponent applicationComponent() {
        return applicationComponent;
    }

}
