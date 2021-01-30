package com.project.personal_health_monitor.dependency_injection;

import android.app.Application;

import com.project.personal_health_monitor.view.graphs.GraphsFragment;
import com.project.personal_health_monitor.view.home.HomeFragment;
import com.project.personal_health_monitor.view.report.ReportActivity;

import javax.inject.Singleton;

import dagger.Component;

@Component( modules = { PersistenceModule.class, ApplicationModule.class } ) @Singleton
public interface ApplicationComponent {

    public Application application();

    // Fragments
    void inject(HomeFragment homeFragment);

    void inject(GraphsFragment graphsFragment);

    void inject(ReportActivity reportActivity);
}
