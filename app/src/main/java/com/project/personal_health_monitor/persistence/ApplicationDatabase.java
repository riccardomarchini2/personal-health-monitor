package com.project.personal_health_monitor.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.project.personal_health_monitor.persistence.converter.HealthParameterNameConverter;
import com.project.personal_health_monitor.persistence.converter.LocalDateConverter;
import com.project.personal_health_monitor.persistence.dao.HealthParameterNames;
import com.project.personal_health_monitor.persistence.dao.HealthParameters;
import com.project.personal_health_monitor.persistence.dao.Reports;
import com.project.personal_health_monitor.persistence.model.HealthParameter;
import com.project.personal_health_monitor.persistence.model.Report;

@Database(entities = { HealthParameter.class, Report.class }, version = 1, exportSchema = false)
@TypeConverters( { LocalDateConverter.class, HealthParameterNameConverter.class } )
public abstract class ApplicationDatabase extends RoomDatabase {

    public abstract HealthParameters getHealthParameters();
    public abstract Reports getReports();

    public abstract HealthParameterNames getHealthParameterNames();
}
