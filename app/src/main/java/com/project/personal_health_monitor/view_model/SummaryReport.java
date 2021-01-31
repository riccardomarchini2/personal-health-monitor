package com.project.personal_health_monitor.view_model;

import com.project.personal_health_monitor.persistence.model.HealthParameterName;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SummaryReport {
    public LocalDate date;
    public List<SummaryHealthParameter> summaryHealthParameters;

    public SummaryReport(LocalDate date) {
        this.date = date;
        this.summaryHealthParameters = new ArrayList<>();
    }

    public SummaryHealthParameter getSummaryHealthParameter(HealthParameterName name) {
        for (SummaryHealthParameter summaryHealthParameter : summaryHealthParameters) {
            if (summaryHealthParameter.name == name) {
                return summaryHealthParameter;
            }
        }

        SummaryHealthParameter summaryHealthParameter = new SummaryHealthParameter(name);
        summaryHealthParameters.add(summaryHealthParameter);

        return summaryHealthParameter;
    }

    public String getSummaryHealthParameterValue(HealthParameterName name) {
        for (SummaryHealthParameter summaryHealthParameter : summaryHealthParameters) {
            if (summaryHealthParameter.name == name) {
                return String.valueOf(summaryHealthParameter.getAverageValue());
            }
        }
        return "-";
    }
}
