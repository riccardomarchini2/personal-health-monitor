package com.project.personal_health_monitor;

import android.app.Application;

import com.project.personal_health_monitor.dependency_injection.ApplicationComponent;
import com.project.personal_health_monitor.dependency_injection.ApplicationModule;
import com.project.personal_health_monitor.dependency_injection.DaggerApplicationComponent;
import com.project.personal_health_monitor.dependency_injection.PersistenceModule;

public class PersonalHealthMonitor extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(new ApplicationModule(this))
            .persistenceModule(new PersistenceModule(this))
            .build();
    }

    public ApplicationComponent applicationComponent() {
        return applicationComponent;
    }

}
