package com.project.personal_health_monitor.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.project.personal_health_monitor.dao.ReportDAO;
import com.project.personal_health_monitor.model.HealthParameter;
import com.project.personal_health_monitor.model.Report;

@Database(entities = { HealthParameter.class, Report.class }, version = 1, exportSchema = false)
public abstract class ApplicationDatabase extends RoomDatabase {

    public abstract ReportDAO getReportDAO();

}
