package com.ctr.homestaybooking.ui.wedget

import android.app.DatePickerDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.extension.onClickDelayAction
import com.ctr.homestaybooking.ui.App
import kotlinx.android.synthetic.main.dialog_birthday_selection.*
import kotlinx.android.synthetic.main.dialog_birthday_selection.view.*

/**
 * Created by at-cuongcao on 25/08/2020.
 */
class CustomDatePickerDialog(
    context: Context,
    internal var title: String,
    private val callBack: DatePickerDialog.OnDateSetListener,
    year: Int,
    monthOfYear: Int,
    dayOfMonth: Int,
    isHideDay: Boolean = false
) : AlertDialog(context, R.style.AlertDialog), DatePicker.OnDateChangedListener {

    companion object {
        private const val DEFAULT_MAX_YEAR = 2100
    }

    private var minYear = 0
    private var minMonthOfYear = 0
    private var minDayOfMonth = 0
    private var maxYear = DEFAULT_MAX_YEAR
    private var maxMonthOfYear = 0
    private var maxDayOfMonth = 0
    private var lastYear = year
    private var lastMonth = monthOfYear
    private var lastDayOfMonth = dayOfMonth
    private var isHideDay = isHideDay

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_birthday_selection, null)
        view.datePicker.init(lastYear, lastMonth, lastDayOfMonth, this)
        setNumberPickerDividerColour(
            view.datePicker.findViewById(
                Resources.getSystem().getIdentifier("year", "id", "android")
            )
        )
        setNumberPickerDividerColour(
            view.datePicker.findViewById(
                Resources.getSystem().getIdentifier("month", "id", "android")
            )
        )
        setNumberPickerDividerColour(
            view.datePicker.findViewById(
                Resources.getSystem().getIdentifier("day", "id", "android")
            )
        )
        view.datePicker.descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS
        setView(view)
        setCancelable(false)
    }

    override fun show() {
        super.show()
        (context.applicationContext as? App)?.currentActivity?.currentFocus?.clearFocus()
        hideDayInPickerDialog(isHideDay)
        tvTitle.text = title
        datePicker.updateDate(lastYear, lastMonth, lastDayOfMonth)
        tvOk.onClickDelayAction {
            callBack.onDateSet(datePicker, lastYear, lastMonth, lastDayOfMonth)
            dismiss()
        }
        tvCancel.onClickDelayAction {
            dismiss()
        }
    }

    internal fun setMinDate(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        minYear = year
        minMonthOfYear = monthOfYear
        minDayOfMonth = dayOfMonth
    }

    internal fun setMaxDate(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        maxYear = year
        maxMonthOfYear = monthOfYear
        maxDayOfMonth = dayOfMonth
    }

    internal fun updateDate(year: Int, month: Int, day: Int) {
        lastYear = year
        lastMonth = month
        lastDayOfMonth = day
    }

    private fun beforeMinDate(year: Int, monthOfYear: Int, dayOfMonth: Int): Boolean {
        if (year < minYear) {
            return true
        }
        if (year == minYear && monthOfYear < minMonthOfYear) {
            return true
        }
        if (year == minYear && monthOfYear == minMonthOfYear && dayOfMonth < minDayOfMonth) {
            return true
        }
        return false
    }

    private fun afterMaxDate(year: Int, monthOfYear: Int, dayOfMonth: Int): Boolean {
        if (year > maxYear) {
            return true
        }
        if (year == maxYear && monthOfYear > maxMonthOfYear) {
            return true
        }
        if (year == maxYear && monthOfYear == maxMonthOfYear && dayOfMonth > maxDayOfMonth) {
            return true
        }
        return false
    }

    private fun hideDayInPickerDialog(value: Boolean) {
        if (value) {
            datePicker.findViewById<ViewGroup>(
                Resources.getSystem().getIdentifier(
                    "day",
                    "id",
                    "android"
                )
            )
                .visibility = View.GONE
        }
    }

    override fun onDateChanged(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        when {
            beforeMinDate(year, month, dayOfMonth) -> when {
                dayOfMonth != lastDayOfMonth -> datePicker.updateDate(
                    year,
                    month,
                    dayOfMonth + 1
                )

                month != lastMonth -> datePicker.init(year, month + 1, dayOfMonth, this)

                year != lastYear -> datePicker.init(year + 1, month, dayOfMonth, this)
            }
            afterMaxDate(year, month, dayOfMonth) -> when {
                dayOfMonth != lastDayOfMonth -> datePicker.updateDate(
                    year,
                    month,
                    dayOfMonth - 1
                )

                month != lastMonth -> datePicker.updateDate(
                    year,
                    month - 1,
                    dayOfMonth
                )

                year != lastYear -> datePicker.updateDate(
                    year - 1,
                    month,
                    dayOfMonth
                )
            }
            else -> {
                lastYear = year
                lastMonth = month
                lastDayOfMonth = dayOfMonth
            }
        }
    }

    private fun setNumberPickerDividerColour(number_picker: NumberPicker) {
        val count = number_picker.childCount
        for (i in 0 until count) {
            try {
                val dividerField =
                    number_picker.javaClass.getDeclaredField("mSelectionDivider")
                dividerField.isAccessible = true
                val colorDrawable =
                    ColorDrawable(context.resources.getColor(R.color.colorAzureRadiance))
                dividerField.set(number_picker, colorDrawable)
                number_picker.invalidate()
            } catch (e: Exception) {
            }
        }
    }
}
