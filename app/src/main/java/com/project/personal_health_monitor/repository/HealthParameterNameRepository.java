package com.project.personal_health_monitor.repository;

import androidx.lifecycle.LiveData;

import com.project.personal_health_monitor.persistence.dao.HealthParameterNames;
import com.project.personal_health_monitor.persistence.dao.HealthParameters;
import com.project.personal_health_monitor.persistence.model.HealthParameter;
import com.project.personal_health_monitor.persistence.model.HealthParameterName;

import java.util.List;

import javax.inject.Inject;

public class HealthParameterNameRepository {

    private final HealthParameterNames healthParameterNames;

    @Inject
    public HealthParameterNameRepository(HealthParameterNames healthParameterNames) {
        this.healthParameterNames = healthParameterNames;
    }

    public Long create(HealthParameterName healthParameterName) {
        return healthParameterNames.create(healthParameterName);
    }

    public LiveData<List<HealthParameterName>> getAll() {
        return healthParameterNames.getAll();
    }

    public void update(HealthParameterName healthParameterName) {
        healthParameterNames.update(healthParameterName);
    }

    public void delete(HealthParameterName healthParameterName) {
        healthParameterNames.delete(healthParameterName);
    }
}
