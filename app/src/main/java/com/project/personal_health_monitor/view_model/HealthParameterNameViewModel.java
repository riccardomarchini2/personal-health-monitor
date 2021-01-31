package com.project.personal_health_monitor.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.project.personal_health_monitor.persistence.model.HealthParameter;
import com.project.personal_health_monitor.persistence.model.HealthParameterName;
import com.project.personal_health_monitor.repository.HealthParameterNameRepository;
import com.project.personal_health_monitor.repository.HealthParameterRepository;

import java.util.List;

import javax.inject.Inject;

public class HealthParameterNameViewModel extends ViewModel {
    private final HealthParameterNameRepository healthParameterNameRepository;

    @Inject
    public HealthParameterNameViewModel(HealthParameterNameRepository healthParameterNameRepository) {
        this.healthParameterNameRepository = healthParameterNameRepository;
    }

    public Long create(HealthParameterName healthParameterName) {
        return healthParameterNameRepository.create(healthParameterName);
    }

    public LiveData<List<HealthParameterName>> getAll() {
        return healthParameterNameRepository.getAll();
    }

    public void update(HealthParameterName healthParameterName) {
        healthParameterNameRepository.update(healthParameterName);
    }

    public void delete(HealthParameterName healthParameterName) {
        healthParameterNameRepository.delete(healthParameterName);
    }

}
