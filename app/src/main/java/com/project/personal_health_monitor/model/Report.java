package com.project.personal_health_monitor.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Report {
    
    @PrimaryKey(autoGenerate = true)
    public Long id;
    public List<HealthParameter> healthParameters;
    public LocalDate date;
    
}
