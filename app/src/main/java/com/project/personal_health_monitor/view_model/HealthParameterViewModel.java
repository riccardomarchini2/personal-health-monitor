package com.project.personal_health_monitor.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.project.personal_health_monitor.persistence.model.HealthParameter;
import com.project.personal_health_monitor.repository.HealthParameterRepository;

import java.util.List;

import javax.inject.Inject;

public class HealthParameterViewModel extends ViewModel {
    private final HealthParameterRepository healthParameterRepository;

    @Inject
    public HealthParameterViewModel(HealthParameterRepository healthParameterRepository) {
        this.healthParameterRepository = healthParameterRepository;
    }

    public void create(HealthParameter healthParameter) {
        healthParameterRepository.create(healthParameter);
    }

    public LiveData<List<HealthParameter>> getAll() {
        return healthParameterRepository.getAll();
    }

    public void update(HealthParameter healthParameter) {
        healthParameterRepository.update(healthParameter);
    }

    public void delete(HealthParameter healthParameter) {
        healthParameterRepository.delete(healthParameter);
    }

}
