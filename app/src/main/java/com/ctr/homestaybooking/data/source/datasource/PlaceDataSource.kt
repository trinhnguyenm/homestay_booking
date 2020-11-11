package com.ctr.homestaybooking.data.source.datasource

import com.ctr.homestaybooking.data.source.request.RoomsReservationBody
import com.ctr.homestaybooking.data.source.response.*
import io.reactivex.Single

/**
 * Created by at-trinhnguyen2 on 2020/06/19
 */
interface PlaceDataSource {
    fun getPlaces(): Single<PlaceResponse>

    fun getPlaceDetail(placeId: Int): Single<PlaceDetailResponse>

    fun getAllRoomByBrand(brandId: Int): Single<RoomResponse>

    fun getAllRoomStatus(brandId: Int, startDate: String, endDate: String): Single<RoomTypeResponse>

    fun addNewRoomsReservation(
        numberOfRooms: Int,
        listPromoCode: List<String>?,
        roomsReservationBody: RoomsReservationBody
    ): Single<MyBookingResponse>

    fun getBookingHistory(): Single<MyBookingResponse>

    fun changeReservationStatus(reservationId: Int): Single<ChangeReservationStatusResponse>

    fun changeRoomReservationStatus(roomReservationId: Int): Single<ChangeRoomReservationStatusResponse>
}