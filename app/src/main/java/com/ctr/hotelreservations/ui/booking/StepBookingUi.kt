package com.ctr.hotelreservations.ui.booking

/**
 * Created by at-trinhnguyen2 on 2020/06/16
 */
enum class StepBooking(val value: Int) {
    STEP_BOOKING(1),
    STEP_PAYMENT(3),
    STEP_CHECK_IN(2),
    STEP_REVIEW(4),
}

class StepBookingUI(val stepBooking: StepBooking,val title: String)
