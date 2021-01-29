package com.project.personal_health_monitor.persistence.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ReportWithHealthParameters {

    @Embedded
    public Report report;

    @Relation(
        parentColumn = "id",
        entityColumn = "report_id"
    )
    public List<HealthParameter> healthParameters;

}
