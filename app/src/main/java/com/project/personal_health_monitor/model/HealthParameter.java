package com.project.personal_health_monitor.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "HealthParameter", 
    foreignKeys = @ForeignKey(entity = Report.class, childColumns = "reportId", parentColumns = "id")
)
public class HealthParameter {
    
    @PrimaryKey(autoGenerate = true)
    public Long id;
    
    @ColumnInfo(name = "reportId")
    public Long reportId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "value")
    public String value;

    @ColumnInfo(name = "notes")
    public String notes;
    
}