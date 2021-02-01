package com.project.personal_health_monitor.persistence.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity(tableName = "Report")
public class Report {
    
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Long id;

    @ColumnInfo(name = "local_date")
    @NonNull
    public LocalDate localDate;

    @ColumnInfo(name = "notes")
    public String notes;
    
}
