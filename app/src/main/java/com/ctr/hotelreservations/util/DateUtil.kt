package com.ctr.hotelreservations.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by at-chauhoang on 11/06/2019.
 */
object DateUtil {
    internal const val FORMAT_DATE_TIME_FROM_API = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    internal const val FORMAT_DATE_TIME = "yyyy/MM/dd"
    internal const val FORMAT_DATE_TIME_CHECK_IN = "dd/MM/yyyy"
    internal const val FORMAT_DATE_TIME_DAY_IN_WEEK = "EEEE"
    internal const val ONE_HOUR = 1000 * 60 * 60

    internal fun parse(targetString: String, format: String): Calendar {
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        try {
            val calendar = Calendar.getInstance()
            calendar.time = formatter.parse(targetString)
            return calendar
        } catch (e: ParseException) {
            throw IllegalArgumentException("non-parsable date format. target: $targetString, format: $format")
        }
    }

    internal fun format(calendar: Calendar, format: String): String {
        val resultFormat = SimpleDateFormat(format, Locale.getDefault())
        return resultFormat.format(calendar.time)
    }

    internal fun formatToUTC(calendar: Calendar, format: String): String {
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        return convertTimeLongToStringWithGMT(calendar.timeInMillis, format)
    }

    internal fun convertDateUTCToLocalDate(targetString: String, format: String): Date {
        try {
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            return sdf.parse(targetString)
        } catch (e: Exception) {
            throw IllegalArgumentException("non-parsable date format. target: $targetString, format: $format")
        }
    }

    internal fun convertTimeFromApi(
        time: String,
        format: String = FORMAT_DATE_TIME_FROM_API
    ): String {
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        try {
            val calendar = Calendar.getInstance()
            calendar.time = formatter.parse(time)
            formatter.timeZone = TimeZone.getTimeZone("Japan")
            return formatter.format(calendar.time)
        } catch (e: ParseException) {
            throw IllegalArgumentException("non-parsable date format. target: $time, format: $format")
        }
    }

    internal fun convertTimeToUTC(
        time: String,
        format: String = FORMAT_DATE_TIME_FROM_API
    ): String {
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("Japan")
        try {
            val calendar = Calendar.getInstance()
            calendar.time = formatter.parse(time)
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            return formatter.format(calendar.time)
        } catch (e: ParseException) {
            throw IllegalArgumentException("non-parsable date format. target: $time, format: $format")
        }
    }

    private fun convertTimeLongToStringWithGMT(timeInMillis: Long, format: String): String {
        try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.timeZone = TimeZone.getTimeZone("GMT")
            return df.format(Date(timeInMillis)).toString()
        } catch (e: Exception) {
            throw IllegalArgumentException(e.message)
        }
    }
}
