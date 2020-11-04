package com.ctr.homestaybooking.ui.home.mybooking

import com.ctr.homestaybooking.data.source.response.MyBookingResponse
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

interface MyBookingVMContract {
    fun getBookings(): MutableList<MyBookingResponse.MyBooking>

    fun getBookingHistory(): Single<MyBookingResponse>

    fun getProgressObservable(): BehaviorSubject<Boolean>

    fun filterMyBooking(filterDays: Int)
}
