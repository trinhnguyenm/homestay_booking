package com.ctr.homestaybooking.ui.booking

import com.ctr.homestaybooking.data.model.BookingStatus
import com.ctr.homestaybooking.data.source.request.BookingBody
import com.ctr.homestaybooking.data.source.request.ReviewBody
import com.ctr.homestaybooking.data.source.response.*
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by at-trinhnguyen2 on 2020/06/21
 */
interface BookingVMContract {

    fun getProgressObservable(): BehaviorSubject<Boolean>

    fun getUserInfo(): Single<UserResponse>

    fun getUserId(): Int

    fun getBookingBody(): BookingBody

    fun addBooking(): Single<BookingResponse>

    fun changeBookingStatus(bookingId: Int, bookingStatus: BookingStatus): Single<BookingResponse>
    fun requestPayment(bookingId: Int): Single<CaptureMoMoApiResponse>
    fun getBookingById(id: Int): Single<BookingResponse>
    fun getBooking(): Booking?
    fun getReviewBody(): ReviewBody
    fun addReview(): Single<Review>
}
