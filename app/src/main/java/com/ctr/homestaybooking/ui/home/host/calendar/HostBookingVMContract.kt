package com.ctr.homestaybooking.ui.home.host.calendar

import com.ctr.homestaybooking.data.source.response.Booking
import com.ctr.homestaybooking.data.source.response.BookingHistoryResponse
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

interface HostBookingVMContract {
    fun getBookings(): MutableList<Booking>

    fun getBookingHistory(): Single<BookingHistoryResponse>

    fun getProgressObservable(): BehaviorSubject<Boolean>

    fun filterMyBooking(filterDays: Int)
}
