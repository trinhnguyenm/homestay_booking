package com.ctr.homestaybooking.data.source.datasource

import com.ctr.homestaybooking.data.model.BookingStatus
import com.ctr.homestaybooking.data.model.SearchBody
import com.ctr.homestaybooking.data.source.request.BookingBody
import com.ctr.homestaybooking.data.source.request.PlaceBody
import com.ctr.homestaybooking.data.source.request.ReviewBody
import com.ctr.homestaybooking.data.source.response.*
import io.reactivex.Single

/**
 * Created by at-trinhnguyen2 on 2020/06/19
 */
interface PlaceDataSource {
    fun getPlaces(): Single<PlaceResponse>

    fun getPlaceDetail(placeId: Int): Single<PlaceDetailResponse>

    fun getAllRoomStatus(brandId: Int, startDate: String, endDate: String): Single<RoomTypeResponse>

    fun addBooking(bookingBody: BookingBody): Single<BookingResponse>

    fun getBookingHistory(id: Int): Single<BookingHistoryResponse>

    fun changeBookingStatus(bookingId: Int, bookingStatus: BookingStatus): Single<BookingResponse>

    fun requestPayment(bookingId: Int): Single<CaptureMoMoApiResponse>

    fun getPlacesByHostId(id: Int): Single<HostPlaceResponse>

    fun getBookingByHostId(id: Int): Single<BookingHistoryResponse>

    fun getBookingById(id: Int): Single<BookingResponse>
    fun editPlace(placeBody: PlaceBody): Single<PlaceDetailResponse>
    fun getProvinces(): Single<ProvinceResponse>
    fun getProvinceById(id: Int): Single<ProvinceDetailResponse>
    fun getPlaceTypes(): Single<PlaceTypeResponse>
    fun getCalendarByPlaceId(id: Int): Single<CalendarResponse>
    fun deletePlace(id: Int): Single<PlaceDetailResponse>
    fun reversePlaceStatusByID(id: Int): Single<PlaceDetailResponse>
    fun searchPlace(searchBody: SearchBody): Single<PlaceResponse>
    fun addReview(reviewBody: ReviewBody): Single<Review>
}
