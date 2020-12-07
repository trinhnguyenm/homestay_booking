package com.ctr.homestaybooking.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

/**
 * Created by at-chauhoang on 11/06/2019.
 */
object DateUtil {
    internal const val FORMAT_DATE = "yyyy/MM/dd"
    internal const val FORMAT_DATE_DAY_MONTH = "dd/MM"
    internal const val FORMAT_DATE_TIME_CHECK_IN_BOOKING = "dd MMM, yyyy"
    internal const val FORMAT_DATE_TIME_DAY_IN_WEEK = "EEEE"
    internal const val ONE_HOUR = 1000 * 60 * 60
}

internal const val FORMAT_DATE_API = "yyyy-MM-dd"
internal const val FORMAT_TIME_API = "'T'HH:mm:ss.SSSz"
internal const val FORMAT_DATE_TIME_API = "yyyy-MM-dd'T'HH:mm:ss.SSSz"

internal fun String.convert(
    outputFormat: String,
    inputFormat: String = FORMAT_DATE_TIME_API
): String {
    return this.toCalendar(inputFormat).format(outputFormat)
}

internal fun String.toCalendar(format: String = FORMAT_DATE_TIME_API): Calendar {
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    try {
        val calendar = Calendar.getInstance()
        calendar.time = formatter.parse(this)
        return calendar
    } catch (e: ParseException) {
        throw IllegalArgumentException("non-parsable date format. target: $this, format: $format")
    }
}

internal fun String.toDate(format: String = FORMAT_DATE_API): Date {
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    try {
        return formatter.parse(this)
    } catch (e: ParseException) {
        throw IllegalArgumentException("non-parsable date format. target: $this, format: $format")
    }
}

internal fun Calendar.format(format: String = FORMAT_DATE_TIME_API): String {
    val resultFormat = SimpleDateFormat(format, Locale.getDefault())
    return resultFormat.format(time)
}

internal fun Date.format(format: String = FORMAT_DATE_TIME_API): String {
    val resultFormat = SimpleDateFormat(format, Locale.getDefault())
    return resultFormat.format(time)
}

internal fun Iterable<Date>.isContain(date: Date): Boolean {
    forEach {
        if (it.format(FORMAT_DATE_API) == date.format(FORMAT_DATE_API)) return true
    }
    return false
}

fun Iterable<Date>.isContainAll(dates: Iterable<Date>): Boolean {
    dates.forEach {
        if (!isContain(it)) return false
    }
    return true
}

internal fun getDate(year: Int, month: Int, day: Int): Date {
    val calendar = Calendar.getInstance()
    calendar[Calendar.YEAR] = year
    calendar[Calendar.MONTH] = month
    calendar[Calendar.DAY_OF_MONTH] = day
    calendar[Calendar.HOUR_OF_DAY] = 0
    calendar[Calendar.MINUTE] = 0
    calendar[Calendar.SECOND] = 0
    calendar[Calendar.MILLISECOND] = 0
    return calendar.time
}

internal fun Date.addDays(days: Int): Date {
    val cal = Calendar.getInstance()
    cal.time = this
    cal.add(Calendar.DATE, days)
    return cal.time
}

internal fun Calendar?.compareDay(otherDate: Calendar?): Int {
    if (this == null || otherDate == null) return -1
    return ((abs(this.timeInMillis - otherDate.timeInMillis) + 2 * DateUtil.ONE_HOUR) / (24 * DateUtil.ONE_HOUR)).toInt()
}

fun Calendar?.datesUntil(endCalendar: Calendar?): MutableList<Date> {
    if (this == null || endCalendar == null) return mutableListOf()
    val calendar = Calendar.getInstance()
    calendar.time = this.time
    val dates = mutableListOf<Date>()
    while (calendar.before(endCalendar)) {
        dates.add(this.time)
        calendar.add(Calendar.DATE, 1)
    }
    return dates
}
