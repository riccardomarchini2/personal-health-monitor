package com.project.personal_health_monitor.view_model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.project.personal_health_monitor.repository.HealthParameterRepository;
import com.project.personal_health_monitor.repository.ReportRepository;

import javax.inject.Inject;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private final HealthParameterRepository healthParameterRepository;
    private final ReportRepository reportRepository;

    @Inject
    public ViewModelFactory(HealthParameterRepository healthParameterRepository, ReportRepository reportRepository) {
        this.healthParameterRepository = healthParameterRepository;
        this.reportRepository = reportRepository;
    }

    @Override @NonNull
    public <T extends ViewModel> T create(@NonNull Class<T> viewModel) {
        if (viewModel.isAssignableFrom(HealthParameterViewModel.class)) {
            return (T) new HealthParameterViewModel(healthParameterRepository);
        } else if (viewModel.isAssignableFrom(ReportViewModel.class)) {
            return (T) new ReportViewModel(reportRepository);
        }

        throw new IllegalArgumentException("ViewModel not found.");
    }

}
