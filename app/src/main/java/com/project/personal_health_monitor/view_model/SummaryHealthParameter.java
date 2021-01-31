package com.project.personal_health_monitor.view_model;

import com.project.personal_health_monitor.persistence.model.HealthParameterName;

import java.util.ArrayList;
import java.util.List;

public class SummaryHealthParameter {
    public HealthParameterName name;
    public List<Long> values;

    public SummaryHealthParameter(HealthParameterName name) {
        this.name = name;
        this.values = new ArrayList<>();
    }

    public Double getAverageValue() {
        if (values.size() == 0) {
            return (double) 0;
        } else {
            Long sum = 0L;
            for (Long value : values) {
                sum += value;
            }
            return (double) sum / (double) values.size();
        }
    }
}
