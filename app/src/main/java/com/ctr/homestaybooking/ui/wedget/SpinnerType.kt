package com.ctr.homestaybooking.ui.wedget

import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.data.model.BookingType
import com.ctr.homestaybooking.ui.App

data class SpinnerType(private val name: String, private val code: String = "") :
    CustomSpinner.SpinnerItem {

    companion object {
        internal val BOOKING_TYPE = mutableListOf<CustomSpinner.SpinnerItem>(
            SpinnerType(
                App.instance.getString(R.string.instant_book),
                BookingType.INSTANT_BOOKING.name
            ),
            SpinnerType(
                App.instance.getString(R.string.request_booking),
                BookingType.REQUEST_BOOKING.name
            )
        )
    }

    override fun getText() = name

    override fun getCode() = code
}
