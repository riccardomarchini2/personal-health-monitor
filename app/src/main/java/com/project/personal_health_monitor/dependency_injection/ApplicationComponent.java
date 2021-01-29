package com.project.personal_health_monitor.dependency_injection;

import android.app.Application;

import com.project.personal_health_monitor.view.MainActivity2;

import javax.inject.Singleton;

import dagger.Component;

@Component( modules = { PersistenceModule.class, ApplicationModule.class } ) @Singleton
public interface ApplicationComponent {

    public Application application();

    // Activities
    void inject(MainActivity2 mainActivity2);

}
