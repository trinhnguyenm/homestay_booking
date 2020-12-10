package com.ctr.homestaybooking.ui.home.host.calendar

import com.ctr.homestaybooking.base.BaseViewModel
import com.ctr.homestaybooking.data.source.LocalRepository
import com.ctr.homestaybooking.data.source.PlaceRepository
import com.ctr.homestaybooking.data.source.response.Booking
import com.ctr.homestaybooking.data.source.response.BookingHistoryResponse
import com.ctr.homestaybooking.util.compareDay
import com.ctr.homestaybooking.util.toCalendar
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import java.util.*

class HostBookingViewModel(
    private val placeRepository: PlaceRepository,
    private val localRepository: LocalRepository
) : HostBookingVMContract, BaseViewModel() {

    private val bookings = mutableListOf<Booking>()

    private var rawBookings = listOf<Booking>()

    override fun getBookings(): MutableList<Booking> = bookings

    override fun filterMyBooking(filterDays: Int) {
        getBookings().apply {
            clear()
            addAll(rawBookings.filter {
                it.createDate.toCalendar().compareDay(Calendar.getInstance()) <= filterDays
            })
        }
    }

    override fun getBookingHistory(): Single<BookingHistoryResponse> {
        return placeRepository.getBookingByHostId(localRepository.getUserId())
            .doOnSuccess { response ->
                rawBookings = response.bookings.sortedByDescending { it.id }
                getBookings().apply {
                    clear()
                    addAll(rawBookings)
                }
            }
    }

    override fun getProgressObservable(): BehaviorSubject<Boolean> =
        progressBarDialogObservable
}
