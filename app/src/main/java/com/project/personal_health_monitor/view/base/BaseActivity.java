package com.project.personal_health_monitor.view.base;

import androidx.appcompat.app.AppCompatActivity;

import com.project.personal_health_monitor.PersonalHealthMonitor;

public abstract class BaseActivity extends AppCompatActivity {

    public PersonalHealthMonitor personalHealthMonitor() {
        return (PersonalHealthMonitor) getApplication();
    }

}
