package com.project.personal_health_monitor.dependency_injection;

import android.app.Application;

import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.project.personal_health_monitor.persistence.ApplicationDatabase;
import com.project.personal_health_monitor.persistence.dao.HealthParameterNames;
import com.project.personal_health_monitor.persistence.dao.HealthParameters;
import com.project.personal_health_monitor.persistence.dao.Reports;
import com.project.personal_health_monitor.persistence.model.HealthParameterName;
import com.project.personal_health_monitor.repository.HealthParameterNameRepository;
import com.project.personal_health_monitor.repository.HealthParameterRepository;
import com.project.personal_health_monitor.repository.ReportRepository;
import com.project.personal_health_monitor.view_model.ViewModelFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PersistenceModule {
    private final ApplicationDatabase applicationDatabase;

    public PersistenceModule(Application application) {
        applicationDatabase = Room
            .databaseBuilder(application, ApplicationDatabase.class, "database")
            .allowMainThreadQueries()
            .build();
    }

    @Provides @Singleton
    public ApplicationDatabase applicationDatabase() {
        return applicationDatabase;
    }

    @Provides @Singleton
    public HealthParameters healthParameters(ApplicationDatabase applicationDatabase) {
        return applicationDatabase.getHealthParameters();
    }

    @Provides @Singleton
    public HealthParameterNames healthParameterNames(ApplicationDatabase applicationDatabase) {
        return applicationDatabase.getHealthParameterNames();
    }

    @Provides @Singleton
    public Reports reports(ApplicationDatabase applicationDatabase) {
        return applicationDatabase.getReports();
    }

    @Provides @Singleton
    public HealthParameterRepository healthParameterRepository(HealthParameters healthParameters) {
        return new HealthParameterRepository(healthParameters);
    }

    @Provides @Singleton
    public HealthParameterNameRepository healthParameterNameRepository(HealthParameterNames healthParameterNames) {
        return new HealthParameterNameRepository(healthParameterNames);
    }

    @Provides @Singleton
    public ReportRepository reportRepository(Reports reports) {
        return new ReportRepository(reports);
    }

    @Provides @Singleton
    public ViewModelProvider.Factory viewModelFactory(HealthParameterRepository healthParameterRepository, HealthParameterNameRepository healthParameterNameRepository , ReportRepository reportRepository) {
        return new ViewModelFactory(healthParameterRepository, healthParameterNameRepository, reportRepository);
    }

}
