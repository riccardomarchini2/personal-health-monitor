package com.project.personal_health_monitor.persistence.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.project.personal_health_monitor.persistence.model.Report;
import com.project.personal_health_monitor.persistence.model.ReportWithHealthParameters;

import java.time.LocalDate;
import java.util.List;

@Dao
public interface Reports {

    @Insert
    public Long create(Report report);

    @Query("SELECT * FROM Report") @Transaction
    public LiveData<List<ReportWithHealthParameters>> getAll();

    @Query("SELECT * FROM Report WHERE Report.date = :localDate") @Transaction
    public LiveData<List<ReportWithHealthParameters>> getBy(LocalDate localDate);

    @Update
    public void update(Report report);

    @Delete
    public void delete(Report report);

}
