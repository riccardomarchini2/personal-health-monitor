package com.project.personal_health_monitor.persistence.converter;

import androidx.room.TypeConverter;

import java.time.LocalDate;

public class LocalDateConverter {

    @TypeConverter
    public static LocalDate getLocalDate(Long value) {
        return LocalDate.ofEpochDay(value);
    }

    @TypeConverter
    public static Long getLong(LocalDate value) {
        return value.toEpochDay();
    }

}
