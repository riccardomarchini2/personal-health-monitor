package com.project.personal_health_monitor.persistence.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.project.personal_health_monitor.persistence.model.HealthParameter;

import java.util.List;

@Dao
public interface HealthParameters {

    @Insert
    public void create(HealthParameter healthParameter);

    @Query("SELECT * FROM HealthParameter")
    public LiveData<List<HealthParameter>> getAll();

    @Update
    public void update(HealthParameter healthParameter);

    @Delete
    public void delete(HealthParameter healthParameter);

}
