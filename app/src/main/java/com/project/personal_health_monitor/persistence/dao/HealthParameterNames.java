package com.project.personal_health_monitor.persistence.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.project.personal_health_monitor.persistence.model.HealthParameter;
import com.project.personal_health_monitor.persistence.model.HealthParameterName;

import java.util.List;

@Dao
public interface HealthParameterNames {

    @Insert
    public Long create(HealthParameterName healthParameterName);

    @Query("SELECT * FROM HealthParameterName")
    public LiveData<List<HealthParameterName>> getAll();

    @Update
    public void update(HealthParameterName healthParameterName);

    @Delete
    public void delete(HealthParameterName healthParameterName);
}
