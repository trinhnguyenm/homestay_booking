package com.ctr.homestaybooking.ui.booking

import com.ctr.homestaybooking.base.BaseViewModel
import com.ctr.homestaybooking.data.model.BookingStatus
import com.ctr.homestaybooking.data.source.LocalRepository
import com.ctr.homestaybooking.data.source.PlaceRepository
import com.ctr.homestaybooking.data.source.UserRepository
import com.ctr.homestaybooking.data.source.request.BookingBody
import com.ctr.homestaybooking.data.source.response.BookingResponse
import com.ctr.homestaybooking.data.source.response.ChangeRoomReservationStatusResponse
import com.ctr.homestaybooking.data.source.response.UserResponse
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by at-trinhnguyen2 on 2020/06/21
 */

class BookingViewModel(
    private var localRepository: LocalRepository,
    private val placeRepository: PlaceRepository,
    private val userRepository: UserRepository
) : BookingVMContract, BaseViewModel() {

    private val bookingBody = BookingBody()

    override fun getBookingBody() = bookingBody

    override fun addBooking(): Single<BookingResponse> {
        return placeRepository.addBooking(getBookingBody())
            .addProgressLoading()
    }

    override fun getUserId() = localRepository.getUserId()

    override fun getUserInfo(): Single<UserResponse> {
        return userRepository.getUserFollowId(localRepository.getUserId())
            .addProgressLoading()
    }

    override fun changeBookingStatus(
        bookingId: Int,
        bookingStatus: BookingStatus
    ): Single<BookingResponse> {
        return placeRepository.changeBookingStatus(bookingId, bookingStatus)
            .addProgressLoading()
    }

    override fun changeRoomReservationStatus(roomReservationId: Int): Single<ChangeRoomReservationStatusResponse> {
        return placeRepository.changeRoomReservationStatus(roomReservationId)
            .addProgressLoading()
    }

    override fun getProgressObservable(): BehaviorSubject<Boolean> =
        progressBarDialogStateObservable
}
