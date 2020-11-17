package com.ctr.homestaybooking.data.source

import com.ctr.homestaybooking.data.model.BookingStatus
import com.ctr.homestaybooking.data.source.datasource.PlaceDataSource
import com.ctr.homestaybooking.data.source.remote.PlaceRemoteDataSource
import com.ctr.homestaybooking.data.source.request.BookingBody
import com.ctr.homestaybooking.data.source.request.RoomsReservationBody
import com.ctr.homestaybooking.data.source.response.BookingHistoryResponse
import io.reactivex.Single

/**
 * Created by at-trinhnguyen2 on 2020/06/19
 */
class PlaceRepository : PlaceDataSource {

    private val placeRemoteDataSource = PlaceRemoteDataSource()

    override fun getPlaces() = placeRemoteDataSource.getPlaces()

    override fun getPlaceDetail(placeId: Int) = placeRemoteDataSource.getPlaceDetail(placeId)

    override fun addBooking(bookingBody: BookingBody) =
        placeRemoteDataSource.addBooking(bookingBody)

    override fun getBookingHistory(id: Int): Single<BookingHistoryResponse> =
        placeRemoteDataSource.getBookingHistory(id)

    override fun getAllRoomByBrand(brandId: Int) = placeRemoteDataSource.getAllRoomByBrand(brandId)

    override fun getAllRoomStatus(brandId: Int, startDate: String, endDate: String) =
        placeRemoteDataSource.getAllRoomStatus(brandId, startDate, endDate)

    override fun addNewRoomsReservation(
        numberOfRooms: Int,
        listPromoCode: List<String>?,
        roomsReservationBody: RoomsReservationBody
    ) = placeRemoteDataSource.addNewRoomsReservation(
        numberOfRooms,
        listPromoCode,
        roomsReservationBody
    )

    override fun changeBookingStatus(bookingId: Int, bookingStatus: BookingStatus) =
        placeRemoteDataSource.changeBookingStatus(bookingId, bookingStatus)

    override fun changeRoomReservationStatus(roomReservationId: Int) =
        placeRemoteDataSource.changeRoomReservationStatus(roomReservationId)
}
