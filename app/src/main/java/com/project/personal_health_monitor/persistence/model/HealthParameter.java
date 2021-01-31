package com.project.personal_health_monitor.persistence.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "HealthParameter",
    foreignKeys = {@ForeignKey(entity = Report.class,
            parentColumns = "id",
            childColumns = "report_id",
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
    ), @ForeignKey(entity = HealthParameterName.class,
            parentColumns = "id",
            childColumns = "healthParameterName_id",
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
    )}
)
public class HealthParameter {
    
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Long id;
    
    @ColumnInfo(name = "report_id")
    @NonNull
    public Long reportId;

    @Embedded
    public HealthParameterName name;

    @ColumnInfo(name = "value")
    @NonNull
    public Long value;
    
}