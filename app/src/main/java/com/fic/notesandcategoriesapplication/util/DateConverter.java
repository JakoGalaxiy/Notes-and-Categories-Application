package com.fic.notesandcategoriesapplication.util;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {
    // Convierte Long (TIMESTAMP) a Date
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    // Convierte Date a Long (TIMESTAMP)
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}