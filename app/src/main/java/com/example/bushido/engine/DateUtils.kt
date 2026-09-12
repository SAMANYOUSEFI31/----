package com.example.bushido.engine

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object DateUtils {
    private val isoFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    fun getLogicalTodayDate(nightOwlCutoffHour: Int = 4): String {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        // If before cutoff hour (e.g. 02:00 AM and cutoff is 4 AM), count as previous logical day
        if (currentHour < nightOwlCutoffHour) {
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }
        return isoFormat.format(calendar.time)
    }

    fun addDaysToDate(dateStr: String, days: Int): String {
        return try {
            val parsed = isoFormat.parse(dateStr) ?: Date()
            val calendar = Calendar.getInstance()
            calendar.time = parsed
            calendar.add(Calendar.DAY_OF_YEAR, days)
            isoFormat.format(calendar.time)
        } catch (e: Exception) {
            dateStr
        }
    }

    fun daysBetween(startDateStr: String, endDateStr: String): Int {
        return try {
            val start = isoFormat.parse(startDateStr) ?: return 0
            val end = isoFormat.parse(endDateStr) ?: return 0
            val diffMs = end.time - start.time
            TimeUnit.MILLISECONDS.toDays(diffMs).toInt()
        } catch (e: Exception) {
            0
        }
    }

    fun getDayLabelFa(dateStr: String, logicalToday: String = getLogicalTodayDate()): String {
        if (dateStr == logicalToday) {
            return "روز جاری نبرد"
        }
        val diff = daysBetween(logicalToday, dateStr)
        return when {
            diff == 1 -> "فردا (۱ روز بعد)"
            diff > 1 -> "${NumberUtils.toPersianDigits(diff)} روز بعد"
            diff == -1 -> "دیروز (۱ روز قبل)"
            else -> "${NumberUtils.toPersianDigits(kotlin.math.abs(diff))} روز قبل"
        }
    }
}
