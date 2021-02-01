package com.project.personal_health_monitor.persistence.model;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "HealthParameterName"
)
public class HealthParameterName {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Long id;

    @ColumnInfo(name = "name")
    @NonNull
    public String name;

    @ColumnInfo(name = "priority")
    @NonNull
    public Integer priority;

    @Override
    public String toString() {
        return name;
    }

}
