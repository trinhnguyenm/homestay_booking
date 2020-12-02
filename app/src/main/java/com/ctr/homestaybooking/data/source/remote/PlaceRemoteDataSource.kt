package com.ctr.homestaybooking.data.source.remote

import com.ctr.homestaybooking.data.model.BookingStatus
import com.ctr.homestaybooking.data.source.datasource.PlaceDataSource
import com.ctr.homestaybooking.data.source.remote.network.ApiClient
import com.ctr.homestaybooking.data.source.remote.network.ApiService
import com.ctr.homestaybooking.data.source.request.BookingBody
import com.ctr.homestaybooking.data.source.response.BookingResponse
import io.reactivex.Single

/**
 * Created by at-trinhnguyen2 on 2020/06/19
 */
class PlaceRemoteDataSource(private val apiService: ApiService = ApiClient.getInstance(null).service) :
    PlaceDataSource {
    override fun getPlaces() = apiService.getPlaces()

    override fun getPlacesByHostId(id: Int) = apiService.getPlacesByHostId(id)

    override fun getPlaceDetail(placeId: Int) = apiService.getPlaceDetail(placeId)

    override fun addBooking(bookingBody: BookingBody): Single<BookingResponse> =
        apiService.addBooking(bookingBody)

    override fun getBookingById(id: Int): Single<BookingResponse> =
        apiService.getBookingById(id)

    override fun getAllRoomStatus(brandId: Int, startDate: String, endDate: String) =
        apiService.getAllRoomStatus(brandId, startDate, endDate)

    override fun getBookingHistory(id: Int) = apiService.getBookingHistory(id)

    override fun getBookingByHostId(id: Int) = apiService.getBookingByHostId(id)

    override fun changeBookingStatus(bookingId: Int, bookingStatus: BookingStatus) =
        apiService.changeBookingStatus(bookingId, bookingStatus)

    override fun requestPayment(bookingId: Int) =
        apiService.requestPayment(bookingId)

}
