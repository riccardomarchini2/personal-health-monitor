package com.project.personal_health_monitor.dependency_injection;

import android.app.Application;

import com.project.personal_health_monitor.view.graphs.GraphsFragment;
import com.project.personal_health_monitor.view.health_parameters.HealthParametersFragment;
import com.project.personal_health_monitor.view.home.HomeFragment;
import com.project.personal_health_monitor.view.ReportActivity;
import com.project.personal_health_monitor.view.reports.ReportsFragment;

import javax.inject.Singleton;

import dagger.Component;

@Component( modules = { PersistenceModule.class, ApplicationModule.class } ) @Singleton
public interface ApplicationComponent {

    public Application application();

    // Activities
    void inject(ReportActivity reportActivity);

    // Fragments
    void inject(HomeFragment homeFragment);
    void inject(HealthParametersFragment healthParametersFragment);
    void inject(ReportsFragment reportsFragment);
    void inject(GraphsFragment graphsFragment);

}
