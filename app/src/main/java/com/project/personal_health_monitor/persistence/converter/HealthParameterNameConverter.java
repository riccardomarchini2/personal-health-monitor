package com.project.personal_health_monitor.persistence.converter;

import androidx.room.TypeConverter;

import com.project.personal_health_monitor.persistence.model.HealthParameterName;

import java.time.LocalDate;

public class HealthParameterNameConverter {

    @TypeConverter
    public static HealthParameterName getHealthParameterName(Integer ordinal) {
        for(HealthParameterName healthParameterName : HealthParameterName.values()) {
            if (healthParameterName.ordinal() == ordinal) {
                return healthParameterName;
            }
        }
        return null;
    }

    @TypeConverter
    public static Integer getOrdinal(HealthParameterName healthParameterName) {
        return healthParameterName.ordinal();
    }

}
