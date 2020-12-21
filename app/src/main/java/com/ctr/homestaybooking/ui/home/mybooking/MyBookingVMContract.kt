package com.ctr.homestaybooking.ui.home.mybooking

import com.ctr.homestaybooking.data.model.BookingStatus
import com.ctr.homestaybooking.data.source.response.Booking
import com.ctr.homestaybooking.data.source.response.BookingHistoryResponse
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

interface MyBookingVMContract {
    fun getBookings(): MutableList<Booking>

    fun getBookingHistory(): Single<BookingHistoryResponse>

    fun getProgressObservable(): BehaviorSubject<Boolean>

    fun filterMyBooking(filterDays: Int, bookingStatus: BookingStatus?)
}
