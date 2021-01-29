package com.project.personal_health_monitor.repository;

import androidx.lifecycle.LiveData;

import com.project.personal_health_monitor.persistence.dao.HealthParameters;
import com.project.personal_health_monitor.persistence.model.HealthParameter;

import java.util.List;

import javax.inject.Inject;

public class HealthParameterRepository {
    private final HealthParameters healthParameters;

    @Inject
    public HealthParameterRepository(HealthParameters healthParameters) {
        this.healthParameters = healthParameters;
    }

    public void create(HealthParameter healthParameter) {
        healthParameters.create(healthParameter);
    }

    public LiveData<List<HealthParameter>> getAll() {
        return healthParameters.getAll();
    }

    public void update(HealthParameter healthParameter) {
        healthParameters.update(healthParameter);
    }

    public void delete(HealthParameter healthParameter) {
        healthParameters.delete(healthParameter);
    }

}
