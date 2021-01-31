package com.project.personal_health_monitor.view_model.model;

import java.util.ArrayList;
import java.util.List;

public class SummaryHealthParameter {
    public String name;
    public Integer priority;
    public List<Double> values;

    public SummaryHealthParameter(String name, Integer priority) {
        this.name = name;
        this.priority = priority;
        this.values = new ArrayList<>();
    }

    public Double getAverageValue() {
        return values.stream().mapToDouble(value -> value).average().orElse(0);
    }
}
