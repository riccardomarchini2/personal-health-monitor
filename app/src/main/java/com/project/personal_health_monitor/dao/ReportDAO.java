package com.project.personal_health_monitor.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.project.personal_health_monitor.model.Report;

import java.time.LocalDate;
import java.util.List;

@Dao
public interface ReportDAO {

    @Query("SELECT * FROM Report, HealthParameter WHERE Report.id = HealthParameter.reportId AND Report.date = :date")
    public LiveData< List<Report> > getReports(LocalDate date);

}
