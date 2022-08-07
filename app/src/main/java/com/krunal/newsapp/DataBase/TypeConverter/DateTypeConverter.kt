package com.krunal.newsapp.DataBase.TypeConverter

import androidx.room.TypeConverter
import java.util.*

class DateTypeConverter {

    @TypeConverter
    fun toDate(date: Long?): Date? {
        return if (date == null) null else Date(date)
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}