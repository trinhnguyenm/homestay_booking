package com.ctr.homestaybooking.ui.booking

import com.ctr.homestaybooking.data.source.request.BookingBody
import com.ctr.homestaybooking.data.source.response.BookingResponse
import com.ctr.homestaybooking.data.source.response.ChangeReservationStatusResponse
import com.ctr.homestaybooking.data.source.response.ChangeRoomReservationStatusResponse
import com.ctr.homestaybooking.data.source.response.UserResponse
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by at-trinhnguyen2 on 2020/06/21
 */
interface BookingVMContract {

    fun getProgressObservable(): BehaviorSubject<Boolean>

    fun getUserInfo(): Single<UserResponse>

    fun getUserId(): Int

    fun changeReservationStatus(reservationId: Int): Single<ChangeReservationStatusResponse>

    fun changeRoomReservationStatus(roomReservationId: Int): Single<ChangeRoomReservationStatusResponse>

    fun getBookingBody(): BookingBody

    fun addBooking(): Single<BookingResponse>
}
