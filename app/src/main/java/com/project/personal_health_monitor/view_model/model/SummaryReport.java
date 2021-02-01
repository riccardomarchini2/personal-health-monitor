package com.project.personal_health_monitor.view_model.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SummaryReport {
    public LocalDate localDate;
    public List<SummaryHealthParameter> summaryHealthParameters;

    public SummaryReport(LocalDate localDate) {
        this.localDate = localDate;
        this.summaryHealthParameters = new ArrayList<>();
    }

    public SummaryHealthParameter getSummaryHealthParameter(String healthParameterName, Integer healthParameterPriority) {
        for (SummaryHealthParameter summaryHealthParameter : summaryHealthParameters) {
            if (Objects.equals(summaryHealthParameter.name, healthParameterName)) {
                return summaryHealthParameter;
            }
        }

        SummaryHealthParameter summaryHealthParameter = new SummaryHealthParameter(healthParameterName, healthParameterPriority);
        summaryHealthParameters.add(summaryHealthParameter);

        return summaryHealthParameter;
    }

}
