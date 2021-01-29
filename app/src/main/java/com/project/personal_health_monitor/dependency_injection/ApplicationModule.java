package com.project.personal_health_monitor.dependency_injection;

import android.app.Application;

import com.project.personal_health_monitor.PersonalHealthMonitor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides @Singleton
    public Application application() {
        return application;
    }

}
