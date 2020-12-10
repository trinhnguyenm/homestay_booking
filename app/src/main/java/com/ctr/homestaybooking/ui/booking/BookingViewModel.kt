package com.ctr.homestaybooking.ui.booking

import com.ctr.homestaybooking.base.BaseViewModel
import com.ctr.homestaybooking.data.model.BookingStatus
import com.ctr.homestaybooking.data.source.LocalRepository
import com.ctr.homestaybooking.data.source.PlaceRepository
import com.ctr.homestaybooking.data.source.UserRepository
import com.ctr.homestaybooking.data.source.request.BookingBody
import com.ctr.homestaybooking.data.source.response.Booking
import com.ctr.homestaybooking.data.source.response.BookingResponse
import com.ctr.homestaybooking.data.source.response.CaptureMoMoApiResponse
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

    private var booking: Booking? = null

    override fun getBookingBody() = bookingBody

    override fun getBooking() = booking

    override fun addBooking(): Single<BookingResponse> {
        return placeRepository.addBooking(getBookingBody())
            .addProgressLoading()
    }

    override fun getBookingById(id: Int): Single<BookingResponse> {
        return placeRepository.getBookingById(id)
            .addProgressLoading()
            .doOnSuccess {
                booking = it.booking
            }
    }

    override fun getUserId() = localRepository.getUserId()

    override fun getUserInfo(): Single<UserResponse> {
        return userRepository.getUserById(localRepository.getUserId())
            .addProgressLoading()
    }

    override fun changeBookingStatus(
        bookingId: Int,
        bookingStatus: BookingStatus
    ): Single<BookingResponse> {
        return placeRepository.changeBookingStatus(bookingId, bookingStatus)
            .addProgressLoading()
            .doOnSuccess {
                booking = it.booking
            }
    }

    override fun requestPayment(bookingId: Int): Single<CaptureMoMoApiResponse> {
        return placeRepository.requestPayment(bookingId).addProgressLoading()
    }

    override fun getProgressObservable(): BehaviorSubject<Boolean> =
        progressBarDialogObservable
}
