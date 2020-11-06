package com.ctr.homestaybooking.data.source

import com.ctr.homestaybooking.data.source.datasource.PlaceDataSource
import com.ctr.homestaybooking.data.source.remote.PlaceRemoteDataSource
import com.ctr.homestaybooking.data.source.request.RoomsReservationBody

/**
 * Created by at-trinhnguyen2 on 2020/06/19
 */
class PlaceRepository : PlaceDataSource {

    private val placeRemoteDataSource = PlaceRemoteDataSource()

    override fun getPlaces() = placeRemoteDataSource.getPlaces()

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

    override fun getAllPromoStillActive() = placeRemoteDataSource.getAllPromoStillActive()

    override fun getBookingHistory() = placeRemoteDataSource.getBookingHistory()

    override fun changeReservationStatus(reservationId: Int) =
        placeRemoteDataSource.changeReservationStatus(reservationId)

    override fun changeRoomReservationStatus(roomReservationId: Int) =
        placeRemoteDataSource.changeRoomReservationStatus(roomReservationId)
}
