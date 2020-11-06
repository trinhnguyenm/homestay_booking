package com.ctr.homestaybooking.ui.home.mybooking

import android.util.Log
import com.ctr.homestaybooking.base.BaseViewModel
import com.ctr.homestaybooking.data.source.PlaceRepository
import com.ctr.homestaybooking.data.source.response.MyBookingResponse
import com.ctr.homestaybooking.util.DateUtil
import com.ctr.homestaybooking.util.compareDay
import com.ctr.homestaybooking.util.parseToCalendar
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import java.util.*

class MyBookingViewModel(
    private val hotelRepository: PlaceRepository
) : MyBookingVMContract, BaseViewModel() {

    private val bookings = mutableListOf<MyBookingResponse.MyBooking>()

    private var rawBookings = listOf<MyBookingResponse.MyBooking>()

    override fun getBookings(): MutableList<MyBookingResponse.MyBooking> = bookings


    override fun filterMyBooking(filterDays: Int) {
        getBookings().apply {
            clear()
            addAll(rawBookings.filter {
                it.createDate.parseToCalendar(DateUtil.FORMAT_DATE_TIME_FROM_API_3).compareDay(
                    Calendar.getInstance()
                ).apply { Log.d("--=", "+${this}") } <= filterDays
            })
        }
    }

    override fun getBookingHistory(): Single<MyBookingResponse> {
        return hotelRepository.getBookingHistory()
            .doOnSuccess { response ->
                rawBookings = response.myBookings.sortedByDescending { it.id }
                getBookings().apply {
                    clear()
                    addAll(rawBookings)
                }
            }
    }

    override fun getProgressObservable(): BehaviorSubject<Boolean> =
        progressBarDialogStateObservable
}
