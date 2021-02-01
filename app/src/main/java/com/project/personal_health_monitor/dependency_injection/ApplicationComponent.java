package com.project.personal_health_monitor.dependency_injection;

import android.app.Application;

import com.project.personal_health_monitor.notification.RememberToCreateReportNotificationService;
import com.project.personal_health_monitor.view.SettingsActivity;
import com.project.personal_health_monitor.view.graphs.GraphsFragment;
import com.project.personal_health_monitor.view.health_parameters.HealthParametersFragment;
import com.project.personal_health_monitor.view.home.HomeFragment;
import com.project.personal_health_monitor.view.ReportActivity;
import com.project.personal_health_monitor.view.reports.ReportsFragment;
import com.project.personal_health_monitor.view_model.HealthParameterNameViewModel;
import com.project.personal_health_monitor.view_model.HealthParameterViewModel;
import com.project.personal_health_monitor.view_model.ReportViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Component( modules = { PersistenceModule.class, ApplicationModule.class } ) @Singleton
public interface ApplicationComponent {

    public Application application();

    public HealthParameterNameViewModel healthParameterNameViewModel();
    public HealthParameterViewModel healthParameterViewModel();
    public ReportViewModel reportViewModel();

    // Activities
    void inject(ReportActivity reportActivity);
    void inject(SettingsActivity settingsActivity);

    // Fragments
    void inject(HomeFragment homeFragment);
    void inject(HealthParametersFragment healthParametersFragment);
    void inject(ReportsFragment reportsFragment);
    void inject(GraphsFragment graphsFragment);

    // Services
    void inject(RememberToCreateReportNotificationService rememberToCreateReportNotificationService);

}
