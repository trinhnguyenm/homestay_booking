package com.ctr.homestaybooking.ui.wedget

import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.data.model.BookingType
import com.ctr.homestaybooking.data.model.CancelType
import com.ctr.homestaybooking.data.model.Gender
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
        internal val GENDER = mutableListOf<CustomSpinner.SpinnerItem>(
            SpinnerType(
                App.instance.getString(R.string.gender_male),
                Gender.MALE.name
            ),
            SpinnerType(
                App.instance.getString(R.string.gender_female),
                Gender.FEMALE.name
            ),
            SpinnerType(
                App.instance.getString(R.string.gender_other),
                Gender.OTHER.name
            )
        )
        internal val CANCER_TYPE = mutableListOf<CustomSpinner.SpinnerItem>(
            SpinnerType(
                "Linh hoạt",
                CancelType.FLEXIBLE.name
            ),
            SpinnerType(
                "Trung bình",
                CancelType.MODERATE.name
            ),
            SpinnerType(
                "Nghiêm ngặt",
                CancelType.STRICT.name
            )
        )
        internal val HOURS = (1..24).map {
            SpinnerType(
                "$it : 00",
                "$it"
            )
        }
    }

    override fun getText() = name

    override fun getCode() = code
}
